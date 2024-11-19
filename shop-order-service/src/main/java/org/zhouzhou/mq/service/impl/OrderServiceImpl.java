package org.zhouzhou.mq.service.impl;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import java.math.BigDecimal;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.zhouzhou.mq.ICouponService;
import org.zhouzhou.mq.IGoodsService;
import org.zhouzhou.mq.IOrderService;
import org.zhouzhou.mq.IUserService;
import org.zhouzhou.mq.constant.ShopCode;
import org.zhouzhou.mq.entity.MQEntity;
import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.exception.CastException;
import org.zhouzhou.mq.mapper.ShopOrderMapper;
import org.zhouzhou.mq.pojo.ShopCoupon;
import org.zhouzhou.mq.pojo.ShopGoods;
import org.zhouzhou.mq.pojo.ShopOrder;
import org.zhouzhou.mq.pojo.ShopOrderGoodsLog;
import org.zhouzhou.mq.pojo.ShopUser;
import org.zhouzhou.mq.pojo.ShopUserMoneyLog;
import org.zhouzhou.mq.utils.IDWorker;

@Slf4j
@Component
@Service(interfaceClass = IOrderService.class)
public class OrderServiceImpl implements IOrderService {
    @Reference
    private IGoodsService goodsService;

    @Reference
    private IUserService userService;

    @Reference
    private ICouponService couponService;

    @Resource
    private IDWorker idWorker;

    @Resource
    private ShopOrderMapper orderMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Value("${mq.order.topic}")
    private String topic;

    @Value("${mq.order.tag.cancel}")
    private String cancelTag;

    @Override
    public Result confirmOrder(ShopOrder order) {
        //1.校验订单
        checkOrder(order);
        //2.生成预订单
        Long orderId = savePreOrder(order);
        order.setOrderId(orderId);
        try {
            //3.扣减库存
            reduceGoodsNum(order);
            //4.扣减优惠券
            changeCouponStatus(order);
            //5.使用余额
            reduceMoneyPaid(order);

            // 手动抛出异常
            CastException.cast(ShopCode.SHOP_FAIL);
            //6.确认订单
            updateOrderStatus(order);
            log.info("订单:["+orderId+"]确认成功");
            return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
        } catch (Exception e) {
            //确认订单失败,发送消息
            MQEntity cancelOrderMQ = new MQEntity();
            cancelOrderMQ.setOrderId(order.getOrderId());
            cancelOrderMQ.setCouponId(order.getCouponId());
            cancelOrderMQ.setGoodsId(order.getGoodsId());
            cancelOrderMQ.setGoodsNumber(order.getGoodsNumber());
            cancelOrderMQ.setUserId(order.getUserId());
            cancelOrderMQ.setUserMoney(order.getMoneyPaid());
            try {
                sendMessage(topic,
                    cancelTag,
                    cancelOrderMQ.getOrderId().toString(),
                    JSON.toJSONString(cancelOrderMQ));
            } catch (Exception e1) {
                e1.printStackTrace();
                CastException.cast(ShopCode.SHOP_MQ_SEND_MESSAGE_FAIL);
            }
            return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_FAIL.getMessage());
        }
    }


    private void sendMessage(String topic, String tags, String keys, String body) throws Exception {
        //判断Topic是否为空
        if (StringUtils.isEmpty(topic)) {
            CastException.cast(ShopCode.SHOP_MQ_TOPIC_IS_EMPTY);
        }
        //判断消息内容是否为空
        if (StringUtils.isEmpty(body)) {
            CastException.cast(ShopCode.SHOP_MQ_MESSAGE_BODY_IS_EMPTY);
        }
        //消息体
        Message message = new Message(topic, tags, keys, body.getBytes());
        //发送消息
        rocketMQTemplate.getProducer().send(message);
    }

    private void checkOrder(ShopOrder order) {
        // 校验订单是否存在
        if (order == null) {
            CastException.cast(ShopCode.SHOP_ORDER_INVALID);
        }
        // 校验订单商品是否存在
        ShopGoods goods = goodsService.findOne(order.getGoodsId());
        if (goods == null) {
            CastException.cast(ShopCode.SHOP_GOODS_NO_EXIST);
        }
        // 校验下单用户是否存在
        ShopUser user = userService.findOne(order.getUserId());
        if (user == null) {
            CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
        }
        // 校验订单商品单价是否合法
        if (order.getGoodsPrice().compareTo(goods.getGoodsPrice()) != 0) {
            CastException.cast(ShopCode.SHOP_GOODS_PRICE_INVALID);
        }
        // 校验订单商品数量是否合法
        if (order.getGoodsNumber() >= goods.getGoodsNumber()) {
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        log.info("校验订单通过");
    }

    /**
     * 扣减用户余额
     *
     * @param order
     */
    private void reduceMoneyPaid(ShopOrder order) {
        if (order.getMoneyPaid() != null && order.getMoneyPaid().compareTo(BigDecimal.ZERO) == 1) {
            ShopUserMoneyLog userMoneyLog = new ShopUserMoneyLog();
            userMoneyLog.setOrderId(order.getOrderId());
            userMoneyLog.setUserId(order.getUserId());
            userMoneyLog.setUseMoney(order.getMoneyPaid());
            userMoneyLog.setMoneyLogType(ShopCode.SHOP_USER_MONEY_PAID.getCode());
            Result result = userService.updateMoneyPaid(userMoneyLog);
            if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
                CastException.cast(ShopCode.SHOP_USER_MONEY_REDUCE_FAIL);
            }
            log.info("订单:" + order.getOrderId() + ",扣减余额成功");
        }
    }

    private void changeCouponStatus(ShopOrder order) {
        // 判断用户是否使用优惠卷
        if (order.getCouponId()!=null) {
            // 封装优惠卷对象
            ShopCoupon coupon = couponService.findOne(order.getCouponId());
            coupon.setIsUsed(ShopCode.SHOP_COUPON_ISUSED.getCode());
            coupon.setUsedTime(new Date());
            coupon.setOrderId(order.getOrderId());
            Result result = couponService.updateCouponStatus(coupon);
            if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
                CastException.cast(ShopCode.SHOP_COUPON_USE_FAIL);
            }
            log.info("订单:[" + order.getOrderId() + "]使用优惠卷[" + order.getCouponPaid() +"元]成功");
        }
    }

    /**
     * 确认订单
     *
     * @param order
     */
    private void updateOrderStatus(ShopOrder order) {
        order.setOrderStatus(ShopCode.SHOP_ORDER_CONFIRM.getCode());
        order.setPayStatus(ShopCode.SHOP_ORDER_PAY_STATUS_NO_PAY.getCode());
        order.setConfirmTime(new Date());
        int r = orderMapper.updateByPrimaryKey(order);
        if (r <= 0) {// 订单确认失败
            CastException.cast(ShopCode.SHOP_ORDER_CONFIRM_FAIL);
        }
        log.info("订单:" + order.getOrderId() + ",确认订单成功");
    }

    private void reduceGoodsNum(ShopOrder order) {
        ShopOrderGoodsLog orderGoodsLog = new ShopOrderGoodsLog();
        orderGoodsLog.setGoodsId(order.getGoodsId());
        orderGoodsLog.setOrderId(order.getOrderId());
        orderGoodsLog.setGoodsNumber(order.getGoodsNumber());
        Result result = goodsService.reduceGoodsNum(orderGoodsLog);
        if (result.getSuccess().equals(ShopCode.SHOP_FAIL.getSuccess())) {
            CastException.cast(ShopCode.SHOP_REDUCE_GOODS_NUM_FAIL);
        }
        log.info("订单:[" + order.getOrderId() + "]扣减库存[" + order.getGoodsNumber() +"个]成功");
    }

    /**
     * 生成预订单
     * @param order
     * @return
     */
    private Long savePreOrder(ShopOrder order) {
        // 1. 设置订单状态不可见
        order.setOrderStatus(ShopCode.SHOP_ORDER_NO_CONFIRM.getCode());
        // 2. 设置订单id
        order.setOrderId(idWorker.nextId());
        // 3. 核算运费是否正确
        BigDecimal shippingFee = calculateShippingFee(order.getOrderAmount());
        if (order.getShippingFee().compareTo(shippingFee) != 0){
            CastException.cast(ShopCode.SHOP_ORDER_SHIPPINGFEE_INVALID);
        }
        // 4. 核算订单总价格是否正确
        BigDecimal orderAmount = order.getGoodsPrice().multiply(new BigDecimal(order.getGoodsNumber()));
        orderAmount.add(shippingFee);
        if (orderAmount.compareTo(order.getOrderAmount()) != 0) {
            CastException.cast(ShopCode.SHOP_ORDERMOUNT_INVALID);
        }
        // 5. 客户是否使用余额
        BigDecimal moneyPaid = order.getMoneyPaid();
        if (moneyPaid!=null) {
            // 比较余额是否大于0
            int r = order.getMoneyPaid().compareTo(BigDecimal.ZERO);
            // 余额小于0
            if (r == -1) {
                CastException.cast(ShopCode.SHOP_MONEY_PAID_LESS_ZERO);
            }
            // 余额大于0
            if (r == 1) {
                // 查询用户信息
                ShopUser user = userService.findOne(order.getUserId());
                if (user == null) {
                    CastException.cast(ShopCode.SHOP_USER_NO_EXIST);
                }
                // 比较余额是否大于用户余额
                if (user.getUserMoney().compareTo(order.getMoneyPaid()) == -1) {
                    CastException.cast(ShopCode.SHOP_MONEY_PAID_INVALIS);
                }
            }
        } else {
            order.setMoneyPaid(BigDecimal.ZERO);
        }
        // 6. 客户是否使用优惠卷
        Long couponId = order.getCouponId();
        if (couponId != null) {
            ShopCoupon coupon = couponService.findOne(couponId);
            // 优惠卷不存在
            if (coupon == null) {
                CastException.cast(ShopCode.SHOP_COUPON_INVALIED);
            }
            // 优惠卷已使用
            if (coupon.getIsUsed().toString()
                .equals(ShopCode.SHOP_COUPON_ISUSED.getCode().toString())) {
                CastException.cast(ShopCode.SHOP_COUPON_ISUSED);
            }
            order.setCouponPaid(coupon.getCouponPrice());
        } else {
            order.setCouponPaid(BigDecimal.ZERO);
        }

        // 7 核算订单支付金额 订单总金额 - 余额 - 优惠卷金额
        BigDecimal payAmount = order.getOrderAmount().subtract(order.getMoneyPaid()).subtract(order.getCouponPaid());
        order.setPayAmount(payAmount);
        // 8 设置下单时间
        order.setAddTime(new Date());
        // 9 保存订单到数据库
        int r = orderMapper.insert(order);
        if (ShopCode.SHOP_SUCCESS.getCode() != r) {
            CastException.cast(ShopCode.SHOP_ORDER_SAVE_ERROR);
        }
        log.info("订单:["+order.getOrderId()+"]预订单生成成功");
        // 10 返回订单 ID
        return order.getOrderId();
    }

    /**
     * 核算运费
     *
     * @param orderAmount
     * @return
     */
    private BigDecimal calculateShippingFee(BigDecimal orderAmount) {
        // 订单但金额大于等于100 免运费
        if (orderAmount.compareTo(BigDecimal.valueOf(100)) == 1) {
            return BigDecimal.ZERO;
        } else {
            // 否则收运费10元
            return BigDecimal.valueOf(10);
        }
    }

    @Override
    public Result cancelOrder(ShopOrder order) {
        return null;
    }
}

package org.zhouzhou.mq.mq;

import com.alibaba.fastjson.JSON;
import java.io.UnsupportedEncodingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhouzhou.mq.constant.ShopCode;
import org.zhouzhou.mq.entity.MQEntity;
import org.zhouzhou.mq.mapper.ShopCouponMapper;
import org.zhouzhou.mq.pojo.ShopCoupon;

@Slf4j
@Component
@RocketMQMessageListener(topic = "${mq.order.topic}",consumerGroup = "${mq.order.consumer.group.name}",
    messageModel = MessageModel.BROADCASTING )
public class CancelMQListener implements RocketMQListener<MessageExt> {


    @Autowired
    private ShopCouponMapper couponMapper;

    @Override
    public void onMessage(MessageExt message) {

        try {
            //1. 解析消息内容
            String body = new String(message.getBody(), "UTF-8");
            MQEntity mqEntity = JSON.parseObject(body, MQEntity.class);
            log.info("接收到消息");
            //2. 查询优惠券信息
            ShopCoupon coupon = couponMapper.selectByPrimaryKey(mqEntity.getCouponId());
            //3.更改优惠券状态
            coupon.setUsedTime(null);
            coupon.setIsUsed(ShopCode.SHOP_COUPON_UNUSED.getCode());
            coupon.setOrderId(null);
            couponMapper.updateByPrimaryKey(coupon);
            log.info("回退优惠券成功");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            log.error("回退优惠券失败");
        }

    }
}
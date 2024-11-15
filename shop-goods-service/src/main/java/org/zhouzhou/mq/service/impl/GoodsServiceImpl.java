package org.zhouzhou.mq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import java.util.Date;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zhouzhou.mq.ICouponService;
import org.zhouzhou.mq.IGoodsService;
import org.zhouzhou.mq.constant.ShopCode;
import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.exception.CastException;
import org.zhouzhou.mq.mapper.ShopGoodsMapper;
import org.zhouzhou.mq.mapper.ShopOrderGoodsLogMapper;
import org.zhouzhou.mq.pojo.ShopGoods;
import org.zhouzhou.mq.pojo.ShopOrderGoodsLog;

@Component
@Service(interfaceClass = IGoodsService.class)
@Slf4j
public class GoodsServiceImpl implements IGoodsService {

    @Resource
    private ShopGoodsMapper goodsMapper;

    @Resource
    private ShopOrderGoodsLogMapper orderGoodsLogMapper;

    @Override
    public ShopGoods findOne(Long goodsId) {
        if (goodsId == null) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        return goodsMapper.selectByPrimaryKey(goodsId);
    }

    @Override
    public Result reduceGoodsNum(ShopOrderGoodsLog orderGoodsLog) {
        if (orderGoodsLog == null ||
        orderGoodsLog.getOrderId() == null ||
        orderGoodsLog.getGoodsNumber() == null ||
        orderGoodsLog.getGoodsNumber().intValue() <= 0) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        ShopGoods goods = goodsMapper.selectByPrimaryKey(orderGoodsLog.getGoodsId());
        // 判断是不是库存不足
        if (goods.getGoodsNumber()< orderGoodsLog.getGoodsNumber()){
            CastException.cast(ShopCode.SHOP_GOODS_NUM_NOT_ENOUGH);
        }
        // 扣减库存
        goods.setGoodsNumber(goods.getGoodsNumber() - orderGoodsLog.getGoodsNumber());
        goodsMapper.updateByPrimaryKey(goods);

        // 记录库存操作日志
        orderGoodsLog.setGoodsNumber(-(orderGoodsLog.getGoodsNumber()));
        orderGoodsLog.setLogTime(new Date());
        orderGoodsLogMapper.insert(orderGoodsLog);

        return new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
    }
}

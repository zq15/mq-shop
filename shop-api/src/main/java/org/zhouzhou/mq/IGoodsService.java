package org.zhouzhou.mq;

import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.pojo.ShopGoods;
import org.zhouzhou.mq.pojo.ShopOrderGoodsLog;

public interface IGoodsService {
    /**
     * 根据ID 查询 Goods
     *
     * @param goodsId
     * @return
     */
    ShopGoods findOne(Long goodsId);

    /**
     * 扣减库存
     *
     * @param orderGoodsLog
     * @return
     */
    Result reduceGoodsNum(ShopOrderGoodsLog orderGoodsLog);
}

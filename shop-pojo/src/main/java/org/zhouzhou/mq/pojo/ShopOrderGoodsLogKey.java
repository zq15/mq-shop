package org.zhouzhou.mq.pojo;

import java.io.Serializable;

public class ShopOrderGoodsLogKey implements Serializable {
    private Long goodsId;

    private Long orderId;

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }
}
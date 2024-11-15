package org.zhouzhou.mq;

import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.pojo.ShopOrder;

public interface IOrderService {
    /**
     * 下单接口
     *
     * @param order
     * @return
     */
    Result confirmOrder(ShopOrder order);

    /**
     * 取消订单
     *
     * @param order
     * @return
     */
    Result cancelOrder(ShopOrder order);

}

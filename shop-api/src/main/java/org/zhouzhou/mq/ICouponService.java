package org.zhouzhou.mq;

import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.pojo.ShopCoupon;

/**
 * 优惠券接口
 */
public interface ICouponService {
    /**
     * 根据ID查询优惠券
     *
     * @param couponId
     * @return
     */
    ShopCoupon findOne(Long couponId);

    /**
     * 更新优惠券状态
     *
     * @param coupon
     * @return
     */
    Result updateCouponStatus(ShopCoupon coupon);
}


package org.zhouzhou.mq.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zhouzhou.mq.ICouponService;
import org.zhouzhou.mq.constant.ShopCode;
import org.zhouzhou.mq.entity.Result;
import org.zhouzhou.mq.exception.CastException;
import org.zhouzhou.mq.mapper.ShopCouponMapper;
import org.zhouzhou.mq.pojo.ShopCoupon;

@Component
@Service(interfaceClass = ICouponService.class)
@Slf4j
public class CouponServiceImpl implements ICouponService {

    @Autowired
    private ShopCouponMapper couponMapper;

    @Override
    public ShopCoupon findOne(Long couponId) {
        if (couponId == null) {
            CastException.cast(ShopCode.SHOP_REQUEST_PARAMETER_VALID);
        }
        return couponMapper.selectByPrimaryKey(couponId);
    }

    @Override
    public Result updateCouponStatus(ShopCoupon coupon) {

        if (coupon == null || coupon.getCouponId() == null) {
            return new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_REQUEST_PARAMETER_VALID.getMessage());
        }
        Result result;
        try {
            //更新优惠券状态
            couponMapper.updateByPrimaryKey(coupon);
            result = new Result(ShopCode.SHOP_SUCCESS.getSuccess(), ShopCode.SHOP_SUCCESS.getMessage());
        } catch (Exception e) {
            log.info(e.toString());
            result = new Result(ShopCode.SHOP_FAIL.getSuccess(), ShopCode.SHOP_FAIL.getMessage());
        }

        return result;
    }
}

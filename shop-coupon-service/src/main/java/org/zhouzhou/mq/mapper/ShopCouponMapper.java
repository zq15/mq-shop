package org.zhouzhou.mq.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopCoupon;
import org.zhouzhou.mq.pojo.ShopCouponExample;

@Mapper
public interface ShopCouponMapper {
    long countByExample(ShopCouponExample example);

    int deleteByExample(ShopCouponExample example);

    int deleteByPrimaryKey(Long couponId);

    int insert(ShopCoupon record);

    int insertSelective(ShopCoupon record);

    List<ShopCoupon> selectByExample(ShopCouponExample example);

    ShopCoupon selectByPrimaryKey(Long couponId);

    int updateByExampleSelective(@Param("record") ShopCoupon record, @Param("example") ShopCouponExample example);

    int updateByExample(@Param("record") ShopCoupon record, @Param("example") ShopCouponExample example);

    int updateByPrimaryKeySelective(ShopCoupon record);

    int updateByPrimaryKey(ShopCoupon record);
}
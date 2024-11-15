package org.zhouzhou.mq.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopOrderGoodsLog;
import org.zhouzhou.mq.pojo.ShopOrderGoodsLogExample;
import org.zhouzhou.mq.pojo.ShopOrderGoodsLogKey;

@Mapper
public interface ShopOrderGoodsLogMapper {
    long countByExample(ShopOrderGoodsLogExample example);

    int deleteByExample(ShopOrderGoodsLogExample example);

    int deleteByPrimaryKey(ShopOrderGoodsLogKey key);

    int insert(ShopOrderGoodsLog record);

    int insertSelective(ShopOrderGoodsLog record);

    List<ShopOrderGoodsLog> selectByExample(ShopOrderGoodsLogExample example);

    ShopOrderGoodsLog selectByPrimaryKey(ShopOrderGoodsLogKey key);

    int updateByExampleSelective(@Param("record") ShopOrderGoodsLog record, @Param("example") ShopOrderGoodsLogExample example);

    int updateByExample(@Param("record") ShopOrderGoodsLog record, @Param("example") ShopOrderGoodsLogExample example);

    int updateByPrimaryKeySelective(ShopOrderGoodsLog record);

    int updateByPrimaryKey(ShopOrderGoodsLog record);
}
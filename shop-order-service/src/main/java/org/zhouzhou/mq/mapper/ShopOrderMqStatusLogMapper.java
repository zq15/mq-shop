package org.zhouzhou.mq.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopOrderMqStatusLog;
import org.zhouzhou.mq.pojo.ShopOrderMqStatusLogExample;

@Mapper
public interface ShopOrderMqStatusLogMapper {
    long countByExample(ShopOrderMqStatusLogExample example);

    int deleteByExample(ShopOrderMqStatusLogExample example);

    int deleteByPrimaryKey(Long orderId);

    int insert(ShopOrderMqStatusLog record);

    int insertSelective(ShopOrderMqStatusLog record);

    List<ShopOrderMqStatusLog> selectByExample(ShopOrderMqStatusLogExample example);

    ShopOrderMqStatusLog selectByPrimaryKey(Long orderId);

    int updateByExampleSelective(@Param("record") ShopOrderMqStatusLog record, @Param("example") ShopOrderMqStatusLogExample example);

    int updateByExample(@Param("record") ShopOrderMqStatusLog record, @Param("example") ShopOrderMqStatusLogExample example);

    int updateByPrimaryKeySelective(ShopOrderMqStatusLog record);

    int updateByPrimaryKey(ShopOrderMqStatusLog record);
}
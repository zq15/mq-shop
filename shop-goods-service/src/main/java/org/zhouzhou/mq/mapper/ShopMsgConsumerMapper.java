package org.zhouzhou.mq.mapper;


import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopMsgConsumer;
import org.zhouzhou.mq.pojo.ShopMsgConsumerExample;
import org.zhouzhou.mq.pojo.ShopMsgConsumerKey;

@Mapper
public interface ShopMsgConsumerMapper {
    long countByExample(ShopMsgConsumerExample example);

    int deleteByExample(ShopMsgConsumerExample example);

    int deleteByPrimaryKey(ShopMsgConsumerKey key);

    int insert(ShopMsgConsumer record);

    int insertSelective(ShopMsgConsumer record);

    List<ShopMsgConsumer> selectByExample(ShopMsgConsumerExample example);

    ShopMsgConsumer selectByPrimaryKey(ShopMsgConsumerKey key);

    int updateByExampleSelective(@Param("record") ShopMsgConsumer record, @Param("example") ShopMsgConsumerExample example);

    int updateByExample(@Param("record") ShopMsgConsumer record, @Param("example") ShopMsgConsumerExample example);

    int updateByPrimaryKeySelective(ShopMsgConsumer record);

    int updateByPrimaryKey(ShopMsgConsumer record);
}
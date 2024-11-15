package org.zhouzhou.mq.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopMsgProvider;
import org.zhouzhou.mq.pojo.ShopMsgProviderExample;
import org.zhouzhou.mq.pojo.ShopMsgProviderKey;

@Mapper
public interface ShopMsgProviderMapper {
    long countByExample(ShopMsgProviderExample example);

    int deleteByExample(ShopMsgProviderExample example);

    int deleteByPrimaryKey(ShopMsgProviderKey key);

    int insert(ShopMsgProvider record);

    int insertSelective(ShopMsgProvider record);

    List<ShopMsgProvider> selectByExample(ShopMsgProviderExample example);

    ShopMsgProvider selectByPrimaryKey(ShopMsgProviderKey key);

    int updateByExampleSelective(@Param("record") ShopMsgProvider record, @Param("example") ShopMsgProviderExample example);

    int updateByExample(@Param("record") ShopMsgProvider record, @Param("example") ShopMsgProviderExample example);

    int updateByPrimaryKeySelective(ShopMsgProvider record);

    int updateByPrimaryKey(ShopMsgProvider record);
}
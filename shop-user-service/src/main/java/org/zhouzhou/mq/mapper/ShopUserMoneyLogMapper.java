package org.zhouzhou.mq.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopUserMoneyLog;
import org.zhouzhou.mq.pojo.ShopUserMoneyLogExample;
import org.zhouzhou.mq.pojo.ShopUserMoneyLogKey;

@Mapper
public interface ShopUserMoneyLogMapper {
    long countByExample(ShopUserMoneyLogExample example);

    int deleteByExample(ShopUserMoneyLogExample example);

    int deleteByPrimaryKey(ShopUserMoneyLogKey key);

    int insert(ShopUserMoneyLog record);

    int insertSelective(ShopUserMoneyLog record);

    List<ShopUserMoneyLog> selectByExample(ShopUserMoneyLogExample example);

    ShopUserMoneyLog selectByPrimaryKey(ShopUserMoneyLogKey key);

    int updateByExampleSelective(@Param("record") ShopUserMoneyLog record, @Param("example") ShopUserMoneyLogExample example);

    int updateByExample(@Param("record") ShopUserMoneyLog record, @Param("example") ShopUserMoneyLogExample example);

    int updateByPrimaryKeySelective(ShopUserMoneyLog record);

    int updateByPrimaryKey(ShopUserMoneyLog record);
}
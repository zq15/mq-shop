package org.zhouzhou.mq.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.zhouzhou.mq.pojo.ShopUser;
import org.zhouzhou.mq.pojo.ShopUserExample;

@Mapper
public interface ShopUserMapper {
    long countByExample(ShopUserExample example);

    int deleteByExample(ShopUserExample example);

    int deleteByPrimaryKey(Long userId);

    int insert(ShopUser record);

    int insertSelective(ShopUser record);

    List<ShopUser> selectByExample(ShopUserExample example);

    ShopUser selectByPrimaryKey(Long userId);

    int updateByExampleSelective(@Param("record") ShopUser record, @Param("example") ShopUserExample example);

    int updateByExample(@Param("record") ShopUser record, @Param("example") ShopUserExample example);

    int updateByPrimaryKeySelective(ShopUser record);

    int updateByPrimaryKey(ShopUser record);
}
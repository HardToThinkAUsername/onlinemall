package com.onlinemall.user.mapper;

import com.onlinemall.user.pojo.UserAddr;
import com.onlinemall.user.pojo.UserAddrExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserAddrMapper {
    long countByExample(UserAddrExample example);

    int deleteByExample(UserAddrExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserAddr record);

    int insertSelective(UserAddr record);

    List<UserAddr> selectByExample(UserAddrExample example);

    UserAddr selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserAddr record, @Param("example") UserAddrExample example);

    int updateByExample(@Param("record") UserAddr record, @Param("example") UserAddrExample example);

    int updateByPrimaryKeySelective(UserAddr record);

    int updateByPrimaryKey(UserAddr record);

    @Update("update tb_user_addr set def=0 where uid=#{uid}")
    void updateAllAddrUnDef(Long uid);
}
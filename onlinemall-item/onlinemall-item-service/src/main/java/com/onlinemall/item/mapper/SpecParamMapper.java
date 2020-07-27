package com.onlinemall.item.mapper;

import com.onlinemall.item.pojo.SpecParam;
import com.onlinemall.item.pojo.SpecParamExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SpecParamMapper {
    long countByExample(SpecParamExample example);

    int deleteByExample(SpecParamExample example);

    int deleteByPrimaryKey(Long id);

    int insert(SpecParam record);

    int insertSelective(SpecParam record);

    List<SpecParam> selectByExample(SpecParamExample example);

    SpecParam selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") SpecParam record, @Param("example") SpecParamExample example);

    int updateByExample(@Param("record") SpecParam record, @Param("example") SpecParamExample example);

    int updateByPrimaryKeySelective(SpecParam record);

    int updateByPrimaryKey(SpecParam record);

    List<SpecParam> selectByCid(@Param("cid") Long cid, @Param("generic") Boolean generic, @Param("searching") Boolean searching);

    @Select("select * from tb_spec_param where group_id=#{gid}")
    List<SpecParam> selectParamsByGid(Long gid);
}
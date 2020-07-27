package com.onlinemall.item.mapper;

import com.onlinemall.item.pojo.Spu;
import com.onlinemall.item.pojo.SpuExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SpuMapper {
    long countByExample(SpuExample example);

    int deleteByExample(SpuExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Spu record);

    int insertSelective(Spu record);

    List<Spu> selectByExample(SpuExample example);

    Spu selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Spu record, @Param("example") SpuExample example);

    int updateByExample(@Param("record") Spu record, @Param("example") SpuExample example);

    int updateByPrimaryKeySelective(Spu record);

    int updateByPrimaryKey(Spu record);
}
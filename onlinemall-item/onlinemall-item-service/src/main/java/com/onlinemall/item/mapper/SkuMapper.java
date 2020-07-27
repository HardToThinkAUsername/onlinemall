package com.onlinemall.item.mapper;

import com.onlinemall.item.pojo.Sku;
import com.onlinemall.item.pojo.SkuExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SkuMapper {
    long countByExample(SkuExample example);

    int deleteByExample(SkuExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Sku record);

    int insertSelective(Sku record);

    List<Sku> selectByExample(SkuExample example);

    Sku selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Sku record, @Param("example") SkuExample example);

    int updateByExample(@Param("record") Sku record, @Param("example") SkuExample example);

    int updateByPrimaryKeySelective(Sku record);

    int updateByPrimaryKey(Sku record);

    @Select("select * from tb_sku where spu_id=#{id}")
    List<Sku> selectSkusBySpuId(Long id);

    @Select("select spu_id from tb_sku where id = #{skuId}")
    Long selectSpuIdBySkuId(Long skuId);
}
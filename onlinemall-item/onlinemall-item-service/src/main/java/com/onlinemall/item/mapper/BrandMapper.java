package com.onlinemall.item.mapper;

import com.onlinemall.item.pojo.Brand;
import com.onlinemall.item.pojo.BrandExample;
import java.util.List;

import org.apache.ibatis.annotations.*;

@Mapper
public interface BrandMapper {
    long countByExample(BrandExample example);

    int deleteByExample(BrandExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Brand record);

    int insertSelective(Brand record);

    List<Brand> selectByExample(BrandExample example);

    Brand selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Brand record, @Param("example") BrandExample example);

    int updateByExample(@Param("record") Brand record, @Param("example") BrandExample example);

    int updateByPrimaryKeySelective(Brand record);

    int updateByPrimaryKey(Brand record);


    /**
     * 新增商品分类和品牌中间表数据
     * @param cid 商品分类id
     * @param bid 品牌id
     * @return
     */
    @Insert("INSERT INTO tb_category_brand(category_id, brand_id) VALUES (#{cid},#{bid})")
    int insertBrandAndCategory(@Param("cid") Long cid, @Param("bid") Long bid);

    /**
     * 根据品牌id删除所有 品牌-分类关联记录
     * @param id
     */
    @Delete("delete from tb_category_brand where brand_id = #{bid}")
    int deleteBrandAndCategoryByBid(Long id);

    @Select("select b.* from tb_brand b inner join tb_category_brand cb on b.id=cb.brand_id where cb.category_id=#{cid}")
    List<Brand> selectBrandsByCid(Long cid);
}
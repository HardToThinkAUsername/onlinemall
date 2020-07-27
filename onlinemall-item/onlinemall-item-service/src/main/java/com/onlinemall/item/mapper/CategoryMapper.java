package com.onlinemall.item.mapper;

import com.onlinemall.item.pojo.Category;
import com.onlinemall.item.pojo.CategoryExample;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CategoryMapper {
    long countByExample(CategoryExample example);

    int deleteByExample(CategoryExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Category record);

    int insertSelective(Category record);

    List<Category> selectByExample(CategoryExample example);

    Category selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByExample(@Param("record") Category record, @Param("example") CategoryExample example);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    /**
     * 根据品牌id查询商品分类
     * @param bid
     * @return
     */
    @Select("select * from tb_category where id in (select category_id from tb_category_brand where brand_id = #{bid})")
    List<Category> selectByBrandId(Long bid);


    @Update("update tb_category set name = #{name} where sort = #{sort} and parentId = #{parentId}")
    boolean updateBySortAndPid(String name, Integer sort, Long parentId);

    List<Category> selectByIdList(List<Long> ids);
}
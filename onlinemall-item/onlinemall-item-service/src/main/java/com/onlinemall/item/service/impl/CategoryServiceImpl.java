package com.onlinemall.item.service.impl;

import com.onlinemall.item.mapper.CategoryMapper;
import com.onlinemall.item.pojo.Category;
import com.onlinemall.item.pojo.CategoryExample;
import com.onlinemall.item.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Override
    public List<Category> queryCategoryByPid(Long pid) {
        CategoryExample example = new CategoryExample();
        CategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(pid);
        return categoryMapper.selectByExample(example);
    }

    @Override
    public List<Category> queryByBid(Long bid) {
        return this.categoryMapper.selectByBrandId(bid);
    }

    @Override
    public boolean addCategory(Category category) {
        if(category.getSort() == 1){//父节点的第一个子节点
            //修改父节点的isParent属性为true
            Category parent = new Category();
            parent.setIsParent(true);
            parent.setParentId(category.getParentId());
            this.categoryMapper.updateByPrimaryKeySelective(parent);
        }
        return this.categoryMapper.insert(category) > 0;
    }

    @Override
    public boolean editCateGory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return this.categoryMapper.updateByPrimaryKeySelective(category) > 0;
    }

    @Override
    public boolean deleteCategory(Long id) {
        return this.categoryMapper.deleteByPrimaryKey(id) > 0;
    }

    @Override
    public List<String> queryNamesByIds(List<Long> ids) {
        System.out.println(ids);
        List<Category> list = this.categoryMapper.selectByIdList(ids);
        List<String> names = new ArrayList<>();
        for(Category category : list){
            names.add(category.getName());
        }
        return names;
    }

    @Override
    public List<Category> queryAllByCid3(Long id) {
        Category c3 = this.categoryMapper.selectByPrimaryKey(id);
        Category c2 = this.categoryMapper.selectByPrimaryKey(c3.getParentId());
        Category c1 = this.categoryMapper.selectByPrimaryKey(c2.getParentId());
        return Arrays.asList(c1,c2,c3);
    }

}

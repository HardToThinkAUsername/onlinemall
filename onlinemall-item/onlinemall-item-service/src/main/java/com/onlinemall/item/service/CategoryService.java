package com.onlinemall.item.service;


import com.onlinemall.item.pojo.Category;

import java.util.List;

public interface CategoryService {
    List<Category> queryCategoryByPid(Long pid);

    List<Category> queryByBid(Long bid);

    boolean addCategory(Category category);

    boolean editCateGory(Long id, String name);

    boolean deleteCategory(Long id);

    List<String> queryNamesByIds(List<Long> asList);

    List<Category> queryAllByCid3(Long id);
}

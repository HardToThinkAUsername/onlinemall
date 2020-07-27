package com.onlinemall.item.service;

import com.github.pagehelper.PageInfo;
import com.onlinemall.item.pojo.Brand;

import java.util.List;

public interface BrandService {
    PageInfo<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc);
    void saveBrand(Brand brand, List<Long> cids) ;
    PageInfo<Brand> queryAll();

    boolean updateBrand(Brand brand, List<Long> cids);

    boolean deleteBrand(Long bid);

    List<Brand> queryBrandsByCid(Long cid);

    Brand queryBrandById(Long id);
}

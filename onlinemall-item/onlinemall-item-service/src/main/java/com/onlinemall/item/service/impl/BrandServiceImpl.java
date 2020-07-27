package com.onlinemall.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinemall.item.mapper.BrandMapper;
import com.onlinemall.item.pojo.Brand;
import com.onlinemall.item.pojo.BrandExample;
import com.onlinemall.item.service.BrandService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private BrandMapper brandMapper;


    @Override
    public PageInfo<Brand> queryBrandsByPage(String key, Integer page, Integer rows, String sortBy, Boolean desc) {
        // 初始化example对象
        BrandExample example = new BrandExample();
        BrandExample.Criteria criteria = example.createCriteria();
        BrandExample.Criteria criteria2 = example.createCriteria();

        // 根据name模糊查询，或者根据首字母查询
        if (StringUtils.isNotBlank(key)) {
            criteria.andNameLike("%" + key + "%");
            criteria2.andLetterEqualTo(key);
        }

        // 添加分页条件
        PageHelper.startPage(page, rows);

        // 添加排序条件
        if (StringUtils.isNotBlank(sortBy)) {
            example.setOrderByClause(sortBy + " " + (desc ? "desc" : "asc"));
        }
        example.or(criteria2);
        List<Brand> brands = this.brandMapper.selectByExample(example);
        // 包装成pageInfo
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        // 包装成分页结果集返回
        return pageInfo;
    }

    /**
     * 新增品牌
     *
     * @param brand
     * @param cids
     */
    @Transactional
    public void saveBrand(Brand brand, List<Long> cids) {

        // 先新增brand
        this.brandMapper.insertSelective(brand);

        // 在新增中间表
        cids.forEach(cid -> {
            this.brandMapper.insertBrandAndCategory(cid, brand.getId());
        });
    }

    @Override
    public PageInfo<Brand> queryAll() {
        List<Brand> brands = brandMapper.selectByExample(null);
        PageInfo<Brand> pageInfo = new PageInfo<>(brands);
        return pageInfo;

    }

    @Transactional
    @Override
    public boolean updateBrand(Brand brand, List<Long> cids) {
        Long brandId = brand.getId();
        if(brandId == null){
            return false;
        }
        //根据bid删除category_brand
        this.brandMapper.deleteBrandAndCategoryByBid(brandId);

        //添加category_brand
        cids.forEach(cid->{
            this.brandMapper.insertBrandAndCategory(cid,brandId);
        });

        //修改brand
        return this.brandMapper.updateByPrimaryKey(brand) > 0;

    }

    @Override
    public boolean deleteBrand(Long bid) {
        return this.brandMapper.deleteByPrimaryKey(bid) > 0;
    }

    @Override
    public List<Brand> queryBrandsByCid(Long cid) {
        return this.brandMapper.selectBrandsByCid(cid);
    }

    @Override
    public Brand queryBrandById(Long id) {
        return this.brandMapper.selectByPrimaryKey(id);
    }
}

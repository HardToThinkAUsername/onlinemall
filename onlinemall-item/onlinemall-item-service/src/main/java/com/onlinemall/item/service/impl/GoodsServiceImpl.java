package com.onlinemall.item.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinemall.item.bo.SpuBo;
import com.onlinemall.item.mapper.*;
import com.onlinemall.item.pojo.*;
import com.onlinemall.item.service.CategoryService;
import com.onlinemall.item.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private SpuMapper spuMapper;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private BrandMapper brandMapper;

    @Autowired
    private SpuDetailMapper spuDetailMapper;

    @Autowired
    private StockMapper stockMapper;

    @Autowired
    private SkuMapper skuMapper;

    @Autowired
    private AmqpTemplate amqpTemplate;

    private Logger logger = LoggerFactory.getLogger(GoodsServiceImpl.class);

    public void sendMessage(Long id, String type){
        // 发送消息
        try {
            this.amqpTemplate.convertAndSend("item." + type, id);
        } catch (Exception e) {
            logger.error("{}商品消息发送异常，商品id：{}", type, id, e);
        }
    }

    @Override
    public PageInfo<SpuBo> querySpuPoByPage(String key, Boolean saleable, Integer pageNum, Integer pageSize) {
        SpuExample example = new SpuExample();
        SpuExample.Criteria criteria = example.createCriteria();
        //搜索条件
        if(StringUtils.isNotBlank(key)){
            criteria.andTitleLike("%" + key + "%");
        }
        if(saleable != null){
            criteria.andSaleableEqualTo(saleable);
        }
        List<Spu> spus = new ArrayList<>();
        if(pageSize == -1){
            spus = this.spuMapper.selectByExample(null);
        }else{
            //分页条件
            PageHelper.startPage(pageNum,pageSize);

            //执行查询
            spus = this.spuMapper.selectByExample(example);
        }

        PageInfo pageInfo = new PageInfo(spus);

        List<SpuBo> spuBos = new ArrayList<>();
        spus.forEach(spu->{
            SpuBo spuBo = new SpuBo();
            //copy共同属性的值到新的对象
            BeanUtils.copyProperties(spu, spuBo);
            //查询分类名称
            List<String> names = this.categoryService.queryNamesByIds(Arrays.asList(spu.getCid1(), spu.getCid2(), spu.getCid3()));
            System.out.println(names);
            spuBo.setCname(StringUtils.join(names, "/"));
            //查询品牌的名称
            spuBo.setBname(this.brandMapper.selectByPrimaryKey(spu.getBrandId()).getName());
            spuBos.add(spuBo);
        });
        pageInfo.setList(spuBos);
        return pageInfo;
    }

    /**
     * 新增商品
     * @param spuBo
     */
    @Transactional
    public void saveGoods(SpuBo spuBo) {
        // 新增spu
        // 设置默认字段
        spuBo.setId(null);
        spuBo.setSaleable(true);
        spuBo.setValid(true);
        spuBo.setCreateTime(new Date());
        spuBo.setLastUpdateTime(spuBo.getCreateTime());
        this.spuMapper.insertSelective(spuBo);

        // 新增spuDetail
        SpuDetail spuDetail = spuBo.getSpuDetail();
        spuDetail.setSpuId(spuBo.getId());
        this.spuDetailMapper.insertSelective(spuDetail);

        saveSkuAndStock(spuBo);
        sendMessage(spuBo.getId(),"insert");
    }

    private void saveSkuAndStock(SpuBo spuBo) {
        spuBo.getSkus().forEach(sku -> {
            // 新增sku
            sku.setSpuId(spuBo.getId());
            sku.setCreateTime(new Date());
            sku.setLastUpdateTime(sku.getCreateTime());
            this.skuMapper.insertSelective(sku);

            // 新增库存
            Stock stock = new Stock();
            stock.setSkuId(sku.getId());
            stock.setStock(sku.getStock());
            this.stockMapper.insertSelective(stock);
        });
    }

    /**
     * 根据spuId查询spuDetail
     * @param spuId
     * @return
     */
    public SpuDetail querySpuDetailBySpuId(Long spuId) {

        return this.spuDetailMapper.selectByPrimaryKey(spuId);
    }

    /**
     * 根据spuId查询sku的集合
     * @param spuId
     * @return
     */
    public List<Sku> querySkusBySpuId(Long spuId) {
        if(spuId == null){
            return null;
        }
        SkuExample example = new SkuExample();
        SkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(spuId);
        List<Sku> skus = this.skuMapper.selectByExample(example);
        skus.forEach(s -> {
            Stock stock = this.stockMapper.selectByPrimaryKey(s.getId());
            s.setStock(stock.getStock());
        });
        return skus;
    }

    @Transactional
    public void updateGoods(SpuBo spu) {
        // 查询以前sku
        List<Sku> skus = this.querySkusBySpuId(spu.getId());
        // 如果以前存在，则删除
        if(!CollectionUtils.isEmpty(skus)) {
            List<Long> ids = skus.stream().map(s -> s.getId()).collect(Collectors.toList());
            // 删除以前库存
            StockExample example = new StockExample();
            StockExample.Criteria criteria = example.createCriteria();
            criteria.andSkuIdIn(ids);
            this.stockMapper.deleteByExample(example);

            // 删除以前的sku
            SkuExample example1 = new SkuExample();
            SkuExample.Criteria criteria1 = example1.createCriteria();
            criteria1.andSpuIdEqualTo(spu.getId());
            this.skuMapper.deleteByExample(example1);

        }
        // 新增sku和库存
        saveSkuAndStock(spu);

        // 更新spu
        spu.setLastUpdateTime(new Date());
        spu.setCreateTime(null);
        spu.setValid(null);
        spu.setSaleable(null);
        this.spuMapper.updateByPrimaryKeySelective(spu);

        // 更新spu详情
        this.spuDetailMapper.updateByPrimaryKeySelective(spu.getSpuDetail());
        this.sendMessage(spu.getId(),"update");
    }

    @Override
    public boolean deleteSpuById(Long id) {
        //查询spu下所有的sku
        List<Sku> skus = this.skuMapper.selectSkusBySpuId(id);
        System.out.println(skus.size());
        //提取sku中的id
        List<Long> skuIds = skus.stream().map(sku->sku.getId()).collect(Collectors.toList());

        //根据sku_id删除库存
        System.out.println(skuIds);
        this.stockMapper.deleteBySkuIds(skuIds);

        //根据spuId删除sku
        SkuExample example = new SkuExample();
        SkuExample.Criteria criteria = example.createCriteria();
        criteria.andSpuIdEqualTo(id);
        this.skuMapper.deleteByExample(example);

        //删除spu
        this.spuMapper.deleteByPrimaryKey(id);
        SpuDetailExample example1 = new SpuDetailExample();
        SpuDetailExample.Criteria criteria1 = example1.createCriteria();
        criteria1.andSpuIdEqualTo(id);


        return this.spuDetailMapper.deleteByExample(example1) > 0;
    }

    @Override
    public boolean saleableGoods(Long id, Boolean saleable) {
        Spu spu = new Spu();
        spu.setId(id);
        spu.setSaleable(saleable);
        return this.spuMapper.updateByPrimaryKeySelective(spu) > 0;
    }

    public Spu querySpuById(Long id) {

        return this.spuMapper.selectByPrimaryKey(id);
    }
    public Sku querySkuById(Long id) {
        return this.skuMapper.selectByPrimaryKey(id);
    }

    @Override
    public Long querySpuIdBySkuId(Long skuId) {
        if(skuId == null){
            return null;
        }
        return this.skuMapper.selectSpuIdBySkuId(skuId);
    }
}

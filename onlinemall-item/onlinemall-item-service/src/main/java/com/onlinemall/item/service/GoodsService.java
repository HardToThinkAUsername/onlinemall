package com.onlinemall.item.service;

import com.github.pagehelper.PageInfo;
import com.onlinemall.item.bo.SpuBo;
import com.onlinemall.item.pojo.Sku;
import com.onlinemall.item.pojo.Spu;
import com.onlinemall.item.pojo.SpuDetail;

import java.util.List;

public interface GoodsService {
    PageInfo<SpuBo> querySpuPoByPage(String key, Boolean saleable, Integer pageNum, Integer pageSize);

    void saveGoods(SpuBo spuBo);

    SpuDetail querySpuDetailBySpuId(Long spuId);

    List<Sku> querySkusBySpuId(Long spuId);

    void updateGoods(SpuBo spuBo);

    boolean deleteSpuById(Long id);

    boolean saleableGoods(Long id, Boolean saleable);

    Spu querySpuById(Long id);

    void sendMessage(Long id, String type);

    Sku querySkuById(Long id);

    Long querySpuIdBySkuId(Long skuId);
}

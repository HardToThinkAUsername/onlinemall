package com.onlinemall.item.api;

import com.github.pagehelper.PageInfo;
import com.onlinemall.item.bo.SpuBo;
import com.onlinemall.item.pojo.Sku;
import com.onlinemall.item.pojo.Spu;
import com.onlinemall.item.pojo.SpuDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping()
public interface GoodsApi {

    /**
     * 分页查询商品
     * @param pageNum
     * @param pageSize
     * @param saleable
     * @param key
     * @return
     */
    @GetMapping("spu/page")
    PageInfo<SpuBo> querySpuBoByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize
    );

    /**
     * 根据spu商品id查询详情
     * @param id
     * @return
     */
    @GetMapping("/spu/detail/{id}")
    SpuDetail querySpuDetailBySpuId(@PathVariable("id") Long id);

    /**
     * 根据spu的id查询sku
     * @param id
     * @return
     */
    @GetMapping("sku/list")
    List<Sku> querySkusBySpuId(@RequestParam(value = "id") Long id);

    /**
     * 根据spu的id查询spu
     * @param id
     * @return
     */
    @GetMapping("spu/{id}")
    Spu querySpuById(@PathVariable("id") Long id);

    @GetMapping("sku/{id}")
    Sku querySkuById(@PathVariable("id")Long id);
}
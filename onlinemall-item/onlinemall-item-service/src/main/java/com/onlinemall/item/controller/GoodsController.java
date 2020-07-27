package com.onlinemall.item.controller;

import com.github.pagehelper.PageInfo;
import com.onlinemall.item.bo.SpuBo;
import com.onlinemall.item.pojo.MyHttpStatus;
import com.onlinemall.item.pojo.Sku;
import com.onlinemall.item.pojo.Spu;
import com.onlinemall.item.pojo.SpuDetail;
import com.onlinemall.item.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @GetMapping("spu/page")
    public ResponseEntity<PageInfo<SpuBo>> querySpuBoByPage(
            @RequestParam(value = "key",required = false)String key,
            @RequestParam(value = "saleable",required = false)Boolean saleable,
            @RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,
            @RequestParam(value = "pageSize",defaultValue = "5")Integer pageSize
    ){
        PageInfo<SpuBo> pageInfo = this.goodsService.querySpuPoByPage(key, saleable, pageNum, pageSize);

        return ResponseEntity.ok(pageInfo);
    }

    @PostMapping("goods")
    public ResponseEntity<Void> saveGoods(@RequestBody SpuBo spuBo){
        this.goodsService.saveGoods(spuBo);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("spu/detail/{spuId}")
    public ResponseEntity<SpuDetail> querySpuDetailBySpuId(@PathVariable("spuId")Long spuId){
        SpuDetail spuDetail = this.goodsService.querySpuDetailBySpuId(spuId);
        if (spuDetail == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuDetail);
    }

    @GetMapping("sku/list")
    public ResponseEntity<List<Sku>> querySkusBySpuId(@RequestParam("id")Long spuId){
        System.out.println(spuId);
//        if(spuId == null){
//            return ResponseEntity.badRequest().build();
//        }
        List<Sku> skus = this.goodsService.querySkusBySpuId(spuId);
//        if (CollectionUtils.isEmpty(skus)) {
//            return ResponseEntity.notFound().build();
//        }
        return ResponseEntity.ok(skus);
    }

    @DeleteMapping("spu")
    public ResponseEntity<Void> deleteSpuById(@RequestParam("id")Long id){
        if(this.goodsService.deleteSpuById(id)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    @PutMapping("goods")
    public ResponseEntity<Void> updateGoods(@RequestBody SpuBo spuBo){
        this.goodsService.updateGoods(spuBo);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("spu")
    public ResponseEntity<Void> saleableGoods(
            @RequestParam(name = "id")Long id,
            @RequestParam(name = "saleable")Boolean saleable){
        if(this.goodsService.saleableGoods(id, saleable)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    @GetMapping("spu/{id}")
    public ResponseEntity<Spu> querySpuById(@PathVariable("id") Long id){
        Spu spu = this.goodsService.querySpuById(id);
        if(spu == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(spu);
    }
    @GetMapping("sku/{id}")
    public ResponseEntity<Sku> querySkuById(@PathVariable("id")Long id){
        Sku sku = this.goodsService.querySkuById(id);
        if (sku == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(sku);
    }

    @GetMapping("getSpuIdBySkuId")
    public ResponseEntity<Long> querySpuIdBySku(@RequestParam("skuId")Long skuId){
        Long spuID = this.goodsService.querySpuIdBySkuId(skuId);
        if(spuID == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(spuID);
    }
}

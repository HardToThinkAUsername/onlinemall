package com.onlinemall.item.controller;

import com.github.pagehelper.PageInfo;
import com.onlinemall.item.pojo.Brand;
import com.onlinemall.item.service.BrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("brand")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("page")
    public ResponseEntity<PageInfo<Brand>> queryBrandsByPage(
            @RequestParam(value = "key", required = false)String key,
            @RequestParam(value = "pageNum", defaultValue = "1")Integer page,
            @RequestParam(value = "pageSize", defaultValue = "5")Integer rows,
            @RequestParam(value = "sortBy", required = false)String sortBy,
            @RequestParam(value = "desc", required = false)Boolean desc
    ){
        PageInfo<Brand> result = null;
        if(rows == -1){
            result = this.brandService.queryAll();
        }else{
            result = this.brandService.queryBrandsByPage(key, page, rows, sortBy, desc);
        }
        if (CollectionUtils.isEmpty(result.getList())){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 新增品牌
     * @param brand
     * @param cids
     */
    @PostMapping
    public ResponseEntity<Void> saveBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        this.brandService.saveBrand(brand, cids);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    /**
     * 修改品牌
     * @param brand
     * @param cids
     */
    @PutMapping
    public ResponseEntity<Void> editBrand(Brand brand, @RequestParam("cids") List<Long> cids){
        if(this.brandService.updateBrand(brand,cids)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * 删除品牌
     * @param bid
     * @return
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteBrand(Long bid){
        if(this.brandService.deleteBrand(bid)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("cid/{cid}")
    public ResponseEntity<List<Brand>> queryBrandsByCid(@PathVariable("cid")Long cid){
        List<Brand> brands = this.brandService.queryBrandsByCid(cid);
        if(CollectionUtils.isEmpty(brands)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brands);
    }

    @GetMapping("{id}")
    public ResponseEntity<Brand> queryBrandById(@PathVariable("id") Long id){
        Brand brand = this.brandService.queryBrandById(id);
        if(brand == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(brand);
    }
}

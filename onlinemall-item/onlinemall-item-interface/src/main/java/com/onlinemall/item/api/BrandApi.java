package com.onlinemall.item.api;

import com.onlinemall.item.pojo.Brand;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("brand")
public interface BrandApi {

    @GetMapping("{id}")
    Brand queryBrandById(@PathVariable(value = "id") Long id);
}
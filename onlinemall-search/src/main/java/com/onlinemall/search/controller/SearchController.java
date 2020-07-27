package com.onlinemall.search.controller;

import com.github.pagehelper.PageInfo;
import com.onlinemall.search.pojo.Goods;
import com.onlinemall.search.pojo.SearchRequest;
import com.onlinemall.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 搜索商品
     *
     * @param request
     * @return
     */
    @PostMapping("page")
    public ResponseEntity<PageInfo<Goods>> search(@RequestBody SearchRequest request) {
        PageInfo<Goods> result = this.searchService.search(request);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(result);
    }
}

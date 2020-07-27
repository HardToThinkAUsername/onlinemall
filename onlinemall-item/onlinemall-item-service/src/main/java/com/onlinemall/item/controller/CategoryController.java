package com.onlinemall.item.controller;

import com.onlinemall.item.pojo.Category;
import com.onlinemall.item.service.impl.CategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("category")
public class CategoryController {

    @Autowired
    private CategoryServiceImpl categoryService;


    /**
     * 通过品牌id查询商品分类
     * @param bid
     * @return
     */
    @GetMapping("bid/{bid}")
    public ResponseEntity<List<Category>> queryByBrandId(@PathVariable("bid")Long bid){
        List<Category> list = this.categoryService.queryByBid(bid);
        if(list == null || list.size() < 1){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }

    /**
     * 根据父id查询子节点
     * @param pid
     */
    @GetMapping("list")
    public ResponseEntity<List<Category>> queryCategoriesByPid(@RequestParam(value = "pid", defaultValue = "0") Long pid) {

        if (pid == null || pid.longValue() < 0) {
            // 响应400，相当于ResponseEntity.status(MyHttpStatus.BAD_REQUEST).build();
            return ResponseEntity.badRequest().build();
        }
        List<Category> categories = this.categoryService.queryCategoryByPid(pid);
        if (CollectionUtils.isEmpty(categories)) {
            // 响应404
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(categories);
    }

    /**
     * 添加分类
     * @param name
     * @param parentId
     * @param isParent
     * @param sort
     * @return
     */
    @PostMapping
    public ResponseEntity<Category> addCategory(@RequestParam(value = "name")String name,
                                            @RequestParam(value = "parentId")Long parentId,
                                            @RequestParam(value = "isParent")Boolean isParent,
                                            @RequestParam(value = "sort")Integer sort){
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        category.setIsParent(isParent);
        category.setSort(sort);

        if(this.categoryService.addCategory(category)){
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    /**
     * 修改分类
     * @param id
     * @param name
     * @return
     */
    @PutMapping
    public ResponseEntity<Void> editCategory(@RequestParam(value = "id")Long id,
                                             @RequestParam(value = "name")String name){
        System.out.println(id);
        System.out.println(name);
        if(this.categoryService.editCateGory(id,name)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }


    @DeleteMapping
    public ResponseEntity<Void> deleteCategory(@RequestParam(value = "id")Long id){
        if(this.categoryService.deleteCategory(id)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).build();
    }

    @GetMapping("names")
    public ResponseEntity<List<String>> queryNamesByIds(@RequestParam("ids")List<Long> ids){

        List<String> names = this.categoryService.queryNamesByIds(ids);
        if (CollectionUtils.isEmpty(names)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(names);
    }

    /**
     * 根据3级分类id，查询1~3级的分类
     * @param id
     * @return
     */
    @GetMapping("all/level")
    public ResponseEntity<List<Category>> queryAllByCid3(@RequestParam("id") Long id){
        List<Category> list = this.categoryService.queryAllByCid3(id);
        if (list == null || list.size() < 1) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }
}

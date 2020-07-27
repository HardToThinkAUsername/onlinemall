package com.onlinemall.item.controller;

import com.onlinemall.item.pojo.MyHttpStatus;
import com.onlinemall.item.pojo.SpecGroup;
import com.onlinemall.item.pojo.SpecParam;
import com.onlinemall.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("spec")
public class SpecificationController {
    @Autowired
    private SpecificationService specificationService;


    /**
     * 根据分类id查询分组
     * @param cid
     * @return
     */
    @GetMapping("groups/{cid}")
    public ResponseEntity<List<SpecGroup>> queryGroupsByCid(@PathVariable("cid")Long cid){
        List<SpecGroup> groups = this.specificationService.queryGroupsByCid(cid);
        if(CollectionUtils.isEmpty(groups)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(groups);
    }

    /**
     * 添加规格参数分组(模板)
     * @param specGroup
     * @return
     */
    @PostMapping("group")
    public ResponseEntity<Void> addGroup(SpecGroup specGroup){
        System.out.println(specGroup);
        if(specGroup == null){
            return ResponseEntity.badRequest().build();
        }
        if(this.specificationService.addGroup(specGroup)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 修改规格参数分组(模板)
     * @param specGroup
     * @return
     */
    @PutMapping("group")
    public ResponseEntity<Void> updateGroup(SpecGroup specGroup){
        System.out.println(specGroup);
        if(specGroup == null){
            return ResponseEntity.badRequest().build();
        }
        if(this.specificationService.updateGroup(specGroup)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    @DeleteMapping("group/{gid}")
    public ResponseEntity<Void> deleteGroup(@PathVariable("gid")Long gid){
        if(this.specificationService.deleteGroupById(gid)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 根据分组id查询规格参数
     * @param gid
     * @return
     */
    @GetMapping("params")
    public ResponseEntity<List<SpecParam>> queryParams(
            @RequestParam(value = "gid",required = false)Long gid,
            @RequestParam(value = "cid", required = false)Long cid,
            @RequestParam(value = "generic", required = false)Boolean generic,
            @RequestParam(value = "searching", required = false)Boolean searching){
        List<SpecParam> params = this.specificationService.queryParams(gid, cid, generic, searching);
        if(CollectionUtils.isEmpty(params)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(params);
    }

    /**
     * 添加规格参数
     * @param specParam
     * @return
     */
    @PostMapping("param")
    public ResponseEntity<Void> addParam(SpecParam specParam){
        if(specParam == null){
            return ResponseEntity.badRequest().build();
        }
        if(this.specificationService.addParam(specParam)){
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 修改规格参数
     * @param specParam
     * @return
     */
    @PutMapping("param")
    public ResponseEntity<Void> updateParam(SpecParam specParam){
        if(specParam == null){
            return ResponseEntity.badRequest().build();
        }
        if(this.specificationService.updateParam(specParam)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    /**
     * 删除规格参数
     * @param pid
     * @return
     */
    @DeleteMapping("param/{pid}")
    public ResponseEntity<Void> deleteParam(@PathVariable("pid")Long pid){
        if(this.specificationService.deleteParamByPid(pid)){
            return ResponseEntity.ok().build();
        }
        return MyHttpStatus.INTERNAL_SERVER_ERROR;
    }

    @GetMapping("{cid}")
    public ResponseEntity<List<SpecGroup>> querySpecsByCid(@PathVariable("cid") Long cid){
        List<SpecGroup> list = this.specificationService.querySpecsByCid(cid);
        if(list == null || list.size() == 0){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(list);
    }



}

package com.onlinemall.item.service.impl;

import com.onlinemall.item.mapper.SpecGroupMapper;
import com.onlinemall.item.mapper.SpecParamMapper;
import com.onlinemall.item.pojo.SpecGroup;
import com.onlinemall.item.pojo.SpecGroupExample;
import com.onlinemall.item.pojo.SpecParam;
import com.onlinemall.item.pojo.SpecParamExample;
import com.onlinemall.item.service.SpecificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecificationServiceImpl implements SpecificationService {

    @Autowired
    private SpecGroupMapper groupMapper;

    @Autowired
    private SpecParamMapper paramMapper;

    @Override
    public List<SpecGroup> queryGroupsByCid(Long cid) {
        SpecGroupExample example = new SpecGroupExample();
        SpecGroupExample.Criteria criteria = example.createCriteria();
        criteria.andCidEqualTo(cid);
        List<SpecGroup> specGroups = this.groupMapper.selectByExample(example);
        specGroups.forEach(specGroup -> {
            specGroup.setParamList(this.paramMapper.selectParamsByGid(specGroup.getId()));
        });

        return specGroups;
    }

    @Override
    public List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching) {
        if(gid != null){
            SpecParamExample example = new SpecParamExample();
            SpecParamExample.Criteria criteria = example.createCriteria();
            criteria.andGroupIdEqualTo(gid);
            return this.paramMapper.selectByExample(example);
        }else{
            return this.paramMapper.selectByCid(cid, generic, searching);
        }

    }

    @Override
    public boolean addParam(SpecParam specParam) {
        return this.paramMapper.insert(specParam) > 0;
    }

    @Override
    public boolean updateParam(SpecParam specParam) {
        return this.paramMapper.updateByPrimaryKeySelective(specParam) > 0;
    }

    @Override
    public boolean deleteParamByPid(Long pid) {
        return this.paramMapper.deleteByPrimaryKey(pid) > 0;
    }

    @Override
    public boolean addGroup(SpecGroup specGroup) {
        return this.groupMapper.insert(specGroup) > 0;
    }

    @Override
    public boolean updateGroup(SpecGroup specGroup) {
        return this.groupMapper.updateByPrimaryKeySelective(specGroup) > 0;
    }

    @Override
    public boolean deleteGroupById(Long gid) {
        return this.groupMapper.deleteByPrimaryKey(gid) > 0;
    }

    public List<SpecGroup> querySpecsByCid(Long cid) {
        // 查询规格组
        List<SpecGroup> groups = this.queryGroupsByCid(cid);
        groups.forEach(g -> {
            // 查询组内参数
            g.setParamList(this.queryParams(g.getId(), null, null, null));
        });
        return groups;
    }

}

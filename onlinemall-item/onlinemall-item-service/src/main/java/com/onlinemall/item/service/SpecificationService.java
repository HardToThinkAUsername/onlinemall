package com.onlinemall.item.service;

import com.onlinemall.item.pojo.SpecGroup;
import com.onlinemall.item.pojo.SpecParam;

import java.util.List;

public interface SpecificationService {
    List<SpecGroup> queryGroupsByCid(Long cid);

    List<SpecParam> queryParams(Long gid, Long cid, Boolean generic, Boolean searching);

    boolean addParam(SpecParam specParam);

    boolean updateParam(SpecParam specParam);

    boolean deleteParamByPid(Long pid);

    boolean addGroup(SpecGroup specGroup);

    boolean updateGroup(SpecGroup specGroup);

    boolean deleteGroupById(Long gid);

    List<SpecGroup> querySpecsByCid(Long cid);
}

package com.onlinemall.user.service;

import com.github.pagehelper.PageInfo;
import com.onlinemall.user.pojo.User;
import com.onlinemall.user.pojo.UserAddr;

import java.util.List;

public interface UserService {
    Boolean checkData(String data, Integer type);
    Boolean sendVerifyCode(String phone);
    Boolean register(User user, String code);

    User queryUser(String username, String password);
    Boolean addAddr(UserAddr addr);

    List<UserAddr> queryUserAddr(Long uid);

    boolean updateAddr(UserAddr userAddr);

    boolean deleteAddr(Long aid);

    boolean setAddrDef(Long aid,Long uid);

    Integer checkRole(Long uid);

    PageInfo<User> queryUserAll(Integer page,Integer rows,String search);

    boolean deleteUser(Long uid);
}

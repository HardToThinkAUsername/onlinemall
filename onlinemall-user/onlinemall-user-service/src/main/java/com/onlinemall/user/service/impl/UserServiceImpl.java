package com.onlinemall.user.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.onlinemall.common.utils.NumberUtils;
import com.onlinemall.user.mapper.UserAddrMapper;
import com.onlinemall.user.mapper.UserMapper;
import com.onlinemall.user.pojo.User;
import com.onlinemall.user.pojo.UserAddr;
import com.onlinemall.user.pojo.UserAddrExample;
import com.onlinemall.user.pojo.UserExample;
import com.onlinemall.user.service.UserService;
import com.onlinemall.user.utils.CodecUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserAddrMapper userAddrMapper;

    @Override
    public Boolean checkData(String data, Integer type) {
        System.out.println(data);
        System.out.println(type);
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        switch (type){
            case 1:
                criteria.andUsernameEqualTo(data);
                break;
            case 2:
                criteria.andPhoneEqualTo(data);
                break;
            default:
                return null;
        }
        return this.userMapper.countByExample(example) == 0;
    }

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private AmqpTemplate amqpTemplate;

    static final String KEY_PREFIX = "user:code:phone:";

    static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public Boolean sendVerifyCode(String phone) {
        // 生成验证码
        String code = NumberUtils.generateCode(6);
        try {
            // 发送短信
            Map<String, String> msg = new HashMap<>();
            msg.put("phone", phone);
            msg.put("code", code);
            this.amqpTemplate.convertAndSend("onlinemall.sms.exchange", "sms.verify.code", msg);
            System.out.println(1324564654);
            // 将code存入redis
            this.redisTemplate.opsForValue().set(KEY_PREFIX + phone, code, 5, TimeUnit.MINUTES);
            System.out.println(111111111);
            return true;
        } catch (Exception e) {
            logger.error("发送短信失败。phone：{}， code：{}", phone, code);
            return false;
        }
    }

    public Boolean register(User user, String code) {
        // 校验短信验证码
        String cacheCode = this.redisTemplate.opsForValue().get(KEY_PREFIX + user.getPhone());
        if (!StringUtils.equals(code, cacheCode)) {
            return false;
        }

        // 生成盐
        String salt = CodecUtils.generateSalt();
        user.setSalt(salt);

        // 对密码加密
        user.setPassword(CodecUtils.md5Hex(user.getPassword(), salt));

        // 强制设置不能指定的参数为null
        user.setId(null);
        user.setCreated(new Date());
        // 添加到数据库
        boolean b = this.userMapper.insertSelective(user) == 1;

        if(b){
            // 注册成功，删除redis中的记录
            this.redisTemplate.delete(KEY_PREFIX + user.getPhone());
        }
        return b;
    }

    public User queryUser(String username, String password) {
        //查询
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        criteria.andUsernameEqualTo(username);
        List<User> users = this.userMapper.selectByExample(example);
        // 校验用户名
        if (users == null) {
            return null;
        }
        if(users.size() <= 0){
            return null;
        }
        // 校验密码
        if (!users.get(0).getPassword().equals(CodecUtils.md5Hex(password, users.get(0).getSalt()))) {
            return null;
        }

        // 用户名密码都正确
        return users.get(0);
    }

    @Override
    public Boolean addAddr(UserAddr addr) {
        if(addr == null){
            return false;
        }
        return this.userAddrMapper.insertSelective(addr) > 0;
    }

    @Override
    public List<UserAddr> queryUserAddr(Long uid) {
        if(uid == null){
            return null;
        }
        UserAddrExample example = new UserAddrExample();
        UserAddrExample.Criteria criteria = example.createCriteria();
        criteria.andUidEqualTo(uid);
        return this.userAddrMapper.selectByExample(example);
    }

    @Override
    public boolean updateAddr(UserAddr userAddr) {
        if(userAddr == null){
            return false;
        }
        return this.userAddrMapper.updateByPrimaryKey(userAddr) > 0;
    }

    @Override
    public boolean deleteAddr(Long aid) {
        if(aid == null){
            return false;
        }
        return this.userAddrMapper.deleteByPrimaryKey(aid) > 0;
    }

    @Override
    @Transactional
    public boolean setAddrDef(Long aid, Long uid) {
        if(aid == null || uid == null){
            return false;
        }
        //先把该用户的所有收件人信息都置为非默认
        this.userAddrMapper.updateAllAddrUnDef(uid);
        //把当前地址置为默认
        UserAddr userAddr = new UserAddr();
        userAddr.setId(aid);
        userAddr.setDef(true);
        return this.userAddrMapper.updateByPrimaryKeySelective(userAddr) > 0;
    }

    @Override
    public Integer checkRole(Long uid) {
        if(uid == null){
            return null;
        }
        return this.userMapper.getRoleByUid(uid);
    }

    @Override
    public PageInfo<User> queryUserAll(Integer page,Integer rows,String search) {
        PageHelper.startPage(page,rows);
        List<User> users = this.userMapper.selectByExample(null);
        return new PageInfo<>(users);
    }

    @Override
    public boolean deleteUser(Long uid) {
        if(uid == null){
            return false;
        }
        return this.userMapper.deleteByPrimaryKey(uid) > 0;
    }
}


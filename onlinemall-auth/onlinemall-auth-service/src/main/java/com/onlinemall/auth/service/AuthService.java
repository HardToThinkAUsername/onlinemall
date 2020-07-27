package com.onlinemall.auth.service;

import com.onlinemall.auth.client.UserClient;
import com.onlinemall.auth.config.JwtProperties;
import com.onlinemall.auth.entity.UserInfo;
import com.onlinemall.auth.utils.JwtUtils;
import com.onlinemall.user.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private JwtProperties properties;

    public String authentication(String username, String password) {
        System.out.println(username);
        System.out.println(password);

        try {
            // 调用微服务，执行查询
            User user = this.userClient.queryUser(username, password);
            System.out.println(user);

            // 如果查询结果为null，则直接返回null
            if (user == null) {
                return null;
            }

            // 如果有查询结果，则生成token
            String token = JwtUtils.generateToken(new UserInfo(user.getId(), user.getUsername()),
                    properties.getPrivateKey(), properties.getExpire());
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

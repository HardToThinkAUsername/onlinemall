package com.onlinemall.auth.test;

import com.onlinemall.auth.entity.UserInfo;
import com.onlinemall.auth.utils.JwtUtils;
import com.onlinemall.auth.utils.RsaUtils;
import org.junit.Before;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

public class JwtTest {
    private static final String pubKeyPath = "C:\\my\\Code\\projects\\WebProjects\\308shop\\resources\\rsa\\rsa.pub";

    private static final String priKeyPath = "C:\\my\\Code\\projects\\WebProjects\\308shop\\resources\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU4ODgzNDQ5NH0.KjUvPOZg6UKazRW_LTUA5Y_O4PSs8FTH2Nb_uw63-GTUnjBWRqIpD-UGQfT8ymm62ok7oSgaILEw1RsItOGt4PKzujnAWlb-VQc3UXi_q7Zpp0n3yh-Ef4lXmnO--d75dQp7qm6PXVKcqHQi0fgELtx3arMH3Bijl6u9EW6L_Tc";

        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}

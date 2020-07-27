package com.onlinemall.user.controller;

import com.github.pagehelper.PageInfo;
import com.onlinemall.user.pojo.User;
import com.onlinemall.user.pojo.UserAddr;
import com.onlinemall.user.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    /**
     * 校验数据是否可用
     * @param data
     * @param type
     * @return
     */
    @GetMapping("check/{data}/{type}")
    public ResponseEntity<Boolean> checkUserData(
            @PathVariable("data")String data,
            @PathVariable("type")Integer type){
        Boolean b = this.userService.checkData(data,type);
        if(b == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(b);
    }

    /**
     * 发送手机验证码
     * @param phone
     * @return
     */
    @PostMapping("code")
    public ResponseEntity<Void> sendVerifyCode(String phone) {
        Boolean boo = this.userService.sendVerifyCode(phone);
        if (boo == null || !boo) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 注册
     * @param user
     * @param code
     * @return
     */
    @PostMapping("register")
    public ResponseEntity<Void> register(@Valid User user, @RequestParam("code") String code) {

        Boolean boo = this.userService.register(user, code);
        if (boo == null || !boo) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("list")
    public ResponseEntity<PageInfo<User>> queryUserAll(@RequestParam(value = "page",defaultValue = "1")Integer page,
                                                       @RequestParam(value = "rows",defaultValue = "5")Integer rows,
                                                       @RequestParam(value = "search",defaultValue = "")String search){
        PageInfo<User> pageInfo = this.userService.queryUserAll(page,rows,search);
        if(pageInfo == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(pageInfo);
    }

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    @GetMapping("query")
    public ResponseEntity<User> queryUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password
    ) {
        User user = this.userService.queryUser(username, password);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(user);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam("uid")Long uid){
        if(this.userService.deleteUser(uid)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("addr")
    public ResponseEntity<Void> addAddr(@Valid UserAddr addr){
        if(this.userService.addAddr(addr)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    @GetMapping("addr")
    public ResponseEntity<List<UserAddr>> queryAddr(Long uid){
        List<UserAddr> addrs = this.userService.queryUserAddr(uid);
        if(addrs == null ){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.ok(addrs);
    }

    @PutMapping("addr")
    public ResponseEntity<Void> updateAddr(@Valid UserAddr userAddr){
        if(this.userService.updateAddr(userAddr)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("addr")
    public ResponseEntity<Void> deleteAddr(@RequestParam("aid")Long aid){
        if(this.userService.deleteAddr(aid)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PutMapping("setAddrDef")
    public ResponseEntity<Void> setAddrDef(@RequestParam("aid")Long aid, @RequestParam("uid")Long uid){
        if(this.userService.setAddrDef(aid,uid)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("checkRole")
    public ResponseEntity<Integer> checkRole(@RequestParam("uid")Long uid){
        Integer userRole = this.userService.checkRole(uid);
        if(userRole == null){
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userRole);
    }



}

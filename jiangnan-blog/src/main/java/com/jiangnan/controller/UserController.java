package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.User;
import com.jiangnan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/userInfo")
    public ResponseResult userInfo() {

        return userService.userInfo();
    }

    /**
     * 更新个人信息
     * @param user
     * @return
     */
    @PutMapping("/userInfo")
    public ResponseResult updateUserInfo(@RequestBody User user) {
        return userService.updateUserInfo(user);
    }


    /**
     * 注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public ResponseResult register(@RequestBody User user) {
        return userService.register(user);
    }
}

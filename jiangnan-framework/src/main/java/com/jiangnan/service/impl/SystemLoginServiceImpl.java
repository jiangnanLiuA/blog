package com.jiangnan.service.impl;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.LoginUser;
import com.jiangnan.domain.entity.User;
import com.jiangnan.domain.vo.BlogUserLoginVo;
import com.jiangnan.domain.vo.UserInfoVo;
import com.jiangnan.service.BlogLoginService;
import com.jiangnan.service.LoginService;
import com.jiangnan.utils.BeanCopyUtils;
import com.jiangnan.utils.JwtUtil;
import com.jiangnan.utils.RedisCache;
import com.jiangnan.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Objects;

@Service
public class SystemLoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Override
    public ResponseResult login(User user) {

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);

        // 判断是否认证通过
        if (Objects.isNull(authenticate)) {
            throw new RuntimeException("用户名或密码错误");
        }
        //获取 userid  生成token
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        String userId = loginUser.getUser().getId() + "";
        //token
        String jwt = JwtUtil.createJWT(userId);

        //把用户信息存入redis
        redisCache.setCacheObject("login:" + userId, loginUser);

        //把token和userinfo封装 返回
        HashMap<String, String> map = new HashMap<>();
        map.put("token", jwt);

        return ResponseResult.okResult(map);
    }


    @Override
    public ResponseResult logout() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();
        //删除redis中的值
        redisCache.deleteObject("login:" + userId);
        return ResponseResult.okResult();
    }

//    @Override
//    public ResponseResult logout() {
//        //获取token  解析获取userId
//        //从之前配置的过滤器中获取
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        //获取属性
//        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//
//        //获取userId
//        Long userId = loginUser.getUser().getId();
//        //删除redis中的用户信息
//        redisCache.deleteObject("bloglogin:" + userId);
//        return ResponseResult.okResult();
//    }
}

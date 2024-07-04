package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.entity.LoginUser;
import com.jiangnan.domain.entity.User;
import com.jiangnan.mapper.MenuMapper;
import com.jiangnan.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MenuMapper menuMapper;


//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        //根据用户名查询用户信息
//        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<User>();
//        lambdaQueryWrapper.eq(User::getUserName, username);
//        User user = userMapper.selectOne(lambdaQueryWrapper);
//        //判断是否查到用户　若没查到，给出异常
//        if (Objects.isNull(user)) {
//            throw new RuntimeException("用户不存在");
//        }
//        //返回用户信息
//        //TODO 查询权限信息封装
//        return new LoginUser(user);
//    }

    @Override
    //在这里之前，我们已经拿到了登录的用户名和密码。UserDetails是SpringSecurity官方提供的接口
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //根据拿到的用户名，并结合查询条件(数据库是否有这个用户名)，去查询用户信息
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, username);
        User user = userMapper.selectOne(queryWrapper);

        //判断是否查询到用户，也就是这个用户是否存在，如果没查到就抛出异常
        if (Objects.isNull(user)) {
            throw new RuntimeException("用户不存在");//后期会对异常进行统一处理
        }

        // 如果是后台用户，才需要查询权限，也就是只对后台用户做权限校验
        if (user.getType().equals(SystemConstants.IS_ADMIN)) {
            //根据用户id查询权限关键字，即list是权限信息的集合
            List<String> list = menuMapper.selectPermsByUserId(user.getId());
            return new LoginUser(user, list);
        }

        // 如果不是后台用户，就只封装用户信息，不封装权限信息
        //返回查询到的用户信息。注意下面那行直接返回user会报错，我们需要在huanf-framework工程的domain目录新
        //建LoginUser类，在LoginUser类实现UserDetails接口，然后下面那行就返回LoginUser对象
        return new LoginUser(user, null);
    }
}

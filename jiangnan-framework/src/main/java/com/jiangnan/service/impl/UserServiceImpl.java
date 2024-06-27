package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.User;
import com.jiangnan.domain.vo.UserInfoVo;
import com.jiangnan.enums.AppHttpCodeEnum;
import com.jiangnan.exception.SystemException;
import com.jiangnan.mapper.UserMapper;
import com.jiangnan.service.UserService;
import com.jiangnan.utils.BeanCopyUtils;
import com.jiangnan.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-06-26 20:08:02
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public ResponseResult userInfo() {
        //封装类
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //getById 为 mp 提供的方法
        //根据用户id查询用户信息
        User user = getById(userId);

        //封装成UserInfoVo
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        //返回
        return ResponseResult.okResult(userInfoVo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }


    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 注册
     *
     * @param user
     * @return
     */
    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对数据进行是否存在判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }

        //对密码加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUserName, userName);

        return count(lambdaQueryWrapper) > 0;
    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getNickName, nickName);

        return count(lambdaQueryWrapper) > 0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getEmail, email);

        return count(lambdaQueryWrapper) > 0;
    }
}


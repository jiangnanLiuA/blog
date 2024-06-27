package com.jiangnan.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.User;

/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-06-26 20:08:00
 */
public interface UserService extends IService<User> {
    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);
}


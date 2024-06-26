package com.jiangnan.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.domain.entity.User;
import com.jiangnan.mapper.UserMapper;
import com.jiangnan.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-06-26 20:08:02
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}


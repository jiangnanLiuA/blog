package com.jiangnan.service;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.User;

public interface LoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}

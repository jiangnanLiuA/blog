package com.jiangnan.service;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.User;

public interface BlogLoginService {
    ResponseResult login(User user);

    ResponseResult logout();
}

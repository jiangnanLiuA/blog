package com.jiangnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Category;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-06-24 19:07:21
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();
}


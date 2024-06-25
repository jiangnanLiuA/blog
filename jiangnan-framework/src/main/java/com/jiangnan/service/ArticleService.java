package com.jiangnan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Article;

public interface ArticleService extends IService<Article> {
    ResponseResult hotArticleList();


    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);
}

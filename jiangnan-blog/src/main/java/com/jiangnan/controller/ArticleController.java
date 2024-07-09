package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

//    @GetMapping("/list")
//    public List<Article> test() {
//        List<Article> list = articleService.list();
//        return list;
//    }

    //测试统一响应格式
    @GetMapping("/hotArticleList")
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult 返回
        ResponseResult result = articleService.hotArticleList();
        return result;
    }

    //更新浏览量
    @PutMapping("/updateViewCount/{id}")
    public ResponseResult updateViewCount(@PathVariable("id") Long id) {
        return articleService.updateViewCount(id);
    }

    //分页查询文章的列表
    @GetMapping("/articleList")
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        ResponseResult result = articleService.articleList(pageNum, pageSize, categoryId);
        return result;
    }

    //查询文章详情
    @GetMapping("/{id}")
    public ResponseResult getArticleDetail(@PathVariable("id") Long id) {
        ResponseResult result = articleService.getArticleDetail(id);
        return result;
    }
}

package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.domain.vo.HotArticleVo;
import com.jiangnan.mapper.ArticleMapper;
import com.jiangnan.service.ArticleService;
import com.jiangnan.utils.BeanCopyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {


    @Override
    public ResponseResult hotArticleList() {
        //查询热门文章 封装成ResponseResult 返回
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //必须是正式文章
        // 修改字面值为常量  ->  lambdaQueryWrapper.eq(Article::getStatus, 0);
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        //按照浏览量降序排序
        lambdaQueryWrapper.orderByDesc(Article::getViewCount);
        //最多只查询10条
        //字面值修改 -> Page<Article> page = new Page<>(1, 10);
        Page<Article> page = new Page<>(SystemConstants.ARTICLE_STATUS_CURRENT, SystemConstants.ARTICLE_STATUS_SIZE);
        page(page, lambdaQueryWrapper);

        //page对象封装为result输 -> 获取最终查询结果
        List<Article> articles = page.getRecords();


        //bean 拷贝  -> 需要的字段使用HotArticleVo进行展示，而不是所有的  Article ->  BeanUtils.copyProperties
//        List<HotArticleVo> articleVos = new ArrayList<>();
        //bean拷贝原理 -> 依靠字段名与字段类型一致才可以拷贝
//        for (Article article : articles) {
//            HotArticleVo vo = new HotArticleVo();
//            BeanUtils.copyProperties(article, vo);
//            articleVos.add(vo);
//        }

        List<HotArticleVo> vs = BeanCopyUtils.copyBeanList(articles, HotArticleVo.class);

        //静态方法
        return ResponseResult.okResult(vs);
    }
}

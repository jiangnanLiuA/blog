package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.dto.AddArticleDto;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.domain.entity.ArticleTag;
import com.jiangnan.domain.entity.Category;
import com.jiangnan.domain.vo.*;
import com.jiangnan.mapper.ArticleMapper;
import com.jiangnan.service.ArticleService;
import com.jiangnan.service.ArticleTagService;
import com.jiangnan.service.CategoryService;
import com.jiangnan.utils.BeanCopyUtils;
import com.jiangnan.utils.RedisCache;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {


    @Autowired
    private CategoryService categoryService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleTagService articleTagService;

    @Autowired
    private ArticleService articleService;


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

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        // 查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 是否有 categoryId， 有的话需保证 查询 时和 传入 的一致
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        // 状态为正式已发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序排序 -> 为了保证置顶的文章在最上面
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);//.orderByDesc(Article::getViewCount);
        // 分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        // mp 的 page方法 -> 分页查询
        page(page, lambdaQueryWrapper);

        // 查询categoryName
        List<Article> articles = page.getRecords();
        //articleId去查询articleName进行设置

        //传统for循环解决
//        for (Article article : articles) {
//            Long id = article.getCategoryId();
//            Category category = categoryService.getById(id);
//            article.setCategoryName(category.getName());
//        }

        //stream解决
        /**
         * //获取分类id，查询分类信息，获取分类名称
         * //                        Long id = article.getCategoryId();
         * //                        Category category = categoryService.getById(id);
         * //                        article.setCategoryName(category.getName());
         *                         //article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
         *                         //把分类名称设置给article
         */


        /**
         * articles.stream()
         *      .map(new Function<Article, Article>() {
         *                          @Override
         *                          public Article apply(Article article) {
         *                              return article.setCategoryName(categoryService.getById(article.getCategoryId()).getName());
         *                          }
         *                      })
         */
//        articles = articles.stream()
//                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName()))
//                .collect(Collectors.toList());


        // 封装查询结果 -> vo
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        // 匹配前端格式
        PageVo pageVo = new PageVo(articleListVos, page.getTotal());

        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        //根据id查询文章
        Article article = getById(id);

        //从redis中获取viewCount
        Integer viewCount = redisCache.getCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString());
        article.setViewCount(viewCount.longValue());

        //转换成vo
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        //根据分类id查询分类名
//        Category category = categoryService.getById(id);
//        articleDetailVo.setCategoryName(category.getName());
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        // 若存在
        if (category != null) {
            articleDetailVo.setCategoryName(category.getName());
        }

        //封装响应返回
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {

        //更新 redis 中对应id的浏览量
        redisCache.incrementCacheMapValue(SystemConstants.ARTICLE_VIEW_COUNT, id.toString(), 1);
        return ResponseResult.okResult();
    }




    /**
     * 增加博客文章
     *
     * @param articleDto
     * @return
     */
    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        articleService.save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }
}

package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.domain.entity.Category;
import com.jiangnan.domain.vo.CategoryVo;
import com.jiangnan.domain.vo.PageVo;
import com.jiangnan.mapper.CategoryMapper;
import com.jiangnan.service.ArticleService;
import com.jiangnan.service.CategoryService;
import com.jiangnan.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 分类表(Category)表服务实现类
 *
 * @author makejava
 * @since 2024-06-24 19:11:01
 */
@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private ArticleService articleService;

    @Override
    public ResponseResult getCategoryList() {
        //首先查询文章表，状态为已发布的文章 -> 标记为0的查询
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // list() 方法 为 mp 提供的，在这里不能直接使用 list的原因是 查询的是 文章表，而目前service方法是 分类表  -> 因此需要注入  articleService 进行使用
        // articleService.list()

        List<Article> articleList = articleService.list(lambdaQueryWrapper);
//        for (Article articles : articleList) {
//            articles.getCategoryId();
//        }

        //获取文章的分类id，并去重
        List<Long> categoryIds = articleList.stream()
                .map(article -> article.getCategoryId())
                .distinct()
                .collect(Collectors.toList());// 在这里也可以直接 Collectors.toSet()  转为Set集合，达到去重的效果

        //查询分类表
        List<Category> categories = listByIds(categoryIds);//  这里的 listByIds() 方法则是 分类表，可以直接使用，而不需要 注入 articleService

        //保证必须是 正常状态的分类  即 状态值为 0
        List<Category> categoryList = categories.stream()
                .filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus()))
                .collect(Collectors.toList());
        //封装vo
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categoryList, CategoryVo.class);

        return ResponseResult.okResult(categoryVos);
    }

    /**
     * 写博客-查询文章分类的接口
     *
     * @return
     */
    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(lambdaQueryWrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    /**
     * 分页查询分类列表
     *
     * @param category
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public PageVo selectCategoryPage(Category category, Integer pageNum, Integer pageSize) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();

        queryWrapper.like(StringUtils.hasText(category.getName()), Category::getName, category.getName());
        queryWrapper.eq(Objects.nonNull(category.getStatus()), Category::getStatus, category.getStatus());

        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, queryWrapper);

        //转换成Vo
        List<Category> categories = page.getRecords();

        PageVo pageVo = new PageVo();
        pageVo.setTotal(page.getTotal());
        pageVo.setRows(categories);
        return pageVo;
    }
}


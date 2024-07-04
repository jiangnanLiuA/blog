package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.dto.AddArticleDto;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.domain.vo.ArticleByIdVo;
import com.jiangnan.domain.vo.PageVo;
import com.jiangnan.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    /**
     * 新增博客文章
     *
     * @param article
     * @return
     */
    @PostMapping
    @PreAuthorize("@ps.hasPermission('content:article:writer')")//权限控制
    public ResponseResult add(@RequestBody AddArticleDto article) {
        return articleService.add(article);
    }

    /**
     * 分页查询博客文章
     *
     * @param article
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Article article, Integer pageNum, Integer pageSize) {
        PageVo pageVo = articleService.selectArticlePage(article, pageNum, pageSize);
        return ResponseResult.okResult(pageVo);
    }

    /**
     * 根据文章id来修改文章
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/{id}")
    //1.先根据文章id查询对应的文章
    public ResponseResult getInfo(@PathVariable(value = "id") Long id) {
        ArticleByIdVo article = articleService.getInfo(id);
        return ResponseResult.okResult(article);
    }

    /**
     * 2.然后才是修改文章
     *
     * @param article
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody AddArticleDto article) {
        articleService.edit(article);
        return ResponseResult.okResult();
    }

    /**
     * 根据文章id来删除文章
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResponseResult delete(@PathVariable Long id) {
        //直接使用mybatisPlus提供的removeById方法
        articleService.removeById(id);
        return ResponseResult.okResult();
    }
}

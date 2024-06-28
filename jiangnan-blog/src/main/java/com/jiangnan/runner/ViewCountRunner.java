package com.jiangnan.runner;

import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.mapper.ArticleMapper;
import com.jiangnan.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class ViewCountRunner implements CommandLineRunner {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private RedisCache redisCache;

    @Override
    public void run(String... args) throws Exception {

        //查询博客信息 id  viewCount/
        //stream流的方式，将 id -> key, value -> viewCount 存入redis中
        List<Article> articles = articleMapper.selectList(null);
        Map<String, Integer> viewCountMap = articles.stream()
                .collect(Collectors.toMap(article -> article.getId().toString(), article -> {
                    return article.getViewCount().intValue();//浏览量要增加操作，因此不能直接存储为 Long 类型，而改存 Integer 类型
                }));
        //存储到 redis
        redisCache.setCacheMap(SystemConstants.ARTICLE_VIEW_COUNT, viewCountMap);
    }
}

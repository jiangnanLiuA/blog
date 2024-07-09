package com.jiangnan.job;

import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.entity.Article;
import com.jiangnan.service.ArticleService;
import com.jiangnan.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 定时更新浏览量数据到 数据库
 */
@Component
public class UpdateViewCountJob {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ArticleService articleService;

    @Scheduled(cron = "0 0 1 * * ?")
    public void updateViewCount() {

        //获取 redis 中的浏览量
        Map<String, Integer> viewCountMap = redisCache.getCacheMap(SystemConstants.ARTICLE_VIEW_COUNT);
        //双列集合不能直接操作，因此首先利用 entrySet 或者 keySet 拿到单列集合
        //viewCountMap.entrySet() 拿到单列集合
        List<Article> articles = viewCountMap.entrySet()
                .stream()
                .map(new Function<Map.Entry<String, Integer>, Article>() {
                    @Override
                    public Article apply(Map.Entry<String, Integer> entry) {
                        return new Article(Long.valueOf(entry.getKey()), entry.getValue().longValue());
                    }
                })
                .collect(Collectors.toList());

        //批量更新到数据库中 ，调用mp的updateBatchById()方法发现 需要的参数是list集合，因此操作上述redis中的map，利用stream流的方式实现
        articleService.updateBatchById(articles);
    }
}

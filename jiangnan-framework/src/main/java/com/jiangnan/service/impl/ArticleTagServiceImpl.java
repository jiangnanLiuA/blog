package com.jiangnan.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.domain.entity.ArticleTag;
import com.jiangnan.mapper.ArticleTagMapper;
import com.jiangnan.service.ArticleTagService;
import org.springframework.stereotype.Service;
/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2024-07-02 22:34:36
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {
    
}


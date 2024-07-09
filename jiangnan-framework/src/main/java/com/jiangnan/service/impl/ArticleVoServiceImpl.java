package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.domain.vo.ArticleVo;
import com.jiangnan.mapper.ArticleVoMapper;
import com.jiangnan.service.ArticleVoService;
import org.springframework.stereotype.Service;

@Service
public class ArticleVoServiceImpl extends ServiceImpl<ArticleVoMapper, ArticleVo> implements ArticleVoService {

}

package com.jiangnan.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Comment;

/**
 * 评论表(Comment)表服务接口
 *
 * @author makejava
 * @since 2024-06-26 17:15:27
 */
public interface CommentService extends IService<Comment> {
    ResponseResult commentList(String commentType, Long articleId, Integer pageNum, Integer pageSize);

    ResponseResult addComment(Comment comment);
}


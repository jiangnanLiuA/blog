package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Comment;
import com.jiangnan.domain.entity.User;
import com.jiangnan.domain.vo.CommentVo;
import com.jiangnan.domain.vo.PageVo;
import com.jiangnan.mapper.CommentMapper;
import com.jiangnan.service.CommentService;
import com.jiangnan.service.UserService;
import com.jiangnan.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 评论表(Comment)表服务实现类
 *
 * @author makejava
 * @since 2024-06-26 17:15:28
 */
@Service("commentService")
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private UserService userService;

    @Override
    public ResponseResult commentList(Long articleId, Integer pageNum, Integer pageSize) {
        //查询对应文章的根评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //对应文章 -> articleId 进行判断
        lambdaQueryWrapper.eq(Comment::getArticleId, articleId);
        //根评论 rootId = -1
        lambdaQueryWrapper.eq(Comment::getRootId, SystemConstants.ROOT_COMMENT);

        //分页查询
        Page<Comment> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        //封装vo
        List<CommentVo> commentVoList = toCommentVoList(page.getRecords());

        //查询所有根评论对应的子评论集合，并且赋值给对应的属性
        for (CommentVo commentVo : commentVoList) {
            //查询对应的子评论
            List<CommentVo> children = getChildren(commentVo.getId());
            //赋值
            commentVo.setChildren(children);
        }


        return ResponseResult.okResult(new PageVo(commentVoList, page.getTotal()));
    }

    private List<CommentVo> toCommentVoList(List<Comment> list) {
        List<CommentVo> commentVos = BeanCopyUtils.copyBeanList(list, CommentVo.class);
        //遍历 vo 集合
        for (CommentVo commentVo : commentVos) {
            //通过 createBy查询用户的昵称并赋值
            String nickName = userService.getById(commentVo.getCreateBy()).getNickName();
            commentVo.setUsername(nickName);
            //通过 toCommentUserId查询用户的昵称并赋值
            //如果 toCommentUserId不为-1 -> 即必须保证为子评论 -> 不是根评论 才进行查询
            if (commentVo.getToCommentUserId() != -1) {
                String toCommentUserName = userService.getById(commentVo.getToCommentUserId()).getNickName();
                commentVo.setToCommentUserName(toCommentUserName);
            }
        }
        return commentVos;
    }

    /**
     * 根据根评论的id查询所对应的子评论的集合
     * Comment::getRootId, id -> 查子评论
     *
     * @param id 根评论id
     * @return
     */
    private List<CommentVo> getChildren(Long id) {

        //通过rootId和当前评论一致保证为  父子评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getRootId, id);
        //按照时间排序
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        //拿到所有评论
        List<Comment> comments = list(lambdaQueryWrapper);
        //将评论封装为vo
        List<CommentVo> commentVos = toCommentVoList(comments);
        return commentVos;
    }
}


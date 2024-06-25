package com.jiangnan.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Link;

/**
 * 友链(Link)表服务接口
 *
 * @author makejava
 * @since 2024-06-25 15:55:55
 */
public interface LinkService extends IService<Link> {
    ResponseResult getAllLink();
}


package com.jiangnan.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangnan.domain.entity.Tag;
import org.apache.ibatis.annotations.Param;

/**
 * 标签(Tag)表数据库访问层
 *
 * @author makejava
 * @since 2024-06-30 16:47:00
 */
public interface TagMapper extends BaseMapper<Tag> {
    int myUpdateById(@Param("id") Long id, @Param("flag") int flag);
    //删除标签，注意是逻辑删除，所以不能使用mybatisPlus提供的，要自己使用mybatis写SQL语句
//    int myUpdateById(@Param("id") Long id, @Param("flag") int flag);
}


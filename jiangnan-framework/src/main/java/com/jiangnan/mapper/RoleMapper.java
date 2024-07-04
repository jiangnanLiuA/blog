package com.jiangnan.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiangnan.domain.entity.Role;

import java.util.List;

/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-07-01 11:29:54
 */
public interface RoleMapper extends BaseMapper<Role> {
    List<String> selectRoleKeyByUserId(Long userId);
}


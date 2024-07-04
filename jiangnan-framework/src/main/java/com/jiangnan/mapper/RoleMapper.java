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
    //查询普通用户的角色权限
    List<String> selectRoleKeyByUserId(Long userId);

    //修改用户->根据id查询用户信息
    List<Long> selectRoleIdByUserId(Long userId);
}


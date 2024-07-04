package com.jiangnan.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.domain.entity.Role;
import com.jiangnan.mapper.RoleMapper;
import com.jiangnan.service.RoleService;
import com.jiangnan.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-07-01 11:29:58
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        //判断是否是管理员 如果是 -> 返回集合中只需要有admin
        if (SecurityUtils.isAdmin()) {
            List<String> roleKeys = new ArrayList<>();
            //为什么查 admin 字段即可?
            roleKeys.add("admin");
            return roleKeys;
        }
        //否则，查询用户所具有的角色信息
        return getBaseMapper().selectRoleKeyByUserId(id);
    }
}


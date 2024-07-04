package com.jiangnan.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.domain.entity.UserRole;
import com.jiangnan.mapper.UserRoleMapper;
import com.jiangnan.service.UserRoleService;
import org.springframework.stereotype.Service;
/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-07-04 14:49:34
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}


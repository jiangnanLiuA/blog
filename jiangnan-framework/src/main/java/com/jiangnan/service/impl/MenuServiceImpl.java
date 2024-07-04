package com.jiangnan.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiangnan.constants.SystemConstants;
import com.jiangnan.domain.entity.Menu;
import com.jiangnan.domain.vo.MenuVo;
import com.jiangnan.mapper.MenuMapper;
import com.jiangnan.service.MenuService;
import com.jiangnan.utils.BeanCopyUtils;
import com.jiangnan.utils.SecurityUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-07-01 11:15:43
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {
    @Override
    public List<String> selectPermsByUserId(Long id) {
        //如果是管理员，返回所有的权限
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            //获取菜单类型为C或者F
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            //获取状态为正常的
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            //未被删除的权限
            //delFlag 配置文件中已经配置过，无需重复

            List<Menu> menuList = list(wrapper);

            List<String> perms = menuList.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        //否则 返回其所具有的权限
        List<String> perms = getBaseMapper().selectPermsByUserId(id);
        return perms;
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是不是管理员
        if (SecurityUtils.isAdmin()) {
            //如果是 返回所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        } else {
            //否则获取当前用户具有的权限
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单 然后去找他们的子菜单设置到children属性中
        //0L 表示 第一层级的父id为0
        List<Menu> menuTree = builderMenuTree(menus, 0L);
        return menuTree;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
//        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
//        List<MenuVo> menuTree = menuVos.stream()
//                .filter(menuVo -> menuVo.getParentId().equals(parentId))
//                .map(menuVo -> menuVo.setChildren(getChildren(menuVo, menuVos)))
//                .collect(Collectors.toList());

        List<Menu> menusList = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
//        List<Menu> menusList = BeanCopyUtils.copyBeanList(menuTree, Menu.class);
        return menusList;
    }

//    @Override
//    public List<Menu> selectMenuList(Menu menu) {
//
//        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//        //menuName模糊查询
//        queryWrapper.like(StringUtils.hasText(menu.getMenuName()),Menu::getMenuName,menu.getMenuName());
//        queryWrapper.eq(StringUtils.hasText(menu.getStatus()),Menu::getStatus,menu.getStatus());
//        //排序 parent_id和order_num
//        queryWrapper.orderByAsc(Menu::getParentId,Menu::getOrderNum);
//        List<Menu> menus = list(queryWrapper);;
//        return menus;
//    }
//    @Override
//    public boolean hasChild(Long menuId) {
//        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Menu::getParentId,menuId);
//        return count(queryWrapper) != 0;
//    }
//
//    @Override
//    public List<Long> selectMenuListByRoleId(Long roleId) {
//        return getBaseMapper().selectMenuListByRoleId(roleId);
//    }

    /**
     * 获取存入参数的子Menu集合
     *
     * @param
     * @param
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                //通过过滤得到子菜单
                .filter(m -> m.getParentId().equals(menu.getId()))
                //如果有三层菜单的话，也就是子菜单的子菜单，我们就用下面那行递归(自己调用自己)来处理
                .map(m -> m.setChildren(getChildren(m, menus)))//递归 如有多层
                .collect(Collectors.toList());
        return childrenList;
    }

    /**
     * 获取存入参数的子Menu集合
     *
     * @param menuVo
     * @param menuVos
     * @return
     */
//    private List<MenuVo> getChildren(MenuVo menuVo, List<MenuVo> menuVos) {
//        List<MenuVo> childrenList = menuVos.stream()
//                .filter(m -> m.getParentId().equals(menuVo.getId()))
//                .map(m -> m.setChildren(getChildren(m, menuVos)))//递归 如有多层
//                .collect(Collectors.toList());
//        return childrenList;
//    }
}


package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Menu;
import com.jiangnan.domain.vo.MenuTreeVo;
import com.jiangnan.domain.vo.MenuVo;
import com.jiangnan.domain.vo.RoleMenuTreeSelectVo;
import com.jiangnan.service.MenuService;
import com.jiangnan.utils.BeanCopyUtils;
import com.jiangnan.utils.SystemConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;

    /**
     * 查询菜单列表
     *
     * @param menu
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(Menu menu) {
        List<Menu> menus = menuService.selectMenuList(menu);
        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
        return ResponseResult.okResult(menuVos);
    }

    /**
     * 新增菜单
     *
     * @param menu
     * @return
     */

    @PostMapping
    public ResponseResult add(@RequestBody Menu menu) {
        menuService.save(menu);
        return ResponseResult.okResult();
    }

    /**
     * 修改菜单
     *
     * @param menuId
     * @return
     */
    @GetMapping(value = "/{menuId}")
    //1.先根据菜单id查询对应的权限菜单
    public ResponseResult getInfo(@PathVariable Long menuId) {
        return ResponseResult.okResult(menuService.getById(menuId));
    }

    @PutMapping
    //2.然后才是更新菜单
    public ResponseResult edit(@RequestBody Menu menu) {
        if (menu.getId().equals(menu.getParentId())) {
            return ResponseResult.errorResult(500, "修改菜单'" + menu.getMenuName() + "'失败，上级菜单不能选择自己");
        }
        menuService.updateById(menu);
        return ResponseResult.okResult();
    }

    /**
     * 删除菜单
     *
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public ResponseResult remove(@PathVariable("menuId") Long menuId) {
        if (menuService.hasChild(menuId)) {
            return ResponseResult.errorResult(500, "存在子菜单不允许删除");
        }
        menuService.removeById(menuId);
        return ResponseResult.okResult();
    }


    /**
     * 新增角色-获取菜单下拉树列表
     *
     * @return
     */
    @GetMapping("/treeselect")
    public ResponseResult treeselect() {
        //复用之前的selectMenuList方法。方法需要参数，参数可以用来进行条件查询，而这个方法不需要条件，所以直接new Menu()传入
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<MenuTreeVo> options = SystemConverter.buildMenuSelectTree(menus);
        return ResponseResult.okResult(options);
    }

    /**
     * 修改角色-根据角色id查询对应角色菜单列表树
     *
     * @param roleId
     * @return
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public ResponseResult roleMenuTreeSelect(@PathVariable("roleId") Long roleId) {
        List<Menu> menus = menuService.selectMenuList(new Menu());
        List<Long> checkedKeys = menuService.selectMenuListByRoleId(roleId);
        List<MenuTreeVo> menuTreeVos = SystemConverter.buildMenuSelectTree(menus);
        RoleMenuTreeSelectVo vo = new RoleMenuTreeSelectVo(checkedKeys, menuTreeVos);
        return ResponseResult.okResult(vo);
    }
}

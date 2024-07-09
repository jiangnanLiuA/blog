package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.LoginUser;
import com.jiangnan.domain.entity.Menu;
import com.jiangnan.domain.entity.User;
import com.jiangnan.domain.vo.AdminUserInfoVo;
import com.jiangnan.domain.vo.MenuVo;
import com.jiangnan.domain.vo.RoutersVo;
import com.jiangnan.domain.vo.UserInfoVo;
import com.jiangnan.enums.AppHttpCodeEnum;
import com.jiangnan.exception.SystemException;
import com.jiangnan.service.LoginService;
import com.jiangnan.service.MenuService;
import com.jiangnan.service.RoleService;
import com.jiangnan.utils.BeanCopyUtils;
import com.jiangnan.utils.RedisCache;
import com.jiangnan.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {

    @Autowired
    private LoginService loginService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private RedisCache redisCache;

    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }

    @GetMapping("getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo() {

        //获取当前登录的用户
        LoginUser loginUser = SecurityUtils.getLoginUser();
        //根据用户id查询权限信息
        List<String> permsList = menuService.selectPermsByUserId(loginUser.getUser().getId());

        //根据用户id查询角色信息
        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        //获取用户信息
        User user = loginUser.getUser();
        //bean拷贝
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        //封装数据返回
        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(permsList, roleKeyList, userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }

    @GetMapping("getRouters")
    public ResponseResult<RoutersVo> getRouters() {
        //获取当前登录的用户id
        Long userId = SecurityUtils.getUserId();

        //查询menu 结果是 tree 的形式
        //根据用户id来查询menu(权限菜单)。要求查询结果是tree的形式，也就是子父菜单树
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);

        //封装数据返回
//        List<MenuVo> menuVos = BeanCopyUtils.copyBeanList(menus, MenuVo.class);
//        return ResponseResult.okResult(new RoutersVo(menuVos));
        return ResponseResult.okResult(new RoutersVo(menus));
    }

    @PostMapping("/user/logout")
    public ResponseResult logout() {
        return loginService.logout();
    }
}

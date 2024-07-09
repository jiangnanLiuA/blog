package com.jiangnan.controller;

import com.jiangnan.domain.ResponseResult;
import com.jiangnan.domain.entity.Role;
import com.jiangnan.domain.entity.User;
import com.jiangnan.domain.vo.UserInfoAndRoleIdsVo;
import com.jiangnan.enums.AppHttpCodeEnum;
import com.jiangnan.exception.SystemException;
import com.jiangnan.service.RoleService;
import com.jiangnan.service.UserService;
import com.jiangnan.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 查询用户列表
     *
     * @param user
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/list")
    public ResponseResult list(User user, Integer pageNum, Integer pageSize) {
        return userService.selectUserPage(user, pageNum, pageSize);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping
    public ResponseResult add(@RequestBody User user) {
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        if (!userService.checkUserNameUnique(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (!userService.checkPhoneUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        if (!userService.checkEmailUnique(user)) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        return userService.addUser(user);
    }

    /**
     * 删除用户
     *
     * @param userIds
     * @return
     */
    @DeleteMapping("/{userIds}")
    public ResponseResult remove(@PathVariable List<Long> userIds) {
        //判断要删除的用户id和正在登录的id是否一致
        if (userIds.contains(SecurityUtils.getUserId())) {
            return ResponseResult.errorResult(500, "不能删除当前你正在使用的用户");
        }
        userService.removeByIds(userIds);
        return ResponseResult.okResult();
    }



    /**
     * 修改用户 -> 根据id查询用户信息
     *
     * @param userId
     * @return
     */
    @GetMapping(value = {"/{userId}"})
    public ResponseResult getUserInfoAndRoleIds(@PathVariable(value = "userId") Long userId) {
        List<Role> roles = roleService.selectRoleAll();
        User user = userService.getById(userId);
        //当前用户所具有的角色id列表
        List<Long> roleIds = roleService.selectRoleIdByUserId(userId);

        UserInfoAndRoleIdsVo vo = new UserInfoAndRoleIdsVo(user, roles, roleIds);
        return ResponseResult.okResult(vo);
    }

    /**
     * 修改用户 -> 更新用户信息
     *
     * @param user
     * @return
     */
    @PutMapping
    public ResponseResult edit(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseResult.okResult();
    }
}

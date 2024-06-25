package com.jiangnan.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
//@NoArgsConstructor
//@AllArgsConstructor
@Accessors(chain = true)
public class UserInfoVo {

    //主键
    private Long id;

    //昵称
    private String nickName;

    //头像
    private String avatar;

    //邮箱
    private String email;

    //性别
    private String sex;
}

package com.example.entity;

import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.time.LocalDateTime;

/**
 * 用户信息
 */
@Data
public class User {
    private static final long serialVersionUID = 1L;

    private Long id;

    //昵称
    private String name;

    //账号
    private String username;

    //密码
    private String password;

    //手机号
    private String phone;

    //个人介绍
    private String description;

    //1男2女0保密
    private int sex;

    //1启用0禁用，默认1
    private int status;

    //头像图片
    private String img;

    //创建时间
    private LocalDateTime createTime;
}

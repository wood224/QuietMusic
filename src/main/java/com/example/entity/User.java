package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息
 */
@Data
public class User {
    private static final long serialVersionUID = 1L;

    private Integer id;

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
    private Integer sex;

    //1启用0禁用，默认1
    private Integer status;

    //头像图片
    private String img;

    //创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    //逻辑删除
    @TableLogic(value = "0",delval = "1")
    private Integer deleted;
}

package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Comment {
    //id
    private Integer id;

    //音乐Id
    private Integer musicId;

    //用户Id
    private Integer userId;

    //回复评论id
    private Integer replyId;

    //点赞量
    private Integer agreement;

    //评论内容
    private String content;

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

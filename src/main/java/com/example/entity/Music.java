package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.models.auth.In;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 实体类
 * 歌曲
 */
@Data
@TableName(autoResultMap = true)
public class Music {
    //音乐id
    private Integer id;

    private String Name;

    //歌手
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String[] singerName;

    //时长
    private String time;

    private String album;

    //播放次数
    private Integer views;

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

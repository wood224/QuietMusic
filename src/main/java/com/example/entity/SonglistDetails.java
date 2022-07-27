package com.example.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 实体类
 * 歌单歌曲
 */
@Data
@TableName(autoResultMap = true)
public class SonglistDetails {
    //id
    private Integer id;

    //歌单id
    private Integer listId;

    //音乐id
    private Integer musicId;

    //歌曲名称
    private String musicName;

    //歌手
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private String[] singerName;

    //时长
    private String time;

    //专辑
    private String album;

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

package com.example.entity;

import lombok.Data;

import java.util.List;

/**
 * Dto实体类
 * 歌单详情
 */
@Data
public class SongListDto extends SongList{
    //用户名称
    private String userName;

    private Integer sum;

    //歌单内歌曲
    private List<SonglistDetails> songs;
}

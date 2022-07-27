package com.example.entity;

import lombok.Data;

import java.util.List;

@Data
public class SongListDto extends SongList{
    private String userName;

    private List<SonglistDetails> songs;
}

package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.SongList;
import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SongListMapper extends BaseMapper<SongList> {
}

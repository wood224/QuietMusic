package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.ListSongs;
import com.example.entity.Music;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ListSongsMapper extends BaseMapper<ListSongs> {
}

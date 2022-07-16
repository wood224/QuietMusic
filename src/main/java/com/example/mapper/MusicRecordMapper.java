package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.MusicRecord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MusicRecordMapper extends BaseMapper<MusicRecord> {
}

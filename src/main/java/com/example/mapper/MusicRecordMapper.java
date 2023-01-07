package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.Music;
import com.example.entity.MusicRecord;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface MusicRecordMapper extends BaseMapper<MusicRecord> {

    List<Music> selectRank();

    List<Music> selectRankByOneTime();

    List<Music> selectRankBySevenTime();

    List<Music> selectRankByThirtyTime();
}

package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Music;
import com.example.entity.MusicRecord;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public interface MusicRecordService extends IService<MusicRecord> {

    List<Music> getRank();
    List<Music> getOneRank();

    List<Music> getSevenRank();

    List<Music> getThirtyRank();
}

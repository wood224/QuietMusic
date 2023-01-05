package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.MusicRecord;
import io.swagger.models.auth.In;

import java.util.List;
import java.util.Map;

public interface MusicRecordService extends IService<MusicRecord> {

    List<MusicRecord> getRank();
    List<MusicRecord> getOneRank();

    List<MusicRecord> getSevenRank();

    List<MusicRecord> getThirtyRank();
}

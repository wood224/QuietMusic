package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Music;
import com.example.entity.MusicRecord;
import com.example.mapper.MusicMapper;
import com.example.mapper.MusicRecordMapper;
import com.example.service.MusicRecordService;
import com.example.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class MusicRecordServiceImpl extends ServiceImpl<MusicRecordMapper, MusicRecord> implements MusicRecordService {

    @Autowired
    private MusicRecordMapper musicRecordMapper;

    public List<Music> getRank(){
        return musicRecordMapper.selectRank();
    }

    public List<Music> getOneRank(){
        return musicRecordMapper.selectRankByOneTime();
    }

    public List<Music> getSevenRank(){
        return musicRecordMapper.selectRankBySevenTime();
    }

    public List<Music> getThirtyRank(){
        return musicRecordMapper.selectRankByThirtyTime();
    }
}

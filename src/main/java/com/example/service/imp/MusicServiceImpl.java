package com.example.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.R;
import com.example.entity.Music;
import com.example.entity.MusicRecord;
import com.example.entity.User;
import com.example.mapper.MusicMapper;
import com.example.mapper.UserMapper;
import com.example.service.MusicRecordService;
import com.example.service.MusicService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper,Music> implements MusicService {

    @Autowired
    private MusicRecordService musicRecordService;

    @Transactional
    public void saveMusic(Integer id,Integer userId){
        Music music = super.getById(id);
        if (music!=null){
            music.setViews(music.getViews()+1);
            super.updateById(music);
        }
        else{
            Music music1 = new Music();
            music1.setId(id);
            super.save(music1);
        }
        if(userId!=null) {
            LambdaQueryWrapper<MusicRecord> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(MusicRecord::getMusicId, id);
            lambdaQueryWrapper.eq(MusicRecord::getUserId, userId);
            MusicRecord musicRecord = musicRecordService.getOne(lambdaQueryWrapper);
            if (musicRecord == null) {
                MusicRecord musicRecord1 = new MusicRecord();
                musicRecord1.setMusicId(id);
                musicRecord1.setUserId(userId);
                musicRecordService.save(musicRecord1);
            } else {
                musicRecord.setViews(musicRecord.getViews() + 1);
                musicRecordService.updateById(musicRecord);
            }
        }
    }
}

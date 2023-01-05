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

        MusicRecord musicRecord = new MusicRecord();
        musicRecord.setMusicId(id);
        musicRecord.setUserId(userId);
        musicRecordService.save(musicRecord);

    }

}

package com.example.service.imp;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.R;
import com.example.entity.Music;
import com.example.entity.MusicDto;
import com.example.entity.MusicRecord;
import com.example.entity.User;
import com.example.mapper.MusicMapper;
import com.example.mapper.UserMapper;
import com.example.service.MusicRecordService;
import com.example.service.MusicService;
import com.example.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MusicServiceImpl extends ServiceImpl<MusicMapper,Music> implements MusicService {

    @Autowired
    private MusicRecordService musicRecordService;

    @Transactional
    public void saveMusic(MusicDto musicDto){
        Music music = super.getById(musicDto.getId());
        if (music!=null){
            music.setName(musicDto.getName());
            music.setSingerName(musicDto.getSingerName());
            music.setTime(musicDto.getTime());
            music.setAlbum(musicDto.getAlbum());
            music.setViews(music.getViews()+1);
            super.updateById(music);
        }
        else{
            Music music1 = new Music();
            BeanUtils.copyProperties(musicDto,music1);
            super.save(music1);
        }
        MusicRecord musicRecord = new MusicRecord();
        musicRecord.setMusicId(musicDto.getId());
        musicRecord.setUserId(musicDto.getUserId());
        musicRecordService.save(musicRecord);

    }
}

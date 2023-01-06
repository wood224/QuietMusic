package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SongList;
import com.example.entity.User;
import com.example.mapper.SongListMapper;
import com.example.mapper.UserMapper;
import com.example.service.SongListService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongListServiceImpl extends ServiceImpl<SongListMapper, SongList> implements SongListService {

    @Autowired

    private SongListMapper songListMapper;


    @Override
    public void updateAdd(Integer id) {
        songListMapper.updateAdd(id);
    }

    @Override
    public void updateRe(Integer id) {
        songListMapper.updateRe(id);
    }

    @Override
    public void updateClear(Integer id) {
        songListMapper.updateClear(id);
    }
}

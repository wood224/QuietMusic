package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.Music;
import com.example.entity.User;

public interface MusicService extends IService<Music> {

    public void saveMusic(Integer id,Integer userId);
}

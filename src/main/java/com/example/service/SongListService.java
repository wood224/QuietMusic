package com.example.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.entity.SongList;
import com.example.entity.User;

public interface SongListService extends IService<SongList> {

    void updateAdd(Integer id);

    void updateRe(Integer id);

    void updateClear(Integer id);

}

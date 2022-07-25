package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.Playlist;
import com.example.entity.User;
import com.example.mapper.PlaylistMapper;
import com.example.mapper.UserMapper;
import com.example.service.PlaylistService;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class PlaylistServiceImpl extends ServiceImpl<PlaylistMapper, Playlist> implements PlaylistService {
    //test
}

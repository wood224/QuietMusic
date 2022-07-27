package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SongList;
import com.example.entity.User;
import com.example.mapper.SongListMapper;
import com.example.mapper.UserMapper;
import com.example.service.SongListService;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class SongListServiceImpl extends ServiceImpl<SongListMapper, SongList> implements SongListService {
}

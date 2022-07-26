package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.ListSongs;
import com.example.entity.User;
import com.example.mapper.ListSongsMapper;
import com.example.mapper.UserMapper;
import com.example.service.ListSongsService;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class ListSongsServiceImpl extends ServiceImpl<ListSongsMapper, ListSongs> implements ListSongsService {
    //test
}

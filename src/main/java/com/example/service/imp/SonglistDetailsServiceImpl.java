package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.SonglistDetails;
import com.example.entity.User;
import com.example.mapper.SonglistDetailsMapper;
import com.example.mapper.UserMapper;
import com.example.service.SonglistDetailsService;
import com.example.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class SonglistDetailsServiceImpl extends ServiceImpl<SonglistDetailsMapper, SonglistDetails> implements SonglistDetailsService {
}

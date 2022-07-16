package com.example.service.imp;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.entity.MusicRecord;
import com.example.mapper.MusicMapper;
import com.example.mapper.MusicRecordMapper;
import com.example.service.MusicRecordService;
import com.example.service.MusicService;
import org.springframework.stereotype.Service;

@Service
public class MusicRecordServiceImpl extends ServiceImpl<MusicRecordMapper, MusicRecord> implements MusicRecordService {
}

package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.MusicRecord;
import com.example.mapper.MusicRecordMapper;
import com.example.service.MusicRecordService;
import com.example.service.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/record")
@Api(tags = "历史播放记录")
public class RecordController {


    @Autowired
    private MusicService musicService;

    @Autowired
    private MusicRecordService musicRecordService;

    @GetMapping("/{id}")
    @ApiOperation("获取历史播放记录")
    public R<List<MusicRecord>> getRecord(@PathVariable Integer id){
        LambdaQueryWrapper<MusicRecord> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(MusicRecord::getUpdateTime);
        List<MusicRecord> list = musicRecordService.list(lambdaQueryWrapper);
        return R.success(list);
    }
}

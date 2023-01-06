package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.entity.Music;
import com.example.entity.MusicDto;
import com.example.entity.MusicRecord;
import com.example.mapper.MusicRecordMapper;
import com.example.service.MusicRecordService;
import com.example.service.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/music")
@Api(tags = "音乐相关接口")
public class MusicController {

    @Autowired
    private MusicService musicService;
    @Autowired
    private MusicRecordService musicRecordService;

    @ApiOperation("保存歌曲")
    @PostMapping("/save")
    public R<String> saveMusic(@RequestBody MusicDto musicDto){
        musicService.saveMusic(musicDto);
        return R.success("保存成功！");
    }


    @ApiOperation("音乐总播放榜单")
    @PostMapping("/rank")
    public R<List<Music>> rank(){
        List<Music> records= musicRecordService.getRank();

        return R.success(records);

    }

    @ApiOperation("音乐昨日榜单")
    @PostMapping("/oneRank")
    public R<List<Music>> oneRank(){
        List<Music> records= musicRecordService.getOneRank();

        return R.success(records);

    }

    @ApiOperation("音乐七日榜单")
    @PostMapping("/sevenRank")
    public R<List<Music>> sevenRank(){
        List<Music> records= musicRecordService.getSevenRank();

        return R.success(records);

    }

    @ApiOperation("音乐三十日榜单")
    @PostMapping("/thirtyRank")
    public R<List<Music>> thirtyRank(){
        List<Music> records= musicRecordService.getThirtyRank();

        return R.success(records);

    }


}

package com.example.controller;

import com.example.common.R;
import com.example.entity.Music;
import com.example.service.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/music")
@Api(tags = "记录歌曲播放次数")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @ApiOperation("保存歌曲")
    @PostMapping("/save")
    public R<String> saveMusic(@RequestBody Integer id,Integer userId){
        musicService.saveMusic(id,userId);
        return R.success("保存成功！");
    }
}

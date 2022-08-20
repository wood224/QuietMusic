package com.example.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.common.R;
import com.example.entity.Music;
import com.example.entity.MusicRecord;
import com.example.service.MusicService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/music")
@Api(tags = "音乐相关接口")
public class MusicController {

    @Autowired
    private MusicService musicService;

    @ApiOperation("保存歌曲")
    @PostMapping("/save")
    public R<String> saveMusic(@RequestBody MusicRecord musicRecord){
        musicService.saveMusic(musicRecord.getMusicId(),musicRecord.getUserId());
        return R.success("保存成功！");
    }


    @ApiOperation("音乐总播放榜单")
    @PostMapping("/rank")
    public R<Page<Music>> rank(){
        Page<Music> page =new Page<>(1,10);

        LambdaQueryWrapper<Music> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Music::getViews);

        musicService.page(page,lambdaQueryWrapper);

        return R.success(page);

    }


    /*@ApiOperation("音乐热歌榜")
    @PostMapping("/ranknew")
    public R<Page<Music>> ranknew(){
        Page<Music> page =new Page<>(1,10);

        LambdaQueryWrapper<Music> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByDesc(Music::getViews);
        lambdaQueryWrapper.orderByDesc(Music::getCreateTime);

        musicService.page(page,lambdaQueryWrapper);

        return R.success(page);

    }*/
}

package com.example.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.SonglistDetails;
import com.example.service.SonglistDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songlistdetails")
@Api(tags = "歌单内歌曲接口")
public class SonglistDetailsController {

    @Autowired
    private SonglistDetailsService songlistDetailsService;

    @PostMapping("/add")
    @ApiOperation("添加歌曲")
    public R<String> addSong(@RequestBody SonglistDetails songlistDetails){
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,songlistDetails.getListId());
        lambdaQueryWrapper.eq(SonglistDetails::getMusicId,songlistDetails.getMusicId());

        SonglistDetails songlistDetails1 = songlistDetailsService.getOne(lambdaQueryWrapper);
        if(songlistDetails1!=null)
            return R.error("该乐曲已在歌单中！");

        songlistDetailsService.save(songlistDetails);
        return R.success("添加成功!");
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除歌曲")
    public R<String> deleteSong(Integer id){
        songlistDetailsService.removeById(id);
        return R.success("删除成功!");
    }

    @GetMapping("/getsongs")
    @ApiOperation("获取歌单全部歌曲")
    public R<List<SonglistDetails>> getList(Integer id){
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,id);
        lambdaQueryWrapper.orderByAsc(SonglistDetails::getCreateTime);

        List<SonglistDetails> list = songlistDetailsService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/delall")
    @ApiOperation("清空歌单")
    public R<String> delall(Integer id){
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,id);
        songlistDetailsService.remove(lambdaQueryWrapper);
        return R.success("清空成功");
    }

    @PostMapping("/insertAll")
    @ApiOperation("批量插入")
    public R<String> insertAll(@RequestBody SonglistDetails[] songlistDetails) {
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        for (SonglistDetails s : songlistDetails) {
            lambdaQueryWrapper.eq(SonglistDetails::getListId, s.getListId());
            lambdaQueryWrapper.eq(SonglistDetails::getMusicId, s.getMusicId());
            SonglistDetails songlistDetails1 = songlistDetailsService.getOne(lambdaQueryWrapper);
            if (songlistDetails1 != null)
                continue;
            songlistDetailsService.save(s);
        }
        return R.success("添加成功");
    }


}

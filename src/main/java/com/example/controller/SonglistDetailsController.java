package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.common.R;
import com.example.entity.SongList;
import com.example.entity.SonglistDetails;
import com.example.service.SongListService;
import com.example.service.SonglistDetailsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.util.Json;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/songlistdetails")
@Api(tags = "歌单内歌曲接口")
public class SonglistDetailsController {

    @Autowired
    private SonglistDetailsService songlistDetailsService;

    @Autowired
    private SongListService songListService;

    @PostMapping("/add")
    @ApiOperation("添加歌曲")
    @Transactional
    public R<String> addSong(@RequestBody SonglistDetails songlistDetails){
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,songlistDetails.getListId());
        lambdaQueryWrapper.eq(SonglistDetails::getMusicId,songlistDetails.getMusicId());

        SonglistDetails songlistDetails1 = songlistDetailsService.getOne(lambdaQueryWrapper);
        if(songlistDetails1!=null)
            return R.error("该乐曲已在歌单中！");

        songlistDetailsService.save(songlistDetails);

        songListService.updateAdd(songlistDetails.getListId());
        return R.success("添加成功!");
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除歌曲")
    public R<String> deleteSong(Integer id,Integer listId){
        songlistDetailsService.removeById(id);

        songListService.updateRe(listId);

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
    @Transactional
    public R<String> delall(Integer listId){
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,listId);
        songlistDetailsService.remove(lambdaQueryWrapper);

        songListService.updateClear(listId);
        return R.success("清空成功");
    }

    @PostMapping("/insertAll")
    @ApiOperation("批量插入")
    @Transactional
    public R<String> insertAll(@RequestBody SonglistDetails[] songlistDetails) {
        /*System.out.println(songlistDetails);
        List<SonglistDetails>  list= JSON.parseArray(songlistDetails,SonglistDetails.class);

        if(list==null)
            return R.error("请选择至少一首歌！");*/
        if(songlistDetails.length==0)
            return R.error("请至少添加一首歌");


        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId, songlistDetails[0].getListId());
        List<SonglistDetails> list = songlistDetailsService.list(lambdaQueryWrapper);

        Map<Integer,SonglistDetails> map = new HashMap<>();
        for (SonglistDetails s:list){
            map.put(s.getMusicId(),s);
        }

        for (SonglistDetails s : songlistDetails) {
            if (!map.containsKey(s.getMusicId())) {
                songlistDetailsService.save(s);
                songListService.updateAdd(s.getListId());
            }
        }
        return R.success("添加成功");
    }


}

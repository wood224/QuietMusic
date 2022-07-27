package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.ListSongs;
import com.example.service.ListSongsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/listsongs")
@Api(tags = "播放列表歌曲接口")
public class ListSongsController {

    @Autowired
    private ListSongsService listSongsService;


    @PostMapping("/add")
    @ApiOperation("添加歌曲")
    public R<String> addSong(@RequestBody ListSongs listSongs){
        LambdaQueryWrapper<ListSongs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ListSongs::getListId,listSongs.getListId());
        lambdaQueryWrapper.eq(ListSongs::getMusicId,listSongs.getMusicId());
        if (listSongsService.count(lambdaQueryWrapper)!=0)
            return R.error("此歌曲已在播放列表中!");
        listSongsService.save(listSongs);
        return R.success("添加成功!");
    }

    @PostMapping("/delete")
    @ApiOperation("移除歌曲")
    public R<String> deleteSong(@RequestBody ListSongs listSongs){
        LambdaQueryWrapper<ListSongs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ListSongs::getListId,listSongs.getListId());
        lambdaQueryWrapper.eq(ListSongs::getMusicId,listSongs.getMusicId());
        listSongsService.remove(lambdaQueryWrapper);
        return R.success("删除成功!");
    }


    @GetMapping("/getsongs")
    @ApiOperation("获取列表歌曲")
    public R<List<ListSongs>> getsongs(Integer id){
        LambdaQueryWrapper<ListSongs> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ListSongs::getListId,id);
        lambdaQueryWrapper.orderByAsc(ListSongs::getCreateTime);
        List<ListSongs> list = listSongsService.list(lambdaQueryWrapper);

        return R.success(list);
    }
}

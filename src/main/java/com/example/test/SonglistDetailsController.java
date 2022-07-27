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
@RequestMapping("/Songlistdetails")
@Api(tags = "歌单内歌曲接口")
public class SonglistDetailsController {

    @Autowired
    private SonglistDetailsService songlistDetailsService;

    @PostMapping("/add")
    @ApiOperation("添加歌曲")
    public R<String> addSong(@RequestBody SonglistDetails songlistDetails){
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

}

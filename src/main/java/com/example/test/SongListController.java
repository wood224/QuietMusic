package com.example.test;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.SongList;
import com.example.entity.SongListDto;
import com.example.entity.SonglistDetails;
import com.example.service.SongListService;
import com.example.service.SonglistDetailsService;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songlist")
@Api(tags = "歌单接口")
public class SongListController {

    @Autowired
    private SongListService songListService;

    @Autowired
    private UserService userService;

    @Autowired
    private SonglistDetailsService songlistDetailsService;

    @PostMapping("/create")
    @ApiOperation("新建歌单")
    public R<String> createlist(@RequestBody SongList songList){
        songListService.save(songList);
        return R.success("创建成功");
    }

    @GetMapping("/getdetails")
    @ApiOperation("获取歌单详情")
    public R<SongListDto> getDetails(Integer id){
        SongList list = songListService.getById(id);
        SongListDto listDto = new SongListDto();
        BeanUtils.copyProperties(list,listDto);
        listDto.setUserName(userService.getById(list.getUserId()).getName());
        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,id);
        lambdaQueryWrapper.orderByAsc(SonglistDetails::getCreateTime);
        listDto.setSongs(songlistDetailsService.list(lambdaQueryWrapper));

        return R.success(listDto);
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除歌单")
    @Transactional
    public R<String> deleteList(Integer id){
        songListService.removeById(id);

        LambdaQueryWrapper<SonglistDetails> lambdaQueryWrapper= new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SonglistDetails::getListId,id);
        songlistDetailsService.remove(lambdaQueryWrapper);
        return R.success("删除成功！");
    }

    @GetMapping("/lists")
    @ApiOperation("获取用户所有歌单")
    public R<List<SongList>> getLists(Integer id){
        LambdaQueryWrapper<SongList> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SongList::getUserId,id);
        lambdaQueryWrapper.orderByAsc(SongList::getCreateTime);

        List<SongList> list = songListService.list(lambdaQueryWrapper);
        return R.success(list);
    }

}

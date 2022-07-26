package com.example.test;

import com.example.common.R;
import com.example.entity.Playlist;
import com.example.service.PlaylistService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/playlist")
@Api(tags = "播放列表接口")
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;


    @PostMapping("/create")
    @ApiOperation("创建播放列表")
    public R<Playlist> createList(){
        Playlist list =new Playlist();
        playlistService.save(list);
        return R.success(list);
    }

}

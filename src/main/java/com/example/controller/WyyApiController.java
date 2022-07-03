package com.example.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/song")
@Api(tags = "转网易云接口")
public class WyyApiController {

    @Autowired
    private RestTemplate restTemplate;


    @GetMapping("/detail")
    @ApiOperation("获取歌曲详情")
    public Object getDetail(String ids) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/song/detail?ids=" + ids;
        return restTemplate.getForObject(url, Object.class);
    }

    @GetMapping("/url")
    @ApiOperation("获取音乐url")
    public Object getUrl(String id, String br) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/song/url?id=" + id + "&br=" + br;
        return restTemplate.getForObject(url, Object.class);
    }

    @GetMapping("/search")
    @ApiOperation("搜索")
    public Object search(String keywords, String limit, String offset, String type) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/search?keywords="
                + keywords + "&limit=" + limit + "&offset=" + offset + "&type=" + type;
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/lyric")
    @ApiOperation("获取歌词")
    public Object getLyric(String id) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/lyric?id=" + id;
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/artists")
    @ApiOperation("获取歌手单曲")
    public Object getDanQu(String id) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/artists?id=" + id;
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/banner")
    @ApiOperation("轮播图")
    public Object getBanner(int type) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/banner?type=" + type;
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/artistlist")
    @ApiOperation("获取歌手列表")
    public Object getArtists(int type, int area) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/artist/list?type=" + type + "&area=" + area;
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/catlist")
    @ApiOperation("获取歌单分类")
    public Object getCatList() {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/playlist/catlist";
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/sldetail")
    @ApiOperation("获取歌单详情")
    public Object getSlDetail(String id) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/playlist/detail?id=" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}



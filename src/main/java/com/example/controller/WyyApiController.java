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
        String brkey = "";
        if(br!=null)
            brkey="&br="+br;
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/song/url?id=" + id + brkey;
        return restTemplate.getForObject(url, Object.class);
    }

    @GetMapping("/search")
    @ApiOperation("搜索")
    public Object search(String keywords, String limit, String offset, String type) {
        String limitkey="";
        String offsetkey="";
        String typekey="";
        if(limit!=null)
            limitkey="&limit="+limit;
        if(offset!=null)
            offsetkey="&offset="+offset;
        if(type!=null)
            typekey="&type="+type;
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/search?keywords="
                + keywords + limitkey+offsetkey+typekey;
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
    public Object getBanner(Integer type) {
        String typekey="";
        if (type!=null)
            typekey="?type="+type;
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/banner" + typekey;
        return restTemplate.getForObject(url, Object.class);
    }


    @GetMapping("/artistlist")
    @ApiOperation("获取歌手列表")
    public Object getArtists(Integer type, Integer area) {
        String keys="";
        if(type==null&&area!=null)
            keys="?area="+area;
        else if(type!=null&&area==null)
            keys="?type="+type;
        else if (type!=null&&area!=null)
            keys="?type=" + type + "&area=" + area;
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/artist/list"+keys;
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

    @GetMapping("/musiccheck")
    @ApiOperation("音乐是否可用")
    public Object getMCheck(String id) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/check/music?id=" + id;
        return restTemplate.getForObject(url, Object.class);
    }
}



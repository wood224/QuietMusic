package com.example.controller;

import com.alibaba.fastjson.JSON;
import com.example.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Slf4j
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
        String keys="?";
        String typeKey="type="+type+"&";
        String areaKey="area="+area+"&";
        if(type!=null)
            keys += typeKey;
        if(area!=null)
            keys += areaKey;
        keys = keys.substring(0,keys.length()-1);
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
    public Object getMCheck(Integer id) {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/check/music?id=" + id;
        try {
            restTemplate.getForObject(url, Object.class);
            return R.success("有版权");
        }catch (Exception e){
            return R.nferror("亲爱的，暂无版权");
        }
    }

    @GetMapping("/hqtags")
    @ApiOperation("获取精品歌单标签列表")
    public Object getHQTags() {
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/playlist/highquality/tags";
        return restTemplate.getForObject(url, Object.class);
    }

    @GetMapping("/playlistall")
    @ApiOperation("获取歌单所有歌曲")
    public Object getPlaylistAll(Integer id, String limit, String offset) {
        String limitkey="";
        String offsetkey="";;
        if(limit!=null)
            limitkey="&limit="+limit;
        if(offset!=null)
            offsetkey="&offset="+offset;
        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/playlist/track/all?id="
                + id + limitkey+offsetkey;
        return JSON.toJSON(restTemplate.getForObject(url, Object.class));
    }

    @GetMapping("/hqplaylist")
    @ApiOperation("获取精品歌单")
    public Object getHQPlaylist(String cat,Integer limit,Integer before) {

        String keys="?";
        String catKey="cat="+cat+"&";
        String limitKey="limit="+limit+"&";
        String beforeKey="before="+before+"&";
        if(cat!=null)
            keys += catKey;
        if(limit!=null)
            keys+=limitKey;
        if (before!=null)
            keys+=beforeKey;
        keys=keys.substring(0,keys.length()-1);


        String url = "https://netease-cloud-music-api-theta-two-56.vercel.app/top/playlist/highquality"+keys;

        return restTemplate.getForObject(url, Object.class);
    }
}



package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.Collect;
import com.example.service.CollectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.management.DescriptorKey;
import java.util.List;

@RestController
@RequestMapping("/collect")
@Api(tags = "个人收藏接口")
public class CollectController {

    @Autowired
    private CollectService collectService;


    @GetMapping("/{id}")
    @ApiOperation("获取收藏歌曲列表")
    public R<List<Collect>> getCollect(@PathVariable Integer id){
        LambdaQueryWrapper<Collect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collect::getUserId,id);
        lambdaQueryWrapper.orderByAsc(Collect::getCreateTime);
        List<Collect> list = collectService.list(lambdaQueryWrapper);
        if (list==null)
            return R.error("清去收藏一首歌吧~");
        return R.success(list);
    }

    @PostMapping("/add")
    @ApiOperation("收藏歌曲")
    public R<String> addMusic(@RequestBody Collect collect){
        if (collect.getUserId()==null)
            return R.error("请在登陆后收藏！");
        LambdaQueryWrapper<Collect> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Collect::getUserId,collect.getUserId());
        lambdaQueryWrapper.eq(Collect::getMusicId,collect.getMusicId());
        long count = collectService.count(lambdaQueryWrapper);
        if (count>0)
            return R.error("您已收藏本歌曲");
        collectService.save(collect);
        return R.success("收藏成功!");
    }

    @DeleteMapping("/delete")
    @ApiOperation("删除收藏歌曲")
    public R<String> deleted(Integer id){
        collectService.removeById(id);
        return R.success("删除成功");
    }

}

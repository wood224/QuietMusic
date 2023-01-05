package com.example.test;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.Comment;
import com.example.entity.CommentDto;
import com.example.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/comment")
@Api(tags = "评论接口")
public class CommentController {


    @Autowired
    private CommentService commentService;

    @PostMapping("/publish")
    @ApiOperation("发表评论")
    public R<String> publish(@RequestBody Comment comment){
        if(comment.getReplyId()!=null)
        {
            Comment comment1= commentService.getById(comment.getReplyId());
            if (comment1==null)
                return R.error("该评论被吃掉了喵~");
        }

        commentService.save(comment);
        return R.success("保存成功!");
    }


    //树状评论显示，因删除逻辑复杂，已弃用
    /*@GetMapping("/byMusic")
    @ApiOperation("获取音乐所有评论")
    public R<List<CommentDto>> byMusic(Integer id){
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getMusicId,id);
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = commentService.list(lambdaQueryWrapper);
        List<CommentDto> commentDtos= comments.stream().map((item) -> {
            CommentDto commentDto = new CommentDto();
            BeanUtils.copyProperties(item, commentDto);
            return commentDto;
        }).toList();


        Map<Integer,CommentDto> map = new HashMap<>();
        List<CommentDto> result = new ArrayList<>();

        for (CommentDto c : commentDtos) {
            if (c.getReplyId() == null) {
                result.add(c);
            }
            map.put(c.getId(), c);
        }
        for (CommentDto c : commentDtos) {
            if (c.getReplyId() != null) {
                CommentDto parent = map.get(c.getReplyId());
                parent.getComments().add(c);
            }
        }

        return R.success(result);
    }*/

    @GetMapping("/byMusic")
    @ApiOperation("获取音乐所有评论")
    public R<List<Comment>> byMusic(Integer id) {
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getMusicId,id);
        lambdaQueryWrapper.orderByAsc(Comment::getCreateTime);
        List<Comment> comments = commentService.list(lambdaQueryWrapper);
        return R.success(comments);
    }


    @GetMapping("/delete")
    @ApiOperation("删除评论")
    public R<String> deleteById(Integer id)
    {
        commentService.removeById(id);
        return R.success("删除成功");
    }

}
package com.example.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.Agreement;
import com.example.entity.Comment;
import com.example.entity.CommentDto;
import com.example.entity.User;
import com.example.service.AgreementService;
import com.example.service.CommentService;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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

    @Autowired
    private AgreementService agreementService;

    @Autowired
    private UserService userService;


    @PostMapping("/publish")
    @ApiOperation("发表评论")
    public R<String> publish(@RequestBody Comment comment){

        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.ge(Comment::getCreateTime, LocalDateTime.now().minusSeconds(5));
        //List<Comment> list = commentService.list(lambdaQueryWrapper);
        Comment list = commentService.getOne(lambdaQueryWrapper);
        if (list!=null)
            return R.error("发送评论过快喵！");

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
    public R<List<CommentDto>> byMusic(Integer id,Integer userId,Integer order) {

        //获取所有评论
        LambdaQueryWrapper<Comment> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Comment::getMusicId,id);
        if (order==1)//按发布时间降序排列
            lambdaQueryWrapper.orderByDesc(Comment::getCreateTime);
        if (order==2)//按点赞数排列
            lambdaQueryWrapper.orderByDesc(Comment::getAgreement,Comment::getCreateTime);
        List<Comment> comments = commentService.list(lambdaQueryWrapper);
        //获取用户点赞信息
        LambdaQueryWrapper<Agreement> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Agreement::getUserId, userId);
        List<Agreement> agreements = agreementService.list(lqw);

        //获取用户信息
        Map<Integer,User> mapU =new HashMap<>();
        List<User> users = userService.list();
        for (User u:users){
            mapU.put(u.getId(),u);
        }

        //将用户点赞信息存入Map
        Map<Integer,Agreement> map =new HashMap<>();
        for(Agreement a:agreements){
            map.put(a.getCommentId(),a);
        }
        //将评论存入Map
        Map<Integer,Comment> mapAll =new HashMap<>();
        for(Comment c:comments){
            mapAll.put(c.getId(),c);
        }

        //将comment转换为commentDto
        List<CommentDto> commentDtos = comments.stream().map((item)->{
            CommentDto commentDto =new CommentDto();
            BeanUtils.copyProperties(item,commentDto);
            User user = mapU.get(item.getUserId());
            commentDto.setUsrName(user.getName());
            commentDto.setImg(user.getImg());
            if(map.containsKey(item.getId()))
                commentDto.setFlag(true);
            if(item.getReplyId()!=null) {
                Comment c = mapAll.get(item.getReplyId());
                commentDto.setReplyUserName(mapU.get(c.getUserId()).getName());
                commentDto.setReplyContent(c.getContent());
            }
            return commentDto;
        }).toList();

        return R.success(commentDtos);
    }


    @GetMapping("/delete")
    @ApiOperation("删除评论")
    public R<String> deleteById(Integer id)
    {
        commentService.removeById(id);
        return R.success("删除成功");
    }

    @GetMapping("/byId")
    @ApiOperation("根据id查询")
    public R<Comment> byId(Integer id){
        Comment comment = commentService.getById(id);
        return R.success(comment);
    }

}

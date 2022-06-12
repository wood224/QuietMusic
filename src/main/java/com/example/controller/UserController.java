package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理接口
 */
@RestController
@RequestMapping("/user")
@Api(tags = "用户管理接口")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 根据id查询用户
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询用户")
    public R<User> getById(@PathVariable long id){
        User user = userService.getById(id);
        return R.success(user);
    }

    /**
     * 批量获取用户
     */
    @GetMapping("/list")
    @ApiOperation("批量获取用户")
    public R<List<User>> getList(String name){
        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(User::getName,name);

        List<User> list = userService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public R<String> register(@RequestBody User user){
        user.setCreateTime(LocalDateTime.now());
        userService.save(user);
        return R.success("添加成功!");
    }

    /**
     * 用户登陆
     */
    @GetMapping("/login")
    @ApiOperation("用户登陆")
    public R<User> login(String username,String password){
        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,username);
        lambdaQueryWrapper.eq(User::getPassword,password);
        User user= userService.getOne(lambdaQueryWrapper);
        if (user!=null)
        {
            if(user.getStatus()==0)
                return R.error("用户封禁中!");
            return R.success(user);
        }
        return R.error("登陆失败，请检查用户名或密码是否正确！");
    }

    /**
     * 更新信息
     */
    @PutMapping("/update")
    @ApiOperation("修改信息")
    public R<String> update(@RequestBody User user){
        userService.updateById(user);
        return R.success("修改成功!");
    }

    /**
     * 解封用户
     */
    @PostMapping("/status/1")
    @ApiOperation("解封用户")
    public R<String> enabled(@RequestBody User user){
        user.setStatus(1);
        userService.updateById(user);
        return R.success("解封成功!");
    }

    /**
     * 封禁用户
     */
    @PostMapping("/status/0")
    @ApiOperation("禁用用户")
    public R<String> disabled(@RequestBody User user){
        user.setStatus(0);
        userService.updateById(user);
        return R.success("封禁成功!");
    }
}

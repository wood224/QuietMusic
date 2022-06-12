package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/id")
    public R<User> getById(@PathVariable long id){
        User user = userService.getById(id);
        return R.success(user);
    }

    @PostMapping("/register")
    public R<String> register(@RequestBody User user){
        user.setCreateTime(LocalDateTime.now());
        userService.save(user);
        return R.success("添加成功!");
    }

    @GetMapping("/login")
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

    @PutMapping("/update")
    public R<String> update(@RequestBody User user){
        userService.updateById(user);
        return R.success("修改成功!");
    }

    @PostMapping("/status/1")
    public R<String> enabled(@RequestBody User user){
        user.setStatus(1);
        userService.updateById(user);
        return R.success("解封成功!");
    }

    @PostMapping("/status/0")
    public R<String> disabled(@RequestBody User user){
        user.setStatus(0);
        userService.updateById(user);
        return R.success("封禁成功!");
    }
}

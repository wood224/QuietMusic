package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
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
     * 检测用户名重复
     */
    @GetMapping("/repeat")
    @ApiOperation("检测用户名是否重复")
    public R<String> isRepeat(String username){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,username);
        User user = userService.getOne(lambdaQueryWrapper);
        if(user!=null)
        {
            return R.error("用户名重复");
        }
        return R.success("用户名可用");
    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    @ApiOperation("用户注册")
    public R<String> register(@RequestBody User user){
        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername, user.getUsername());
        User userh = userService.getOne(lambdaQueryWrapper);
        if(userh!=null)
        {
            return R.error("用户名重复!");
        }
        user.setCreateTime(LocalDateTime.now());
        user.setStatus(1);
        userService.save(user);
        return R.success("添加成功!");
    }

    /**
     * 用户登陆
     */
    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public R<User> login(@RequestBody User user, HttpServletRequest request){
        if(user.getUsername()==null&&user.getPhone()==null)
            return R.error("请输入用户名或手机号码！");
        LambdaQueryWrapper<User> lambdaQueryWrapper =new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(user.getUsername()!=null,User::getUsername,user.getUsername());
        lambdaQueryWrapper.eq(User::getPassword,user.getPassword());
        lambdaQueryWrapper.eq(user.getPhone()!=null,User::getPhone,user.getPhone());
        User userh= userService.getOne(lambdaQueryWrapper);
        if (userh!=null)
        {
            if(userh.getStatus()==0)
                return R.error("用户封禁中!");
            request.getSession().setAttribute("user",userh.getId());
            return R.success(userh);
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

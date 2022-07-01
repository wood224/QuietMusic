package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.common.R;
import com.example.entity.User;
import com.example.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
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
        lambdaQueryWrapper.like(name!=null,User::getName,name);

        List<User> list = userService.list(lambdaQueryWrapper);
        return R.success(list);
    }

    /**
     * 检测用户名重复
     */
    @GetMapping("/repeat")
    @ApiOperation("检测用户名是否重复")
    public R<String> isRepeat(String username,String phone){
        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(User::getUsername,username);
        LambdaQueryWrapper<User> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(User::getPhone,phone);
        User user = userService.getOne(lambdaQueryWrapper);
        User user1 = userService.getOne(lambdaQueryWrapper1);
        if(user!=null)
        {
            return R.error("用户名重复");
        }
        if(user1!=null)
        {
            return R.error("当前手机号已注册");
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
        lambdaQueryWrapper.eq(User::getPhone, user.getPhone());
        int count = Math.toIntExact(userService.count(lambdaQueryWrapper));
        if(count>0)
            return R.error("该用户名或手机号已被注册");
        String password = user.getPassword()+"iwgyf";
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        user.setPassword(password);
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
        String password = user.getPassword()+"iwgyf";
        password= DigestUtils.md5DigestAsHex(password.getBytes());
        lambdaQueryWrapper.eq(User::getPassword,password);
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
     * 注销用户
     */
    @DeleteMapping("/delete")
    @ApiOperation("注销账户")
    public R<String> delete(@RequestBody User user){
        userService.removeById(user);
        return R.success("注销成功");
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

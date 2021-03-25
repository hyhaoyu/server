package com.haoyu.controller;

import com.haoyu.pojo.TbAdmin;
import com.haoyu.pojo.vo.Admin;
import com.haoyu.pojo.vo.Result;
import com.haoyu.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AdminController {

    @Autowired
    AdminService adminService;

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(TbAdmin admin){
        try {
            Admin _admin=adminService.login(admin.getUsername(),admin.getPassword());

            if(_admin == null){
                return new Result(false,"登录失败，请检查用户名或者密码是否正确");
            }
            else{
                return new Result(true,"登录成功", _admin);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"登录错误");
        }
    }

    //注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(TbAdmin admin){

        try {
            adminService.register(admin);
            return new Result(true,"注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }

}

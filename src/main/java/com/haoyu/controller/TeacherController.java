package com.haoyu.controller;

import com.haoyu.pojo.TbTeacher;
import com.haoyu.pojo.vo.Result;
import com.haoyu.pojo.vo.Teacher;
import com.haoyu.pojo.vo.TeacherList;
import com.haoyu.service.TeacherService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/teacher")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    //登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(TbTeacher teacher){

        try {
            Teacher _teacher = teacherService.login(teacher.getUsername(),teacher.getPassword());
            if(_teacher == null){
                return new Result(false,"登录失败，请检查用户名或者密码是否正确");
            }
            else{
                return new Result(true,"登录成功",_teacher);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"登录错误");
        }

    }

    //注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(TbTeacher teacher){

        try {
            teacherService.register(teacher);
            return new Result(true,"注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }

    //修改
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result updateTeacher(TbTeacher teacher,
                                @RequestHeader(value = "authorization",required = false)String token) {
        try {
            teacherService.updateTeacher(teacher, token);
            return new Result(true,"更新成功");
        }
        catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"更新失败");
        }
    }

    //查询教师信息
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result queryTeacher(@RequestParam(value = "name",required = false)String name,
                               @RequestParam(value = "pageNum",required = false)Integer pageNum,
                               @RequestParam(value = "pageSize",required = false)Integer pageSize,
                               @RequestHeader(value = "authorization",required = false)String token) {
        try {
            TeacherList teacherList = teacherService.queryTeacher(name, pageNum, pageSize, token);
            if(teacherList == null){
                return new Result(false,"查询为空");
            }
            else{
                return new Result(true,"查询成功", teacherList);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }

    //删除讲师
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Result deleteUser(@PathVariable("id")String teacherId,
                             @RequestHeader(value = "authorization",required = false)String token) {

        try {
            teacherService.deleteTeacherByTeacherId(teacherId, token);
            return new Result(true,"删除成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }

}

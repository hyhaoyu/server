package com.haoyu.controller;

import com.haoyu.pojo.TbTeacher;
import com.haoyu.pojo.vo.*;
import com.haoyu.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/teacher")
@CrossOrigin(origins = "*", maxAge = 3600)
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    //登录
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@RequestBody TbTeacher teacher){

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
    public Result register(@RequestBody TbTeacher teacher){

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
    public Result updateTeacher(@RequestBody TbTeacher teacher,
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
    //上传头像
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public Result updateStudentAvatar(@RequestParam("id")String teacherId,
                                      @RequestParam("file") MultipartFile imgFile,
                                      @RequestHeader(value = "authorization",required = false)String token){
        try {
            Image image = teacherService.updateTeacherAvatar(teacherId, imgFile, token);
            return new Result(true,"头像上传成功", image);
        }
        catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"头像上传失败");
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
    //根据讲师id查找信息
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result queryTeacherById(@PathVariable(value = "id")String teacherId,
                                   @RequestHeader(value = "authorization",required = false)String token) {
        try {
            Teacher teacher = teacherService.queryTeacherById(teacherId, token);
            if(teacher == null){
                return new Result(false,"查询为空");
            }
            else{
                return new Result(true,"查询成功", teacher);
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

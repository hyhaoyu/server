package com.haoyu.controller;

import com.haoyu.pojo.TbStudent;
import com.haoyu.pojo.vo.*;
import com.haoyu.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping(value="/student")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentController {

    @Autowired
    private StudentService studentService;

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody TbStudent student){
        try {
            Student _student = studentService.login(student.getUsername(),student.getPassword());

            if(_student == null){
                return new Result(false,"登录失败，请检查用户名或者密码是否正确");
            }
            else{
                return new Result(true,"登录成功", _student);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"登录错误");
        }
    }

    //注册
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public Result register(@RequestBody TbStudent student){

        try {
            studentService.register(student);
            return new Result(true,"注册成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }

    //修改
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result updateStudent(@RequestBody TbStudent student,
                                @RequestHeader(value = "authorization",required = false)String token){
        try {
            studentService.updateStudent(student, token);
            return new Result(true,"用户更新成功");
        }
        catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"用户更新失败");
        }
    }
    //上传头像
    @RequestMapping(value = "avatar", method = RequestMethod.POST)
    public Result updateStudentAvatar(@RequestParam("id")String studentId,
                                      @RequestParam("file")MultipartFile imgFile,
                                      @RequestHeader(value = "authorization",required = false)String token){
        try {
            Image image = studentService.updateStudentAvatar(studentId, imgFile, token);
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

    //查询学生信息
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result queryStudent(@RequestParam(value = "name",required = false)String name,
                               @RequestParam(value = "courseId",required = false)String courseId,
                               @RequestParam(value = "pageNum",required = false)Integer pageNum,
                               @RequestParam(value = "pageSize",required = false)Integer pageSize,
                               @RequestHeader(value = "authorization",required = false)String token) {
        try {
            StudentList studentList = studentService.queryStudent(name, courseId, pageNum, pageSize, token);
            if(studentList == null){
                return new Result(false,"用户查询为空");
            }
            else{
                return new Result(true,"用户查询成功", studentList);
            }
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }
    //根据学员id查找信息
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result queryStudentById(@PathVariable(value = "id")String studentId,
                                   @RequestHeader(value = "authorization",required = false)String token) {
        try {
            Student student = studentService.queryStudentById(studentId, token);
            if(student == null){
                return new Result(false,"查询为空");
            }
            else{
                return new Result(true,"查询成功", student);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }

    //删除学生
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Result deleteStudent(@PathVariable("id")String studentId,
                                @RequestHeader(value = "authorization",required = false)String token){

        try {
            studentService.deleteStudentByStudentId(studentId, token);
            return new Result(true,"删除成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }
}

package com.haoyu.controller;

import com.haoyu.pojo.TbStudent;
import com.haoyu.pojo.vo.CourseGrade;
import com.haoyu.pojo.vo.Result;
import com.haoyu.pojo.vo.StudentCourseList;
import com.haoyu.service.StudentCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/studentCourse")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StudentCourseController {

    @Autowired
    private StudentCourseService studentCourseService;

    //查询学生课程信息
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result queryStudentCourse(@PathVariable("id")String userId,
                                     @RequestParam(value = "pageNum",required = false)Integer pageNum,
                                     @RequestParam(value = "pageSize",required = false)Integer pageSize,
                                     @RequestHeader(value = "authorization",required = false)String token){

        try {
            StudentCourseList studentcourseList = studentCourseService.queryStudentCourse(userId, pageNum, pageSize, token);
            if(studentcourseList == null){
                return new Result(false,"课程查询为空");
            }
            else{
                return new Result(true,"课程查询成功", studentcourseList);
            }
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }

    }

    //修改
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Result updateCourseGrade(@RequestBody CourseGrade courseGrade,
                                    @RequestHeader(value = "authorization",required = false)String token) {

        try {
            studentCourseService.updateCourseGrade(courseGrade, token);
            return new Result(true,"课程成绩更新成功");
        }
        catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"课程成绩更新失败");
        }
    }

    //退选课程
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Result deleteStudent(@PathVariable("id")String studentCourseId,
                                @RequestHeader(value = "authorization",required = false)String token){

        try {
            studentCourseService.deleteStudentCourseById(studentCourseId, token);
            return new Result(true,"退选成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }
}

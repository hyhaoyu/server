package com.haoyu.controller;

import com.haoyu.pojo.TbCourse;
import com.haoyu.pojo.vo.Result;
import com.haoyu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/course")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    //添加课程
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result addCourse(TbCourse course){

        try {
            TbCourse tbCourse = courseService.addCourse(course);
            return new Result(true,"课程添加成功", tbCourse);
        } catch (RuntimeException e){
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "课程添加错误");
        }

    }

    //删除课程
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public Result deleteCourse(@RequestParam("id")String courseId){

        try {
            courseService.deleteCourseByCourseId(courseId);
            return new Result(true,"课程删除成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }


    //修改课程
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public Result updateCourse(TbCourse course){

        try {
            TbCourse tbCourse = courseService.updateCourse(course);
            return new Result(true,"课程修改成功", tbCourse);
        } catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "课程修改错误");
        }

    }

    //查询课程
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result queryCourse(@RequestParam(value = "name", required = false)String name){

        List<TbCourse> courseList = courseService.queryCourse(name);
        if(courseList == null){
            return new Result(false,"课程查询为空");
        }
        else{
            return new Result(true,"课程查询成功", courseList);
        }

    }

}

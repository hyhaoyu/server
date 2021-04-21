package com.haoyu.controller;

import com.haoyu.pojo.TbCourse;
import com.haoyu.pojo.vo.CourseDetail;
import com.haoyu.pojo.vo.CourseList;
import com.haoyu.pojo.vo.Image;
import com.haoyu.pojo.vo.Result;
import com.haoyu.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/course")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CourseController {

    @Autowired
    CourseService courseService;

    //添加课程
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Result addCourse(@RequestBody TbCourse course,
                            @RequestHeader(value = "authorization",required = false)String token){

        try {
            TbCourse tbCourse = courseService.addCourse(course, token);
            return new Result(true,"课程添加成功", tbCourse);
        } catch (RuntimeException e){
            return new Result(false, e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "课程添加错误");
        }

    }

    //删除课程
    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public Result deleteCourse(@PathVariable("id")String courseId,
                               @RequestHeader(value = "authorization",required = false)String token){

        try {
            courseService.deleteCourseByCourseId(courseId, token);
            return new Result(true,"课程删除成功");
        } catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false,e.getMessage());
        }

    }


    //修改课程
    @RequestMapping(value = "",method = RequestMethod.PUT)
    public Result updateCourse(@RequestBody TbCourse course,
                               @RequestHeader(value = "authorization",required = false)String token){

        try {
            courseService.updateCourse(course, token);
            return new Result(true,"课程修改成功");
        } catch (RuntimeException e){
            return new Result(false, e.getMessage());
        }catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "课程修改错误");
        }

    }
    //上传课程图片
    @RequestMapping(value = "picture", method = RequestMethod.POST)
    public Result updateCoursePicture(@RequestParam("id")String courseId,
                                      @RequestParam("file") MultipartFile imgFile,
                                      @RequestHeader(value = "authorization",required = false)String token){
        try {
            Image image = courseService.updateCoursePicture(courseId, imgFile, token);
            return new Result(true,"课程图片上传成功", image);
        }
        catch (RuntimeException e){
            return new Result(false,e.getMessage());
        }
        catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"课程图片上传失败");
        }
    }

    //查询课程
    @RequestMapping(value = "", method = RequestMethod.GET)
    public Result queryCourse(@RequestParam(value = "name",required = false)String name,
                              @RequestParam(value = "teacherId",required = false)String teacherId,
                              @RequestParam(value = "pageNum",required = false)Integer pageNum,
                              @RequestParam(value = "pageSize",required = false)Integer pageSize,
                              @RequestHeader(value = "authorization",required = false)String token) {
        try {
            CourseList courseList = courseService.queryCourse(name, teacherId, pageNum, pageSize, token);
            if(courseList == null){
                return new Result(false,"课程查询为空");
            }
            else{
                return new Result(true,"课程查询成功", courseList);
            }
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }

    //根据课程id查找课程
    @RequestMapping(value = "{id}", method = RequestMethod.GET)
    public Result queryCourseById(@PathVariable("id")String courseId,
                                  @RequestHeader(value = "authorization",required = false)String token){

        try {
             CourseDetail courseDetail = courseService.queryCourseById(courseId, token);
            if(courseDetail == null){
                return new Result(false,"课程查询为空");
            }
            else{
                return new Result(true,"课程查询成功", courseDetail);
            }
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            return new Result(false, e.getMessage());
        }
    }

}

package com.haoyu.service.impl;

import com.haoyu.mapper.TbAdminMapper;
import com.haoyu.mapper.TbCourseMapper;
import com.haoyu.mapper.TbStudentCourseMapper;
import com.haoyu.mapper.TbTeacherMapper;
import com.haoyu.pojo.*;
import com.haoyu.pojo.vo.Course;
import com.haoyu.pojo.vo.CourseDetail;
import com.haoyu.pojo.vo.CourseList;
import com.haoyu.pojo.vo.Image;
import com.haoyu.service.CourseService;
import com.haoyu.util.IdWorker;
import com.haoyu.util.ImgFileWorker;
import com.haoyu.util.TokenWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    private TbAdminMapper adminMapper;
    @Autowired
    private TbTeacherMapper teacherMapper;
    @Autowired
    private TbCourseMapper courseMapper;
    @Autowired
    private TbStudentCourseMapper studentCourseMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public Image updateCoursePicture(String courseId, MultipartFile imgFile, String token) throws IOException {
        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);

        //验证权限
        TbCourse tbCourse = courseMapper.selectByPrimaryKey(courseId);
        if(tbCourse == null){
            throw new RuntimeException("课程更新错误");
        }
        if(!id.equals(tbCourse.getTeacherId()) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }

        //保存图片
        if(imgFile != null) {
            String picUrl = "/images/" + ImgFileWorker.saveImg(imgFile, "course", courseId);
            tbCourse.setPicUrl(picUrl);
            courseMapper.updateByPrimaryKey(tbCourse);
            return new Image(courseId, picUrl);
        }
        else{
            return null;
        }
    }

    @Override
    public TbCourse addCourse(TbCourse course, String token) {

        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);
        //验证权限
        TbTeacher teacher = teacherMapper.selectByPrimaryKey(id);
        if(teacher == null && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无添加权限，请以管理员或者讲师身份登录");
        }
        if(course == null){
            throw new RuntimeException("课程添加失败");
        }
        course.setId(String.valueOf(idWorker.nextId()));
        course.setHeadcount(0);
        course.setAddDate(new Date());
        if(teacher != null) course.setTeacherId(id);
        courseMapper.insert(course);
        return courseMapper.selectByPrimaryKey(course.getId());

    }

    @Override
    public void deleteCourseByCourseId(String courseId, String token) {

        TbCourse course = courseMapper.selectByPrimaryKey(courseId);
        if(course == null){
            throw new RuntimeException("课程不存在");
        };
        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);
        //验证权限
        if(!id.equals(course.getTeacherId()) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无删除权限，请以管理员或者用户本人身份登录");
        }
        courseMapper.deleteByPrimaryKey(courseId);
    }

    @Override
    public void updateCourse(TbCourse course, String token) {

        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);
        //验证权限
        if(!id.equals(course.getTeacherId()) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }

        TbCourse tbCourse = courseMapper.selectByPrimaryKey(course.getId());
        if(tbCourse == null){
            throw new RuntimeException("课程修改失败");
        }
        tbCourse.setCourseName(course.getCourseName());
        tbCourse.setDuration(course.getDuration());
        tbCourse.setIntroduction(course.getIntroduction());
        courseMapper.updateByPrimaryKey(tbCourse);

    }

    @Override
    public CourseDetail queryCourseById(String courseId, String token) {

        //解密token
        String studentId = TokenWorker.getIdFromJWT(token);

        //根据用户名查询数据
        TbCourse course = courseMapper.selectByPrimaryKey(courseId);
        if(course == null){
            return null;
        }

        //课程讲师姓名
        String teacherName = null;
        if(course.getTeacherId() != null){
            teacherName = teacherMapper.selectByPrimaryKey(course.getTeacherId()).getRealName();
        }

        TbStudentCourseExample example = new TbStudentCourseExample();
        TbStudentCourseExample.Criteria criteria = example.createCriteria();
        criteria.andUserIdEqualTo(studentId);
        criteria.andCourseIdEqualTo(courseId);
        List<TbStudentCourse> studentCourseList = studentCourseMapper.selectByExample(example);

        //学生课程成绩
        Integer grade = null;
        Boolean isTake = false;
        String studentCourseId = null;
        if(studentCourseList !=null && studentCourseList.size() != 0){
            isTake = true;
            studentCourseId = studentCourseList.get(0).getId();
            if(studentCourseList.get(0).getGrade() != null){
                grade = studentCourseList.get(0).getGrade();
            }
        }
        return new CourseDetail(course, teacherName, isTake, grade, studentCourseId);
    }

    @Override
    public CourseList queryCourse(String name, String teacherId, Integer pageNum, Integer pageSize, String token) {
        //解密token
        TokenWorker.verifyToken(token);

        TbCourseExample example = new TbCourseExample();
        TbCourseExample.Criteria criteria = example.createCriteria();

        //查询条件
        if(StringUtils.isNotBlank(name)){
            criteria.andCourseNameLike("%"+name+"%");
        }
        if(StringUtils.isNotBlank(teacherId)){
            criteria.andTeacherIdEqualTo(teacherId);
        }

        if(pageNum != null && pageSize != null){
            if(pageNum <= 0 || pageSize <= 0){
                pageNum = 1;
                pageSize = 10;
            }
            example.setStartCount((pageNum-1)*pageSize);
            example.setPageSize(pageSize);
        }
        else{
            pageNum = 1;
            example.setStartCount(null);
            example.setPageSize(null);
        }

        //根据用户名查询数据
        List<TbCourse> tbCourseList = courseMapper.selectByExample(example);
        long total = courseMapper.countByExample(example);
        List<Course> courseList = new ArrayList<>();

        //判断数据是否存在
        if(tbCourseList != null && tbCourseList.size() != 0){

            for(TbCourse tbCourse : tbCourseList){
                Course course = new Course();
                String teacherName;
                if(tbCourse.getTeacherId() == null){
                    teacherName = null;
                }
                else{
                    teacherName = teacherMapper.selectByPrimaryKey(tbCourse.getTeacherId()).getRealName();
                }
                course.setCourse(tbCourse, teacherName);
                courseList.add(course);
            }
            return new CourseList(total, pageNum, courseList);
        }
        return null;
    }

}

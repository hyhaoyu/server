package com.haoyu.service.impl;

import com.haoyu.mapper.TbAdminMapper;
import com.haoyu.mapper.TbCourseMapper;
import com.haoyu.mapper.TbStudentCourseMapper;
import com.haoyu.mapper.TbTeacherMapper;
import com.haoyu.pojo.*;
import com.haoyu.pojo.vo.CourseGrade;
import com.haoyu.pojo.vo.StudentCourse;
import com.haoyu.pojo.vo.StudentCourseList;
import com.haoyu.pojo.vo.StudentList;
import com.haoyu.service.StudentCourseService;
import com.haoyu.util.IdWorker;
import com.haoyu.util.TokenWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private TbStudentCourseMapper studentCourseMapper;
    @Autowired
    private TbCourseMapper courseMapper;
    @Autowired
    private TbTeacherMapper teacherMapper;
    @Autowired
    private TbAdminMapper adminMapper;
    @Autowired
    private IdWorker idWorker;

    @Override
    public StudentCourseList queryStudentCourse(String userId, Integer pageNum, Integer pageSize, String token) {

        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);

        //验证权限
        if(!id.equals(userId) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }

        TbStudentCourseExample example = new TbStudentCourseExample();
        TbStudentCourseExample.Criteria criteria = example.createCriteria();

        criteria.andUserIdEqualTo(userId);

        if(pageNum != null && pageSize != null){
            example.setStartCount((pageNum-1)*pageSize);
            example.setPageSize(pageSize);
        }
        else{
            pageNum = 1;
            example.setStartCount(null);
            example.setPageSize(null);
        }

        //根据用户名查询数据
        List<TbStudentCourse> TbStudentCourseList= studentCourseMapper.selectByExample(example);
        long total = studentCourseMapper.countByExample(example);
        List<StudentCourse> studentCourseList = new ArrayList<>();

        //判断数据是否存在
        if(TbStudentCourseList != null && TbStudentCourseList.size() != 0){

            for(TbStudentCourse tbStudentCourse : TbStudentCourseList){

                TbCourse course = queryCourseById(tbStudentCourse.getCourseId());
                String teacherName = queryTeacherNameById(course.getTeacherId());
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudentCourse(course, tbStudentCourse, teacherName);
                studentCourseList.add(studentCourse);
            }
            return new StudentCourseList(total, pageNum, studentCourseList);
        }
        return null;

    }

    //更新课程成绩
    @Override
    public void updateCourseGrade(CourseGrade courseGrade, String token) {

        TbStudentCourse studentCourse = studentCourseMapper.selectByPrimaryKey(courseGrade.getId());
        if(studentCourse == null){
            throw new RuntimeException("无该学生的课程的课程成绩");
        }
        TbCourse course = queryCourseById(studentCourse.getCourseId());
        if(course == null){
            throw new RuntimeException("无此课程");
        }
        String teacherId = course.getTeacherId();
        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);
        //验证权限
        if(!id.equals(teacherId) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }
        studentCourse.setGrade(courseGrade.getGrade());
        studentCourseMapper.updateByPrimaryKey(studentCourse);

    }

    @Override
    public void deleteStudentCourseById(String studentCourseId, String token) {

        TbStudentCourse studentCourse = studentCourseMapper.selectByPrimaryKey(studentCourseId);
        if(studentCourse == null){
            throw new RuntimeException("无该学生的课程的课程成绩");
        }
        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);
        //验证权限
        if(!id.equals(studentCourse.getUserId()) && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无更新权限，请以管理员或者用户本人身份登录");
        }
        studentCourseMapper.deleteByPrimaryKey(studentCourseId);

    }

    //通过课程id查找课程
    private TbCourse queryCourseById(String id){
        return courseMapper.selectByPrimaryKey(id);
    }
    //通过讲师id查找讲师
    private String queryTeacherNameById(String id){
        return teacherMapper.selectByPrimaryKey(id).getRealName();
    }
}

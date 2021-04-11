package com.haoyu.service.impl;

import com.haoyu.mapper.*;
import com.haoyu.pojo.*;
import com.haoyu.pojo.vo.StudentCourse;
import com.haoyu.pojo.vo.StudentCourseList;
import com.haoyu.service.StudentCourseService;
import com.haoyu.util.IdWorker;
import com.haoyu.util.TokenWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentCourseServiceImpl implements StudentCourseService {

    @Autowired
    private TbStudentMapper studentMapper;
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

        //根据用户id查询数据
        List<TbStudentCourse> tbStudentCourseList= studentCourseMapper.selectByExample(example);
        long total = studentCourseMapper.countByExample(example);
        List<StudentCourse> studentCourseList = new ArrayList<>();

        //判断数据是否存在
        if(tbStudentCourseList != null && tbStudentCourseList.size() != 0){

            for(TbStudentCourse tbStudentCourse : tbStudentCourseList){

                TbCourse course = queryCourseById(tbStudentCourse.getCourseId());
                String teacherName ="";
                if(course.getTeacherId() != null){
                    teacherName = queryTeacherNameById(course.getTeacherId());
                }
                StudentCourse studentCourse = new StudentCourse();
                studentCourse.setStudentCourse(course, tbStudentCourse, teacherName);
                studentCourseList.add(studentCourse);
            }
            return new StudentCourseList(total, pageNum, studentCourseList);
        }
        return null;

    }

    //查找课程下学员信息
    @Override
    public List<TbStudentCourse> queryStudentCourseByCourseId(String courseId, String token) {

        //验证token
        TokenWorker.getRoleFromJWT(token);

        TbStudentCourseExample example = new TbStudentCourseExample();
        TbStudentCourseExample.Criteria criteria = example.createCriteria();
        criteria.andCourseIdEqualTo(courseId);
        List<TbStudentCourse> studentCourseList = studentCourseMapper.selectByExample(example);
        if(studentCourseList != null && studentCourseList.size()>0){
            return studentCourseList;
        }
        return null;
    }

    //更新课程成绩
    @Override
    public void updateCourseGrade(TbStudentCourse courseGrade, String token) {

        TbStudentCourse studentCourse = null;
        TbStudentCourseExample example = new TbStudentCourseExample();
        TbStudentCourseExample.Criteria criteria = example.createCriteria();
        if(StringUtils.isNotBlank(courseGrade.getId())){
            studentCourse = studentCourseMapper.selectByPrimaryKey(courseGrade.getId());
        }
        else if(StringUtils.isNotBlank(courseGrade.getCourseId()) && StringUtils.isNotBlank(courseGrade.getUserId())){
            criteria.andCourseIdEqualTo(courseGrade.getCourseId());
            criteria.andUserIdEqualTo(courseGrade.getUserId());
            List<TbStudentCourse> studentCourseList = studentCourseMapper.selectByExample(example);
            if(studentCourseList != null) studentCourse = studentCourseList.get(0);
        }

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

        //更新课程人数
        TbCourse course = courseMapper.selectByPrimaryKey(studentCourse.getCourseId());
        course.setHeadcount(course.getHeadcount() -1);
        courseMapper.updateByPrimaryKey(course);

    }
    @Override
    public void addStudentCourse(String courseId, String token) {
        //获取token中的id
        String id = TokenWorker.getIdFromJWT(token);
        if(studentMapper.selectByPrimaryKey(id) == null){
            throw new RuntimeException("学员id错误");
        }
        TbCourse course = courseMapper.selectByPrimaryKey(courseId);
        if(course == null){
            throw new RuntimeException("课程id错误");
        }
        TbStudentCourse studentCourse = new TbStudentCourse();
        studentCourse.setId(String.valueOf(idWorker.nextId()));
        studentCourse.setUserId(id);
        studentCourse.setCourseId(courseId);
        studentCourseMapper.insert(studentCourse);

        //更新课程人数
        course.setHeadcount(course.getHeadcount() + 1);
        courseMapper.updateByPrimaryKey(course);
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

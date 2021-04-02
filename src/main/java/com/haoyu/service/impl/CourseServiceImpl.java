package com.haoyu.service.impl;

import com.haoyu.mapper.TbAdminMapper;
import com.haoyu.mapper.TbCourseMapper;
import com.haoyu.mapper.TbTeacherMapper;
import com.haoyu.pojo.TbCourse;
import com.haoyu.pojo.TbCourseExample;
import com.haoyu.pojo.TbStudent;
import com.haoyu.pojo.vo.Course;
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
    TbCourseMapper courseMapper;
    @Autowired
    IdWorker idWorker;

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
        if(teacherMapper.selectByPrimaryKey(id) == null && adminMapper.selectByPrimaryKey(id) ==null){
            throw new RuntimeException("无添加权限，请以管理员或者讲师身份登录");
        }
        if(course == null){
            throw new RuntimeException("课程添加失败");
        }
        course.setId(String.valueOf(idWorker.nextId()));
        course.setHeadcount(0);
        course.setAddDate(new Date());
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
    public CourseList queryCourse(String name, Integer pageNum, Integer pageSize, String token) {
        //验证权限
        if(adminMapper.selectByPrimaryKey(TokenWorker.getIdFromJWT(token)) ==null){
            throw new RuntimeException("无查询权限，请以管理员身份登录");
        }

        TbCourseExample example = new TbCourseExample();
        TbCourseExample.Criteria criteria = example.createCriteria();

        //查询条件
        if(StringUtils.isNotBlank(name)){
            criteria.andCourseNameLike("%"+name+"%");
        }

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

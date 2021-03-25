package com.haoyu.service.impl;

import com.haoyu.mapper.TbCourseMapper;
import com.haoyu.pojo.TbCourse;
import com.haoyu.pojo.TbCourseExample;
import com.haoyu.service.CourseService;
import com.haoyu.util.IdWorker;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class CourseServiceImpl implements CourseService {

    @Autowired
    TbCourseMapper courseMapper;
    @Autowired
    IdWorker idWorker;

    @Override
    public TbCourse addCourse(TbCourse course) {

        if(course == null){
            throw new RuntimeException("课程添加失败");
        }
        course.setId(String.valueOf(idWorker.nextId()));
        course.setAddDate(new Date());
        courseMapper.insert(course);
        return courseMapper.selectByPrimaryKey(course.getId());

    }

    @Override
    public void deleteCourseByCourseId(String courseId) {

        if(StringUtils.isBlank(courseId)
                || courseMapper.deleteByPrimaryKey(courseId) == 0) {
            throw new RuntimeException("课程不存在");
        }
    }

    @Override
    public TbCourse updateCourse(TbCourse course) {

        TbCourse tbCourse = courseMapper.selectByPrimaryKey(course.getId());
        if(tbCourse == null){
            throw new RuntimeException("课程修改失败");
        }
        course.setAddDate(tbCourse.getAddDate());
        courseMapper.updateByPrimaryKey(course);
        return courseMapper.selectByPrimaryKey(course.getId());

    }

    @Override
    public List<TbCourse> queryCourse(String name) {

        TbCourseExample example = new TbCourseExample();
        TbCourseExample.Criteria criteria = example.createCriteria();

        //查询条件
        if(StringUtils.isNotBlank(name)) {
            criteria.andCourseNameLike("%" + name + "%");
        }

        //数据
        List<TbCourse> coursesList = courseMapper.selectByExample(example);
        if(coursesList != null && coursesList.size() > 0){
            return coursesList;
        }
        else {
            return null;
        }
    }

}

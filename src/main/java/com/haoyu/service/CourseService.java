package com.haoyu.service;

import com.haoyu.pojo.TbCourse;

import java.util.List;

public interface CourseService {
    /**
     * 添加课程
     * @param course 课程
     * @return 返回课程对象
     */
    TbCourse addCourse(TbCourse course);

    /**
     * 删除课程
     * @param courseId 课程id
     */
    void deleteCourseByCourseId(String courseId);

    /**
     * 修改课程
     * @param course 课程
     * @return 返回课程对象
     */
    TbCourse updateCourse(TbCourse course);

    /**
     * 查询课程
     * @param name 课程名
     * @return 返回课程列表
     */
    List<TbCourse> queryCourse(String name);
}

package com.haoyu.service;

import com.haoyu.pojo.vo.CourseGrade;
import com.haoyu.pojo.vo.StudentCourseList;

public interface StudentCourseService {
    /**
     * 查询学生课程
     * @param userId
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    StudentCourseList queryStudentCourse(String userId, Integer pageNum, Integer pageSize, String token);

    /**
     * 更新学生课程
     * @param courseGrade
     * @param token
     */
    void updateCourseGrade(CourseGrade courseGrade, String token);

    /**
     * 删除学生课程
     * @param studentCourseId
     * @param token
     */
    void deleteStudentCourseById(String studentCourseId, String token);
}

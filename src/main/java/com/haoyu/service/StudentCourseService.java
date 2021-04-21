package com.haoyu.service;

import com.haoyu.pojo.TbStudentCourse;
import com.haoyu.pojo.vo.StudentCourseList;

import java.util.List;

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
    void updateCourseGrade(TbStudentCourse courseGrade, String token);

    /**
     * 删除学生课程
     * @param studentCourseId
     * @param token
     */
    void deleteStudentCourseById(String studentCourseId, String token);

    /**
     * 选课
     * @param studentCourse
     * @param token
     * @return
     */
    void addStudentCourse(TbStudentCourse studentCourse, String token);

    /**
     * 查找课程下学员信息
     * @param courseId
     * @param token
     * @return
     */
    List<TbStudentCourse> queryStudentCourseByCourseId(String courseId, String token);
}

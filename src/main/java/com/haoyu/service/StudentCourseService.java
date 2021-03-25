package com.haoyu.service;

import com.haoyu.pojo.vo.CourseGrade;
import com.haoyu.pojo.vo.StudentCourseList;

public interface StudentCourseService {
    StudentCourseList queryStudentCourse(String userId, Integer pageNum, Integer pageSize, String token);

    void updateCourseGrade(CourseGrade courseGrade, String token);

    void deleteStudentCourseById(String studentCourseId, String token);
}

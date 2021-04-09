package com.haoyu.pojo.vo;

import com.haoyu.pojo.TbCourse;

public class CourseDetail {

    private TbCourse course;

    private String teacherName;

    private Boolean isTake;

    private Integer grade;

    private String studentCourseId;

    public CourseDetail(TbCourse course, String teacherName, Boolean isTake, Integer grade, String studentCourseId) {
        this.course = course;
        this.teacherName = teacherName;
        this.isTake = isTake;
        this.grade = grade;
        this.studentCourseId = studentCourseId;
    }

    public TbCourse getCourse() {
        return course;
    }

    public void setCourse(TbCourse course) {
        this.course = course;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public Boolean getIsTake() {
        return isTake;
    }

    public void setIsTake(Boolean isTake) {
        this.isTake = isTake;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getStudentCourseId() {
        return studentCourseId;
    }

    public void setStudentCourseId(String studentCourseId) {
        this.studentCourseId = studentCourseId;
    }
}

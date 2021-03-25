package com.haoyu.pojo.vo;

import com.haoyu.pojo.TbCourse;
import com.haoyu.pojo.TbStudentCourse;

public class StudentCourse {

    private String id;

    private String courseId;

    private String courseName;

    private Integer headcount;

    private Integer duration;

    private Integer grade;

    private String introduction;

    private String teacherName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public Integer getHeadcount() {
        return headcount;
    }

    public void setHeadcount(Integer headcount) {
        this.headcount = headcount;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getGrade() {
        return grade;
    }

    public void setGrade(Integer grade) {
        this.grade = grade;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public void setStudentCourse(TbCourse course, TbStudentCourse studentCourse,String teacherName){
        this.id = studentCourse.getId();
        this.courseId = studentCourse.getCourseId();
        this.courseName = course.getCourseName();
        this.headcount = course.getHeadcount();
        this.duration = course.getDuration();
        this.grade = studentCourse.getGrade();
        this.introduction = course.getIntroduction();
        this.teacherName = teacherName;
    }
}

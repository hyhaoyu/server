package com.haoyu.pojo.vo;

import com.haoyu.pojo.TbCategory;
import com.haoyu.pojo.TbCourse;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

public class Course {

    private String id;

    private String courseName;

    private Integer headcount;

    private String picUrl;

    private Integer duration;

    private String introduction;

    private String teacherName;

    private Date addDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
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

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public void setCourse(TbCourse tbCourse, String teacherName){

        this.id = tbCourse.getId();
        this.courseName = tbCourse.getCourseName();
        this.headcount = tbCourse.getHeadcount();
        this.picUrl = tbCourse.getPicUrl();
        this.duration = tbCourse.getDuration();
        this.introduction = tbCourse.getIntroduction();
        this.teacherName = teacherName;
        this.addDate = tbCourse.getAddDate();
    }
}

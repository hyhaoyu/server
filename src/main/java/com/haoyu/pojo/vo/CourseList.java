package com.haoyu.pojo.vo;

import java.util.List;

public class CourseList {

    private long total;
    private Integer pageNum;
    private List<Course> courseList;

    public CourseList(long total, Integer pageNum, List<Course> courseList) {
        this.total = total;
        this.pageNum = pageNum;
        this.courseList = courseList;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public List<Course> getCourseList() {
        return courseList;
    }

    public void setCourseList(List<Course> courseList) {
        this.courseList = courseList;
    }
}

package com.haoyu.pojo.vo;

import java.util.List;

public class StudentList {

    private long total;
    private Integer pageNum;
    private List<Student> userList;

    public StudentList(long total, Integer pageNum, List<Student> userList) {
        this.total = total;
        this.pageNum = pageNum;
        this.userList = userList;
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

    public List<Student> getUserList() {
        return userList;
    }

    public void setUserList(List<Student> userList) {
        this.userList = userList;
    }
}

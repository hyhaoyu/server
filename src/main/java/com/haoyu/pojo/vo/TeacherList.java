package com.haoyu.pojo.vo;

import java.util.List;

public class TeacherList {

    private long total;
    private Integer pageNum;
    private List<Teacher> userList;

    public TeacherList(long total, Integer pageNum, List<Teacher> userList) {
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

    public List<Teacher> getUserList() {
        return userList;
    }

    public void setUserList(List<Teacher> userList) {
        this.userList = userList;
    }
}

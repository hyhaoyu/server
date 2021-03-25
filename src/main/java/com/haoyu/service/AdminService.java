package com.haoyu.service;

import com.haoyu.pojo.TbAdmin;
import com.haoyu.pojo.vo.Admin;

public interface AdminService {
    /**
     * 管理员登录
     * @param username 管理员用户名
     * @param password 密码
     * @return 返回管理员数据
     */
    Admin login(String username, String password);

    /**
     * 管理员注册
     * @param admin
     */
    void register(TbAdmin admin);
}

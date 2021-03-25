package com.haoyu.service;

import com.haoyu.pojo.TbTeacher;
import com.haoyu.pojo.vo.Teacher;
import com.haoyu.pojo.vo.TeacherList;

public interface TeacherService {
    /**
     * 登录
     * @param username 用户名
     * @param password 密码
     * @return 返回用户数据
     */
    Teacher login(String username, String password);

    /**
     * 注册
     * @param teacher
     */
    void register(TbTeacher teacher);

    /**
     * 修改
     * @param teacher
     */
    void updateTeacher(TbTeacher teacher, String token);

    /**
     * 查询讲师
     * @param name
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    TeacherList queryTeacher(String name, Integer pageNum, Integer pageSize, String token);

    /**
     * 删除讲师
     * @param teacherId
     * @param token
     */
    void deleteTeacherByTeacherId(String teacherId, String token);
}

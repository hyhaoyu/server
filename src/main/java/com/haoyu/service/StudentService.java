package com.haoyu.service;

import com.haoyu.pojo.TbStudent;
import com.haoyu.pojo.vo.Image;
import com.haoyu.pojo.vo.Student;
import com.haoyu.pojo.vo.StudentList;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StudentService {

    /**
     * 登录验证
     * @param username 用户名
     * @param password 密码
     * @return 返回用户数据
     */
    Student login(String username, String password);

    /**
     * 注册用户，将用户信息保存到数据库
     * 如果抛出异常注册失败，反之成功
     * @param student
     */
    void register(TbStudent student);

    /**
     * 更新用户信息
     * @param student
     */
    void updateStudent(TbStudent student, String token);

    /**
     * 查看用户信息
     * @param name
     * @param courseId
     * @param pageNum
     * @param pageSize
     * @param token
     * @return
     */
    StudentList queryStudent(String name, String courseId, Integer pageNum, Integer pageSize, String token);

    /**
     * 删除用户
     * @param studentId
     * @param token
     */
    void deleteStudentByStudentId(String studentId, String token);

    /**
     * 上传用户头像
     * @param studentId
     * @param imgFile
     * @param token
     */
    Image updateStudentAvatar(String studentId, MultipartFile imgFile, String token) throws IOException;
}

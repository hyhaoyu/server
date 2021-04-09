package com.haoyu.service;

import com.haoyu.pojo.TbCourse;
import com.haoyu.pojo.vo.CourseDetail;
import com.haoyu.pojo.vo.CourseList;
import com.haoyu.pojo.vo.Image;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface CourseService {
    /**
     * 添加课程
     * @param course 课程
     * @return 返回课程对象
     */
    TbCourse addCourse(TbCourse course, String token);

    /**
     * 删除课程
     * @param courseId 课程id
     */
    void deleteCourseByCourseId(String courseId, String token);

    /**
     * 修改课程
     * @param course 课程
     * @return 返回课程对象
     */
    void updateCourse(TbCourse course, String token);

    /**
     * 查询课程
     * @param name
     * @param pageNum
     * @param pageSize
     * @param token
     * @return 返回课程列表
     */
    CourseList queryCourse(String name, String teacherId, Integer pageNum, Integer pageSize, String token);

    /**
     * 上传课程图片
     * @param courseId
     * @param imgFile
     * @param token
     * @return
     */
    Image updateCoursePicture(String courseId, MultipartFile imgFile, String token) throws IOException;

    /**
     * 查询课程详细信息
     * @param courseId
     * @param token
     * @return
     */
    CourseDetail queryCourseById(String courseId, String token);
}

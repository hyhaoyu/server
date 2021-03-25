package com.haoyu.mapper;

import com.haoyu.pojo.TbStudentCourse;
import com.haoyu.pojo.TbStudentCourseExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbStudentCourseMapper {
    long countByExample(TbStudentCourseExample example);

    int deleteByExample(TbStudentCourseExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbStudentCourse record);

    int insertSelective(TbStudentCourse record);

    List<TbStudentCourse> selectByExample(TbStudentCourseExample example);

    TbStudentCourse selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbStudentCourse record, @Param("example") TbStudentCourseExample example);

    int updateByExample(@Param("record") TbStudentCourse record, @Param("example") TbStudentCourseExample example);

    int updateByPrimaryKeySelective(TbStudentCourse record);

    int updateByPrimaryKey(TbStudentCourse record);
}
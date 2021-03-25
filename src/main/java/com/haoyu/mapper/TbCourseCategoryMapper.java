package com.haoyu.mapper;

import com.haoyu.pojo.TbCourseCategory;
import com.haoyu.pojo.TbCourseCategoryExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbCourseCategoryMapper {
    long countByExample(TbCourseCategoryExample example);

    int deleteByExample(TbCourseCategoryExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbCourseCategory record);

    int insertSelective(TbCourseCategory record);

    List<TbCourseCategory> selectByExample(TbCourseCategoryExample example);

    TbCourseCategory selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbCourseCategory record, @Param("example") TbCourseCategoryExample example);

    int updateByExample(@Param("record") TbCourseCategory record, @Param("example") TbCourseCategoryExample example);

    int updateByPrimaryKeySelective(TbCourseCategory record);

    int updateByPrimaryKey(TbCourseCategory record);
}
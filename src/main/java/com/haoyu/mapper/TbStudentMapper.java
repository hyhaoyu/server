package com.haoyu.mapper;

import com.haoyu.pojo.TbStudent;
import com.haoyu.pojo.TbStudentExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface TbStudentMapper {
    long countByExample(TbStudentExample example);

    int deleteByExample(TbStudentExample example);

    int deleteByPrimaryKey(String id);

    int insert(TbStudent record);

    int insertSelective(TbStudent record);

    List<TbStudent> selectByExample(TbStudentExample example);

    //自定义sql映射
    long countByMyExample(TbStudentExample example);
    List<TbStudent> selectByMyExample(TbStudentExample example);

    TbStudent selectByPrimaryKey(String id);

    int updateByExampleSelective(@Param("record") TbStudent record, @Param("example") TbStudentExample example);

    int updateByExample(@Param("record") TbStudent record, @Param("example") TbStudentExample example);

    int updateByPrimaryKeySelective(TbStudent record);

    int updateByPrimaryKey(TbStudent record);
}
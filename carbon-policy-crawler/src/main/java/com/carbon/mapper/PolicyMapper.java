package com.carbon.mapper;

/**
 * @author zhangbo $
 * @title $
 * @description $
 * @updateTime $ 11:55$ $
 * @throws $
 */
import com.carbon.entity.Policy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

import com.carbon.entity.Policy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PolicyMapper {

    // 1. 你原来的方法，保留即可
    List<Policy> selectPolicyList(@Param("keyword") String keyword, @Param("type") String type);
    int insertPolicy(Policy policy);
    Policy selectById(@Param("id") Integer id);

    // 2. 列表页专用：查询所有
    List<Policy> selectAll();
    Policy selectByTitle(@Param("title") String title);
}
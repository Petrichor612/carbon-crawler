package com.carbon.mapper;

/**
 * @author zhangbo $
 * @title $
 * @description $
 * @updateTime $ 11:55$ $
 * @throws $
 */
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.carbon.entity.Policy;
import org.apache.ibatis.annotations.*;

import java.util.List;

import com.carbon.entity.Policy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface PolicyMapper extends BaseMapper<Policy> {

    // 1. 你原来的方法，保留即可
    List<Policy> selectPolicyList(@Param("keyword") String keyword, @Param("type") String type);
    int insertPolicy(Policy policy);
    Policy selectById(@Param("id") Integer id);

    // 2. 列表页专用：查询所有
    List<Policy> selectAll();
    Policy selectByTitle(@Param("title") String title);

    IPage<Policy> selectPolicyPage(IPage<Policy> page);
}
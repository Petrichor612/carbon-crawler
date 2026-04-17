package com.carbon.service;

/**
 * @author zhangbo $
 * @title $
 * @description $
 * @updateTime $ 11:56$ $
 * @throws $
 */
import com.carbon.entity.Policy;
import java.util.List;

public interface PolicyService {
    List<Policy> getPolicyList(String keyword, String type);
    Policy getPolicyById(Integer id);
    void savePolicy(Policy policy);
    // 查询所有政策
    List<Policy> list();
    // 根据ID查询单条（详情页用）
    Policy getById(Integer id);

}
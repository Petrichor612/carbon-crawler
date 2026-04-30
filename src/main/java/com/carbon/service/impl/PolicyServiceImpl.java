package com.carbon.service.impl;

/**
 * @author zhangbo $
 * @title $
 * @description $
 * @updateTime $ 11:56$ $
 * @throws $
 */
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.carbon.entity.Policy;
import com.carbon.mapper.PolicyMapper;
import com.carbon.service.PolicyService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PolicyServiceImpl implements PolicyService {

    @Resource
    private PolicyMapper policyMapper;

    @Override
    public List<Policy> getPolicyList(String keyword, String type) {
        // 🔥 修复：返回空列表而不是 null
        List<Policy> list = policyMapper.selectPolicyList(keyword, type);
        return list == null ? List.of() : list;
    }

    @Override
    public Policy getPolicyById(Integer id) {
        return policyMapper.selectById(id);
    }

    @Override
    public void savePolicy(Policy policy) {
        Policy exist = policyMapper.selectByTitle(policy.getTitle());
        if (exist == null) {
            policyMapper.insertPolicy(policy);
            System.out.println("✅ 已保存：" + policy.getTitle());
        } else {
            System.out.println("ℹ️ 已存在，跳过：" + policy.getTitle());
        }
    }



    @Override
    public List<Policy> list() {
        return null;
    }

    @Override
    public Policy getById(Integer id) {
        return null;
    }

    @Override
    public IPage<Policy> getPolicyPage(int pageNum, int pageSize) {
        Page<Policy> page = new Page<>(pageNum, pageSize);
        // 调用 mapper 方法
        return policyMapper.selectPolicyPage(page);
    }
    

}
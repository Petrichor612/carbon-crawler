package com.carbon.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carbon.entity.Policy;
import com.carbon.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PolicyController {


    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 首页（已修复空指针）
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "type", required = false, defaultValue = "policy") String type) {

        // 🔥 核心修复：确保永远不会返回 null
        List<Policy> policyList = policyService.getPolicyList(keyword, type);
        if (policyList == null) {
            policyList = List.of(); // 空列表，不会空指针
        }

        model.addAttribute("policyList", policyList);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // 原有接口
    @GetMapping("/view")
    public Map<String, Object> viewPolicy(@RequestParam("id") Integer id) {
        Map<String, Object> result = new HashMap<>();
        Policy policy = policyService.getPolicyById(id);
        result.put("code", 200);
        result.put("data", policy);
        return result;
    }

    // 详情页
    @GetMapping("/policy/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Policy policy = policyService.getPolicyById(id);
        model.addAttribute("policy", policy);
        return "detail";
    }

    @GetMapping("/policies")
    public IPage<Policy> getPolicies(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "6") int pageSize) {
        return policyService.getPolicyPage(pageNum, pageSize);
    }

}
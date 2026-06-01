package com.carbon.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.carbon.entity.Policy;
import com.carbon.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 首页：正常渲染模板
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(value = "keyword", required = false) String keyword,
                        @RequestParam(value = "type", required = false, defaultValue = "policy") String type) {

        List<Policy> policyList = policyService.getPolicyList(keyword, type);
        if (policyList == null) {
            policyList = List.of();
        }
        model.addAttribute("policyList", policyList);
        model.addAttribute("keyword", keyword);
        return "index";
    }

    // 详情页：正常渲染模板
    @GetMapping("/policy/detail/{id}")
    public String detail(@PathVariable Integer id, Model model) {
        Policy policy = policyService.getPolicyById(id);
        model.addAttribute("policy", policy);
        return "detail";
    }

    // 分页接口：加上 @ResponseBody，直接返回 JSON
    @ResponseBody
    @GetMapping("/policy/page")
    public IPage<Policy> listPage(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "6") int pageSize,
            @RequestParam(required = false) String keyword) {
        return policyService.listByPage(pageNum, pageSize, keyword);
    }

    // 其他接口也一样，需要返回 JSON 的加 @ResponseBody
    @ResponseBody
    @GetMapping("/view")
    public Map<String, Object> viewPolicy(@RequestParam("id") Integer id) {
        Map<String, Object> result = new HashMap<>();
        Policy policy = policyService.getPolicyById(id);
        result.put("code", 200);
        result.put("data", policy);
        return result;
    }

    @ResponseBody
    @GetMapping("/policies")
    public IPage<Policy> getPolicies(
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "6") int pageSize) {
        return policyService.getPolicyPage(pageNum, pageSize);
    }
}
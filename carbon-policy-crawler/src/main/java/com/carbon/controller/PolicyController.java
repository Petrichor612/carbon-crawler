package com.carbon.controller;

import com.carbon.entity.Policy;
import com.carbon.service.PolicyService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PolicyController {

    private final PolicyService policyService;

    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }

    // 首页 - 不会报错
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 测试接口
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "Project is running successfully!";
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
    
}

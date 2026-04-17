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

    // 首页（已修复空指针）
    @GetMapping("/")
public String index() {
    return "✅ 首页正常！  
    可以访问的页面：  
    1. /test → 测试接口  
    2. /policy/list → 查看政策列表  
    3. /policy/start → 开始爬数据";
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

    @GetMapping("/test")
@ResponseBody
public String test() {
    return "✅ 项目运行成功！接口正常！";
}
    
}

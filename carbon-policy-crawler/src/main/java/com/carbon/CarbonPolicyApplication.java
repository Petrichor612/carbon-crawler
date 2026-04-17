package com.carbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CarbonPolicyCrawlerApplication {

    // 直接把首页写在启动类里！100% 被加载！
    @GetMapping("/")
    public String home() {
        return "✅ 项目启动成功！公网运行正常！";
    }

    public static void main(String[] args) {
        SpringApplication.run(CarbonPolicyCrawlerApplication.class, args);
    }
}

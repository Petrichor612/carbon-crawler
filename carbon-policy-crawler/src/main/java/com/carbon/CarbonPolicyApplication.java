package com.carbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class CarbonPolicyApplication {

    @GetMapping("/")
    public String home() {
        return "✅ 项目部署成功！运行正常！";
    }

    public static void main(String[] args) {
        SpringApplication.run(CarbonPolicyApplication.class, args);
    }
}

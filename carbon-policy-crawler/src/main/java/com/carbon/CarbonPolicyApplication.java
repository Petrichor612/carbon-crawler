package com.carbon;

/**
 * @author zhangbo $
 * @title $
 * @description $
 * @updateTime $ 11:53$ $
 * @throws $
 */
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// @EnableScheduling
public class CarbonPolicyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarbonPolicyApplication.class, args);
    }
}

package com.carbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableScheduling
public class CarbonPolicyApplication {

    public static void main(String[] args) {
        SpringApplication.run(CarbonPolicyApplication.class, args);
    }
}

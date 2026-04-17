package com.carbon;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

// 关键：只扫描 controller，其他全部不加载！
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
@ComponentScan(basePackages = "com.carbon.controller") // 只加载接口！
public class CarbonPolicyApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarbonPolicyApplication.class, args);
    }
}

package com.jiangnan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@MapperScan("com.jiangnan.mapper")
@EnableScheduling//定时任务
@EnableSwagger2
public class LJNBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LJNBlogApplication.class, args);
    }
}

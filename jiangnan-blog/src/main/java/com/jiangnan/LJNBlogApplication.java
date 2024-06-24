package com.jiangnan;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jiangnan.mapper")
public class LJNBlogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LJNBlogApplication.class, args);
    }
}

package com.edu.seiryo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.edu.seiryo.admin.mapper") 
public class SupermarketProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(SupermarketProjectApplication.class, args);
        System.out.println("项目启动成功！");
    }

}

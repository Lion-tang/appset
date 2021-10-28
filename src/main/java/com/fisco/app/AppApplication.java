package com.fisco.app;


import com.fisco.app.entity.LoadConfig;
import com.fisco.app.utils.ElasticSearchClient;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
@MapperScan("com.fisco.app.mapper")
public class AppApplication {

    public static void main(String[] args) throws IOException {

        SpringApplication.run(AppApplication.class, args);

    }
}

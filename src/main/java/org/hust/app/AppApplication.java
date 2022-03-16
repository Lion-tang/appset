package org.hust.app;



import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;

@SpringBootApplication
@MapperScan("org.hust.app.mapper")
public class AppApplication {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(AppApplication.class, args);

    }
}

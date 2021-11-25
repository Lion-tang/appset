package org.hust.app.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public PaginationInterceptor paginationInterceptper() {
        return new PaginationInterceptor();
    }
}

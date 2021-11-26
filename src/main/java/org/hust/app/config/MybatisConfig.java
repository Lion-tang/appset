package org.hust.app.config;


import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
@Date 2021/11/26
@Description 分页拦截器
@author zltang
**/
@Configuration
public class MybatisConfig {
    @Bean
    public PaginationInterceptor paginationInterceptper() {
        return new PaginationInterceptor();
    }
}

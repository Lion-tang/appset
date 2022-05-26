package org.hust.app.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
@Date 2021/11/26
@Description 权限访问控制设置
@author zltang
**/
@EnableWebSecurity
@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new HashPasswordEncoder());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
            "/images/**",
            "/layuiadmin/**",
            "/files/**",
            "/register",
            "/download",
            "/home/timeline",
            "/home/download",
            "/queryRecord",
            "/queryDetail",
            "/coins"
            );
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/home/**").hasAnyRole("USER", "ADMIN", "COMMITEE")
                .antMatchers("/set/**").hasAnyRole("USER", "ADMIN", "COMMITEE")
                .antMatchers("/index").hasAnyRole("USER", "ADMIN", "COMMITEE")
                .antMatchers("/user/administrators/list").hasRole("COMMITEE")
                .antMatchers("/upload").hasAnyRole("USER", "ADMIN", "COMMITEE")
                .antMatchers("/**").hasAnyRole("ADMIN", "COMMITEE")
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin")
                .successHandler(new LoginSuccessHandler())
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .logoutSuccessUrl("/login")
                .deleteCookies()  //deleteCookies 用来清除 cookie。
                .invalidateHttpSession(true)
                .clearAuthentication(true) //clearAuthentication 和 invalidateHttpSession 分别表示清除认证信息和使 HttpSession 失效，默认可以不用配置，默认就会清除。
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .csrf()
                .disable()
                .rememberMe()

        ;


    }
}

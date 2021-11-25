package org.hust.app.utils;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String role = null;
        //项目根地址
        String path = request.getContextPath();
        //主机+端口号+项目根路径
        String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        //权限是我手动赋予的，每一个认证成功的用户只有一个权限
        Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            //所以authorities的size为1，直接赋予
            role = authority.getAuthority();
        }
        //通过不同的角色转到不同的页面
//        if ("ROLE_ADMIN".equals(role) || "ROLE_COMMITEE".equals(role)) {
        //管理员和链委员请求管理员controller接口页面
            response.sendRedirect(basePath + "index");
//        } else {
//        //用户请求用户controller接口页面
//            response.sendRedirect(basePath + "home/demo1");
//        }

    }
}

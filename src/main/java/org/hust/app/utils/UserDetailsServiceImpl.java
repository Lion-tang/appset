package org.hust.app.utils;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hust.app.entity.Customer;
import org.hust.app.mapper.CustomerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerMapper customerMapper;

    @Autowired
    MyPasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", userName);
        Customer customer = customerMapper.selectOne(wrapper);
        if (customer == null) {
            return null;
        }
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        String roleName = customer.getRole();
        if ("admin".equals(roleName)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } else if ("commitee".equals(roleName)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_COMMITEE"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        org.springframework.security.core.userdetails.User userdetail = new org.springframework.security.core.userdetails.User(customer.getUserName(), passwordEncoder.encode(customer.getPassword()), authorities);
//        System.out.println("登录信息："+user.getUserName()+"   "+passwordEncoder.encode(user.getPassword())+"  "+userdetail.getAuthorities());//后台调试输出登录者用户名、哈希密码、角色
        return userdetail;
    }
}

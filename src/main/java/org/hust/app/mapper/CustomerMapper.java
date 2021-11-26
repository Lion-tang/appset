package org.hust.app.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.hust.app.entity.Customer;
import org.springframework.stereotype.Repository;

/**
@Date 2021/11/26
@Description 用户登录信息相关的CRUD
@author zltang
**/

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {


}

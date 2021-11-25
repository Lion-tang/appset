package org.hust.app.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.hust.app.entity.Customer;
import org.hust.app.entity.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerMapper extends BaseMapper<Customer> {


}

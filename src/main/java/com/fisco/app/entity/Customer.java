package com.fisco.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class Customer {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String userName;
    private String password;
    private String role;
    private String address;
}

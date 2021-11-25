package org.hust.app.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "user")
public class Customer {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableField(value = "user_name")
    private String userName;
    private String password;
    private String role;
    private String address;
    private String locate;
}

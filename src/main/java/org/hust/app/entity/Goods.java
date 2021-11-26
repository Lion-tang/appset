package org.hust.app.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
@Date 2021/11/26
@Description 商品信息实体类
@author zltang
**/


@Data
@TableName(value = "g1_DetailCRUD_insert_method0")//默认对应的数据库表
public class Goods{
    private String uid;
    private String hash;
    private String description;


}

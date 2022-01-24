package org.hust.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author zltang
 * @Date 2021/12/5
 * @Description 整车报税测试类
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    private  String uid;
    private String operator;
    private String bsState;
    private String alarm;
    private String engineNum;
    private String motorNum;
    private String prodName;
    private String modelVersion;
    private String conf;
}

package com.fisco.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ElasticSearchConfig {
    private String host;
    private int port;
    private String record_index;
    private String detail_index;


}

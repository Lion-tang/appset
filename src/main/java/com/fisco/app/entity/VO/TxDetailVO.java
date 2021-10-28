package com.fisco.app.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxDetailVO {
    private String uid;
    private String hash;
    private String desc;
    private String time;

}

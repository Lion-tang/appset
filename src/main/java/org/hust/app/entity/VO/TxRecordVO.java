package org.hust.app.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Comparator;

/**
@Date 2021/11/26
@Description 主链(简要信息)属性实体类
@author zltang
**/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxRecordVO implements Comparable {

    private String uid;
    private String attr;
    private String time;


    @Override
    public int compareTo(Object o) {
        TxRecordVO txRecordVO = (TxRecordVO) o;
        int ret = this.time.compareTo(txRecordVO.getTime());
        return ret;
    }
}

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
public class TxRecordVO implements Comparator {
    private String locate;
    private String uid;
    private String txHash;
    private String time;

    @Override
    public int compare(Object o1, Object o2) {
        String time1 = ((TxRecordVO) o1).getTime();
        String time2 = ((TxRecordVO) o2).getTime();
        return time1.compareTo(time2);
    }
}

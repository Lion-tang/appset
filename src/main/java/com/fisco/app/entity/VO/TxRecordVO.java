package com.fisco.app.entity.VO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TxRecordVO {
    private String blockTimeStamp;
    private String blockHeight;
    private String txHash;
    private String txFrom;
    private String txTo;

}

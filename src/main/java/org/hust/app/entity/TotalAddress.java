package org.hust.app.entity;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Data
public class TotalAddress {
    @Value("${address.account}")
    private  String account;
    @Value("${address.contract}")
    private  String contract;
    public static final String ERC20 = "ERC20";
    public static final String DETAIL_CONTRACT_NAME = "Detail";
    public static final String RECORD_CONTRACT_NAME = "Record";

    public static String accountFilePath(String accountFileName){
        return "account/gm/" + accountFileName + ".pem";
    }
}

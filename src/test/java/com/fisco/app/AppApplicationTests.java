package com.fisco.app;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fisco.app.mapper.CustomerMapper;
import com.fisco.app.utils.ShaUtils;
import com.fisco.app.utils.SpringUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@MapperScan("com.fisco.app.mapper")
class AppApplicationTests {
    @Autowired
    CustomerMapper customerMapper;

    @Test
    void testFIle(){
        customerMapper.selectList(null).forEach(System.out::println);
    }

    @Test
    void testHash(){
        System.out.println(ShaUtils.code("123456",ShaUtils.SHA_1));
    }

    @Test
    void testlis(){
        BcosSDK sdk = SpringUtils.getBean("bcosSDK");
        Client client = sdk.getClient(1);
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
        cryptoKeyPair.storeKeyPairWithPemFormat();

    }
}

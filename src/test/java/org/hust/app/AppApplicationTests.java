package org.hust.app;





import org.hust.app.mapper.CustomerMapper;
import org.hust.app.utils.SpringUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


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
    void testCRUD(){

    }



}

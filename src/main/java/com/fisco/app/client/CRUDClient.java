package com.fisco.app.client;

import com.fisco.app.common.CommonClient;
import com.fisco.app.contract.TestCRUD;
import com.fisco.app.utils.SpringUtils;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Classuid CRUDClient
 * @Description sdk客户端，可以不使用ApplicationRunner，这里是让spring容器启动部署合约
 * @Date 2021/3/25 21:45
 * @Created by zyt
 */
@Service
public class CRUDClient extends CommonClient implements ApplicationRunner {

    public static final Logger logger = LoggerFactory.getLogger(CRUDClient.class.getName());


    public void insert(String uid, String hash, String desc,String mapKey) {

        TestCRUD testCRUD = (TestCRUD) getContractMap().get(mapKey);
        TransactionReceipt receipt = testCRUD.insert(uid, hash, desc);
        logger.info("调用CRUDClient的insert方法");
        logger.info("结果：{}", receipt);

    }

    public Tuple3 query(String uid,String mapKey) throws ContractException {

        TestCRUD testCRUD = (TestCRUD) getContractMap().get(mapKey);
        Tuple3<List<String>, List<String>, List<String>> getValue = testCRUD.select(uid);
        logger.info("调用CRUDClient的query方法");
        logger.info("结果：{}", getValue);
        return getValue;

    }

    public void edit(String uid, String hash, String desc,String mapKey) {

        TestCRUD testCRUD = (TestCRUD) getContractMap().get(mapKey);
        TransactionReceipt receipt = testCRUD.update(uid, hash, desc);
        logger.info("调用CRUDClient的edit方法");
        logger.info("结果：{}", receipt);

    }

    @Override
    public void remove(String uid, String mapKey) {

        TestCRUD testCRUD = (TestCRUD) getContractMap().get(mapKey);
        TransactionReceipt receipt = testCRUD.remove(uid);
        logger.info("调用CRUDClient的remove方法");
        logger.info("结果：{}", receipt);


    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //使用固定账户部署合约
//        BcosSDK sdk = SpringUtils.getBean("bcosSDK");
//        deploy("TestCRUD", TestCRUD.class, sdk, 1);
//        TestCRUD testCRUD = (TestCRUD) getContractMap().get("TestCRUD");
        //加载存储的账户，防止每次生成账户和智能合约地址


        TestCRUD testCRUD = getTestCRUDFromAccount(1, "0x5a91ed032b2b541ee705d9d770b30bab5fc596c3.pem" );
        Map<String, Object> map = getContractMap();
        map.put("TestCRUD", testCRUD);
        logger.info("调用CRUDClient的加载合约地址{}方法",testCRUD);
    }

    public TestCRUD getTestCRUDFromAccount(Integer groupNum,String accountFile) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
        Client client = sdk.getClient(groupNum);
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        cryptoSuite.loadAccount("pem", "account/ecdsa/" + accountFile, null);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        //合约地址使用固定的测试地址
        return TestCRUD.load("0xee199428a3a825caa546246bda8b2d28b0671010", client, cryptoKeyPair);
    }

}

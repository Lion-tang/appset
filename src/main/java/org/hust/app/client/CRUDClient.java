package org.hust.app.client;

import org.hust.app.common.CommonClient;
import org.hust.app.contract.DetailCRUD;
import org.hust.app.contract.RecordCRUD;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple3;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
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
    public static final int detailChoice = 1;
    public static final int recordChoice = 2;


    /**
    @Date 2021/11/11
    @Description 以下crud都是为了postman测试， dapp使用主要通过有用户账户构造的DetailCRUD和RecordCRUD发起交易
    @author Lyontang
    **/

    public void insertDetail(String uid, String attr) {

        DetailCRUD detailCRUD= (DetailCRUD) getContractMap().get("Detail");
        TransactionReceipt receipt = detailCRUD.insert(uid , attr);
        logger.info("调用DetailClient的insert方法");
        logger.info("结果：{}", receipt);
    }

    public void insertRecord(String main_tx_hash, String uid, String locate) {
        RecordCRUD recordCRUD = ((RecordCRUD)getCRUDFromAccount(1, "0x67611f776c55565e921b00a4a23c65dc4e9f67df", CRUDClient.recordChoice));
        TransactionReceipt receipt = recordCRUD.insert(main_tx_hash, uid, locate);
        logger.info("调用RecordClient的insert方法");
        logger.info("结果：{}", receipt);
    }



    public  Tuple3 queryRecord(String uid) throws ContractException {
        RecordCRUD recordCRUD = ((RecordCRUD) getContractMap().get("Record"));
        Tuple3<List<String>, List<String>, List<String>> getValue = recordCRUD.select(uid);
        logger.info("调用RecordClient的query方法");
        logger.info("结果:{}", getValue);
        return getValue;
    }



    @Override
    public void run(ApplicationArguments args) throws Exception {
        //使用固定账户部署合约
//        BcosSDK sdk = SpringUtils.getBean("bcosSDK");
//        deploy("TestCRUD", TestCRUD.class, sdk, 1);
//        TestCRUD testCRUD = (TestCRUD) getContractMap().get("TestCRUD");
        //加载存储的账户，防止每次生成账户和智能合约地址
        DetailCRUD detailCRUD = ((DetailCRUD) getCRUDFromAccount(2, "0x67611f776c55565e921b00a4a23c65dc4e9f67df", detailChoice));
        Map<String, Object> map = getContractMap();
        map.put("Detail", detailCRUD);
        logger.info("调用CRUDClient的加载合约地址{}方法");
    }


    public Object getCRUDFromAccount(Integer groupNum, String accountFile, int contractChoice) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
        Client client = sdk.getClient(groupNum);
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        cryptoSuite.loadAccount("pem", "account/gm/" + accountFile + ".pem", null);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        //合约地址使用固定的测试地址
        if (contractChoice == detailChoice)
            return DetailCRUD.load("0xa9a48424cdfd1da0c4d90244d8a91263edcea3b3", client, cryptoKeyPair);
        else return RecordCRUD.load("0xcdeb126edf8133f7b4b7a82b51895afac244e524", client, cryptoKeyPair);
    }




}

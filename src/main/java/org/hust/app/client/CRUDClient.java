package org.hust.app.client;


import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.hust.app.common.CommonClient;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.hust.app.contract.DetailCRUD;
import org.hust.app.contract.RecordCRUD;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @Classuid CRUDClient
 * @Description sdk客户端，可以不使用ApplicationRunner，这里是让spring容器启动部署合约
 * @Date 2021/3/25 21:45
 * @Created by zyt
 */
@Service
public class CRUDClient extends CommonClient implements ApplicationRunner {

    public static final Logger logger = LoggerFactory.getLogger(CRUDClient.class.getName());

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

//    public void insertRecord(String main_tx_hash, String uid, String locate) {
//        RecordCRUD recordCRUD = (RecordCRUD) getContract("Record");
//        if(recordCRUD == null){
//            logger.warn("没有部署的合约,请先部署合约");
//            return;
//        }
//        TransactionReceipt receipt = recordCRUD.insert(main_tx_hash, uid, locate);
//        logger.info("调用RecordClient的insert方法");
//        logger.info("结果：{}", receipt);
//    }

    public Tuple2<List<String>, List<String>> query(String name) throws ContractException {

        DetailCRUD detailCRUD = (DetailCRUD) getContractMap().get("TestCRUD");
        Tuple2<List<String>, List<String>> getValue = detailCRUD.select(name);
        logger.info("调用CRUDClient的query方法");
        logger.info("结果：{}", getValue);
        return getValue;

    }





    @Override
    public void run(ApplicationArguments args) throws Exception {
        //使用固定账户部署合约
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
        deploy("Detail", DetailCRUD.class, sdk, 2);
        deploy("Record", RecordCRUD.class, sdk, 1);

    }

    @SuppressWarnings("unchecked")
    /***
    @Date 2022/3/7
    @Description 重载CommonClient的deploy方法，可以根据群组号，账户，合约类型部署拿到sdk
    @author zltang
    ***/
    public <T> void deploy(String contractName, Class<T> clazz, Integer groupNum, String accountFile) {
        if (getContract(contractName) != null) {
            logger.info("已有部署的合约,可以直接使用");
            return;
        }
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
        Client client = sdk.getClient(groupNum);
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        cryptoSuite.loadAccount("pem", "account/gm/" + accountFile + ".pem", null);//加载已有账户pem文件
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();//获取当前账户，若未加载，则随机生成
//        cryptoKeyPair.storeKeyPairWithPemFormat();//存储账户
        //合约地址使用固定的测试地址
        Method method = null;
        try {
            method = clazz.getMethod("deploy", Client.class, CryptoKeyPair.class);
            T result = (T)method.invoke(null, new Object[]{client, cryptoKeyPair});
            getContractMap().put(contractName, result);

        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.info("调用CRUDClient的部署合约地址方法");
    }




}

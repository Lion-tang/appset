package org.hust.app.common;


import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.hust.app.entity.TotalAddress;
import org.hust.app.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zltang
 * @Description:此类提供一个发布合约的方法，并提供了基本实现方法，可以继承此类实现自己的方法
 * @Date: Created in 16:29 2021/1/20
 */
public abstract class CommonClient {

    public static final Logger logger = LoggerFactory.getLogger(CommonClient.class.getName());

    public CommonClient() {
    }

    private Map<String,Object> contractMap = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> void deploy(String contractName, Class<T> clazz,BcosSDK sdk,Integer groupId) throws ContractException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // 为群组初始化client
        Client client = sdk.getClient(groupId);
        // 向群组部署合约

        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.SM_TYPE);//随机生成账户
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        // cryptoKeyPair.storeKeyPairWithPemFormat();//存储账户
        /*** 无需随机生成账户，加载已有账户文件
        //cryptoSuite cryptoSuite = client.getCryptoSuite();
        //cryptoSuite.loadAccount("pem","account/ecdsa/0x5a91ed032b2b541ee705d9d770b30bab5fc596c3.pem",null);
        //CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        ***/

        Method method = clazz.getMethod("deploy", Client.class,CryptoKeyPair.class);
        T result = (T) method.invoke(null,new Object[]{client,cryptoKeyPair});
        logger.info("执行CommonClient的deploy方法");
        logger.info("部署合约成功:{}",result);
        contractMap.put(contractName,result);
    }

    @SuppressWarnings("unchecked")
    public <T> T load(String contractName,String contractAddress, String accountAddress, Class<T> clazz, Integer groupId) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(contractMap.containsKey(contractName))
            return (T) contractMap.get(contractName);
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
        BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
        Client client = sdk.getClient(groupId);
        CryptoSuite cryptoSuite = client.getCryptoSuite();
        cryptoSuite.loadAccount("pem", TotalAddress.accountFilePath(accountAddress), null);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        Method load = clazz.getMethod("load", String.class, Client.class, CryptoKeyPair.class);
        T contract =(T) load.invoke(null, new Object[]{contractAddress, client, cryptoKeyPair});
        contractMap.put(contractName, contract);
        return contract;
    }

    public Object getContract(String contractName) {
        if (getContractMap().containsKey(contractName)) {
            return getContractMap().get(contractName);
        }
        return null;
    }

    public Map<String, Object> getContractMap() {
        return contractMap;
    }

    public void setContractMap(Map<String, Object> contractMap) {
        this.contractMap = contractMap;
    }

}



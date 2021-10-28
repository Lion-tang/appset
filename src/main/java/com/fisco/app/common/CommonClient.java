package com.fisco.app.common;

import com.fisco.app.contract.TestCRUD;
import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: zyt
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

        CryptoKeyPair cryptoKeyPair = client.getCryptoSuite().getCryptoKeyPair();//随机生成账户
        cryptoKeyPair.storeKeyPairWithPemFormat();//存储账户
        //加载已有账户pem文件
//        CryptoSuite cryptoSuite = client.getCryptoSuite();
//        cryptoSuite.loadAccount("pem","account/ecdsa/0x5a91ed032b2b541ee705d9d770b30bab5fc596c3.pem",null);
//        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        Method method = clazz.getMethod("deploy", Client.class,CryptoKeyPair.class);
        T result = (T) method.invoke(null,new Object[]{client,cryptoKeyPair});
        logger.info("执行CommonClient的deploy方法");
        logger.info("部署合约成功:{}"+contractName,result);
        contractMap.put(contractName,result);
    }

    public Object getContract(String contractName) {
        if (getContractMap().containsKey(contractName)) {
            return getContractMap().get(contractName);
        }
        return null;
    }

    public abstract void remove(String uid,String mapKey);

    public Map<String, Object> getContractMap() {
        return contractMap;
    }

    public void setContractMap(Map<String, Object> contractMap) {
        this.contractMap = contractMap;
    }


}



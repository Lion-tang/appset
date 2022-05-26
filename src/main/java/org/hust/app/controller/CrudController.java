package org.hust.app.controller;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.client.Client;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.hust.app.client.CRUDClient;
import org.hust.app.contract.ERC20;
import org.hust.app.entity.Goods;
import org.hust.app.entity.ResponseData;
import org.hust.app.entity.TotalAddress;
import org.hust.app.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.Principal;

/**
 * @Classname CrudController
 * @Description 通过接口调用sdk
 * @Date 2021/3/25 22:25
 * @Created by zyt
 */
@RestController
public class CrudController {

    @Autowired
    private CRUDClient crudClient;

    @GetMapping("/coins")
    public ResponseData address(@RequestParam("addr")String address) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        TotalAddress totalAddress = SpringUtils.getBean(TotalAddress.class);
        ERC20 erc20 = crudClient.load(TotalAddress.ERC20, totalAddress.getContract(), totalAddress.getAccount(), ERC20.class, 1);
        BigInteger bigInteger = null;

        try {
            bigInteger = erc20.balanceOf(address);
        } catch (ContractException e) {
            e.printStackTrace();
        }
        return ResponseData.success(bigInteger);
    }


    @PostMapping("/insertDetail")
    public ResponseData insertDetail(@RequestBody Goods goods){

        crudClient.insertDetail(goods.getUid(), goods.getAttr());
        return ResponseData.success("修改成功");
    }


}

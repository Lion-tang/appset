package org.hust.app.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hust.app.entity.Customer;
import org.hust.app.entity.ResponseListData;
import org.hust.app.mapper.CustomerMapper;
import org.hust.app.utils.ShaUtils;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomerMapper customerMapper;

    @Override
    public Integer register(Customer user) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", user.getUserName());
        if (customerMapper.selectOne(wrapper) == null) {
            user.setPassword(ShaUtils.code(user.getPassword(),ShaUtils.SHA_1));

            CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.SM_TYPE);
            CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
            String accountAddress = cryptoKeyPair.getAddress();
            cryptoKeyPair.storeKeyPairWithPemFormat();

            user.setAddress(accountAddress);
            int rescode = customerMapper.insert(user);
            return rescode;
        }
        return -1;
}

    @Override
    public Integer deleteUser(List<String> batchUsername) {
        int rescode = 0;
        for (String s : batchUsername) {
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_name", s);
            customerMapper.delete(queryWrapper);
            rescode++;
        }

        return rescode;
    }

    @Override
    public ResponseListData showUserAndAdmin(Integer num, Integer limit) {
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.ne("role", "commitee");
        Page<Customer> page = new Page<>(num, limit);
        IPage<Customer>iPage = customerMapper.selectPage(page, queryWrapper);
        ResponseListData responseListData = ResponseListData.success(iPage.getRecords());
        responseListData.setCode(0);
        responseListData.setCount((int)page.getTotal());
        responseListData.setMsg("查询结果:");
        return responseListData;
    }

    @Override
    public ResponseListData showUser(Integer num, Integer limit) {
        IPage<Customer> page = new Page<>(num, limit);
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("role", "user");
        page =  customerMapper.selectPage(page, queryWrapper);
        ResponseListData responseListData = ResponseListData.success(page.getRecords());
        responseListData.setCode(0);
        responseListData.setCount((int)page.getTotal());
        responseListData.setMsg("查询结果");
        return responseListData;
    }
}

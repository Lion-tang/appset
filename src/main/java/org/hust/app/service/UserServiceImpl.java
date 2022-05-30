package org.hust.app.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.hust.app.entity.Customer;
import org.hust.app.entity.ResponseListData;
import org.hust.app.mapper.CustomerMapper;
import org.hust.app.utils.HashPasswordEncoder;
import org.hust.app.utils.MysqlConstant;
import org.hust.app.utils.ShaUtils;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CustomerMapper customerMapper;

    /**
     * 初始注册一个链委员会账号
     * @param @customer代表一个用户类实体
     * @return zltang
     * @Date 2022/5/29
     */
    @Override
    public Integer initRegister(Customer customer) {
        if(customerMapper.selectCount(null) != 0)
            return -1;
        customer.setLocate("本机构链委员");
        customer.setPassword(ShaUtils.code(customer.getPassword(),ShaUtils.SHA_1));
        CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.SM_TYPE);
        CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
        customer.setAddress(cryptoKeyPair.getAddress());
        cryptoKeyPair.storeKeyPairWithPemFormat();
        customer.setRole("commitee");
        return customerMapper.insert(customer);
    }

    /**
    @Date 2021/11/26
    @Description 注册用户
    @author zltang
    **/

    @Override
    public Integer register(Customer user) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq(MysqlConstant.LOCATE, user.getLocate()).or().eq(MysqlConstant.USER_NAME, user.getUserName());
        if (customerMapper.selectOne(wrapper) == null) {
            user.setPassword(ShaUtils.code(user.getPassword(),ShaUtils.SHA_1));
            CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.SM_TYPE);
            CryptoKeyPair cryptoKeyPair = cryptoSuite.getCryptoKeyPair();
            String accountAddress = cryptoKeyPair.getAddress();
            cryptoKeyPair.storeKeyPairWithPemFormat();
            user.setAddress(accountAddress);
            return customerMapper.insert(user);
        }
        return -1;
}

    @Override
    public Integer updatePassword(String newPassword, String userName) {
        QueryWrapper<Customer> wrapper = new QueryWrapper<>();
        wrapper.eq(MysqlConstant.USER_NAME, userName);
        Customer customer = customerMapper.selectOne(wrapper);
        customer.setPassword(ShaUtils.code(newPassword,ShaUtils.SHA_1));
        return  customerMapper.updateById(customer);

    }

    /**
    @Date 2021/11/26
    @Description 在DApp删除用户，但是区块链并没用删除，由于DApp已经删除了用户对应区块链账户的信息，所以在逻辑上该用户就被删除了
    @author zltang
    **/

    @Override
    public Integer deleteUser(List<String> batchUsername) {
        int rescode = 0;
        for (String s : batchUsername) {
            QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

            queryWrapper.eq(MysqlConstant.USER_NAME, s);
            Customer customer = customerMapper.selectOne(queryWrapper);
            customerMapper.delete(queryWrapper);
            rescode++;
            String prefix = System.getProperty("user.dir") +
                    File.separator + "account" + File.separator +
                    "gm" + File.separator + customer.getAddress();
            File pemFile = new File(prefix + ".pem");
            File pubFile = new File(prefix + ".pem.pub");
            if(pemFile.exists())
                pemFile.delete();
            if(pubFile.exists())
                pubFile.delete();
        }

        return rescode;
    }

    /**
     * 修改用户当前密码
     * @param
     * @return zltang
     * @Date 2022/5/29
     */


    /**
    @Date 2021/11/26
    @Description 显示普通用户节点和链管理员节点的接口
    @author zltang
    **/

    @Override
    public ResponseListData showUserAndAdmin(Integer num, Integer limit) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne(MysqlConstant.ROLE, "commitee");
        Page<Customer> page = new Page<>(num, limit);
        IPage<Customer>iPage = customerMapper.selectPage(page, queryWrapper);
        ResponseListData responseListData = ResponseListData.success(iPage.getRecords());
        responseListData.setCode(0);
        responseListData.setCount((int)page.getTotal());
        responseListData.setMsg("查询结果:");
        return responseListData;
    }

    /**
    @Date 2021/11/26
    @Description 仅显示普通用户节点的接口
    @author zltang
    **/

    @Override
    public ResponseListData showUser(Integer num, Integer limit) {
        IPage<Customer> page = new Page<>(num, limit);
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(MysqlConstant.ROLE, "user");
        page =  customerMapper.selectPage(page, queryWrapper);
        ResponseListData responseListData = ResponseListData.success(page.getRecords());
        responseListData.setCode(0);
        responseListData.setCount((int)page.getTotal());
        responseListData.setMsg("查询结果");
        return responseListData;
    }
}

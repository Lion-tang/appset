package org.hust.app.service;

import org.hust.app.entity.Customer;
import org.hust.app.entity.ResponseListData;

import java.util.List;

public interface UserService {
    Integer initRegister(Customer customer);

    Integer register(Customer customer);

    Integer deleteUser(List<String> batchUsername);

    Integer updatePassword(String newPassword, String userName);

    ResponseListData showUserAndAdmin(Integer num, Integer limit);

    ResponseListData showUser(Integer num, Integer limit);

}

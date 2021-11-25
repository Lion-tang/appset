package org.hust.app.service;

import org.hust.app.entity.Customer;
import org.hust.app.entity.ResponseListData;

import java.util.List;

public interface UserService {
    Integer register(Customer customer);

    Integer deleteUser(List<String> batchUsername);

    ResponseListData showUserAndAdmin(Integer num, Integer limit);

    ResponseListData showUser(Integer num, Integer limit);
}

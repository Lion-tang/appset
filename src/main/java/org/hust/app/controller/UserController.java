package org.hust.app.controller;

import org.hust.app.entity.Customer;
import org.hust.app.entity.ResponseData;
import org.hust.app.entity.ResponseListData;
import org.hust.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
    @Date 2021/11/26
    @Description 用户注册外部接口
    @author zltang
    **/

    @PostMapping("/register")
    @ResponseBody
    public ResponseData register(@RequestBody Customer user) {
        int rescode = userService.register(user);
        if (rescode == -1) {
            return ResponseData.error("用户名或节点实体已存在，请使用其他用户名或节点实体");
        } else if (rescode == 1) {
            return ResponseData.success("注册成功");
        }
        return ResponseData.error("服务器错误，注册失败");
    }

    /**
    @Date 2021/11/26
    @Description 删除逻辑用户外部接口
    @author zltang
    **/

    @PostMapping("/deleteUser")
    @ResponseBody
    public ResponseData deleteUser(@RequestBody List<String> batchUsername){
        int rescode = userService.deleteUser(batchUsername);
        if (rescode >= 1) return ResponseData.success("用户删除成功");
        else return ResponseData.error("用户删除失败");
    }

    /**
    @Date 2021/11/26
    @Description 显示所有普通节点和链管理员外部接口
    @author zltang
    **/

    @PostMapping("/showUserAndAdmin")
    @ResponseBody
    public ResponseListData showUserAndAdmin(@RequestParam("page") Integer num, @RequestParam("limit") Integer limit) {
        ResponseListData responseListData = userService.showUserAndAdmin(num, limit);
        return responseListData;
    }

    /**
    @Date 2021/11/26
    @Description 仅显示普通节点外部接口
    @author zltang
    **/

    @PostMapping("/showUser")
    @ResponseBody
    public ResponseListData showUser(@RequestParam("page") Integer num, @RequestParam("limit") Integer limit) {
        ResponseListData responseListData = userService.showUser(num, limit);
        return responseListData;
    }

}

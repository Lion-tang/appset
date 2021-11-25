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

    @PostMapping("/register")
    @ResponseBody
    public ResponseData register(@RequestBody Customer user) {
        int rescode = userService.register(user);
        if (rescode == -1) {
            return ResponseData.error("用户名已存在，请使用其他用户名");
        } else if (rescode == 1) {
            return ResponseData.success("注册成功");
        }
        return ResponseData.error("服务器错误，注册失败");
    }

    @PostMapping("/deleteUser")
    @ResponseBody
    public ResponseData deleteUser(@RequestBody List<String> batchUsername){
        int rescode = userService.deleteUser(batchUsername);
        if (rescode >= 1) return ResponseData.success("用户删除成功");
        else return ResponseData.error("用户删除失败");
    }

    @PostMapping("/showUserAndAdmin")
    @ResponseBody
    public ResponseListData showUserAndAdmin(@RequestParam("page") Integer num, @RequestParam("limit") Integer limit) {
        ResponseListData responseListData = userService.showUserAndAdmin(num, limit);
        return responseListData;
    }

    @PostMapping("/showUser")
    @ResponseBody
    public ResponseListData showUser(@RequestParam("page") Integer num, @RequestParam("limit") Integer limit) {
        ResponseListData responseListData = userService.showUser(num, limit);
        return responseListData;
    }

    @GetMapping("/test")
    @ResponseBody
    public ResponseData test() {
        return ResponseData.success("hello world");
    }
}

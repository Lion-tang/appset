package com.fisco.app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.security.Principal;


@Controller
public class ViewController {
    //无前缀逻辑页面转换到物理页面
    @RequestMapping("/{url}")
    public String getUrl(@PathVariable("url") String url) {
        return url;
    }

    //登陆后管理员的根页面
    @RequestMapping("/index")
    public String getIndex(Model model, Principal principal) {
        String title = "用户"+principal.getName();
        model.addAttribute("title",title);
        return "index";
    }

    //未找到资源404视图
    @RequestMapping("/404")
    public String notFound() {
        /**
         * @Date  2021/6/5
         * @Description ErrorConfig已经拦截了404请求，然后映射到这个请求上
         * @return java.lang.String
         * @author Lyontang
        **/
        return "page/tips/404";
    }

    //服务器错误500视图
    @RequestMapping("/500")
    public String internalError() {
        return "page/tips/error";
    }


    //主页物理视图

    @RequestMapping("/home/{page}")
    public String getHomeUrl(@PathVariable("page")String page) {
        return "home/"+page;
    }

    //组件物理视图
    @RequestMapping("/component/{prefix}/{page}")
    public String getComponentUrl(@PathVariable("prefix")String prefix,@PathVariable("page")String page) {
        return "component/"+prefix+"/"+page;
    }

    //设置物理视图
    @RequestMapping("/set/{set}")
    public  String getSetUrl(@PathVariable("set")String set) {
        return "set/" + set;
    }

    //页面物理视图
    @RequestMapping("/page/{page}")
    public String getPageUrl(@PathVariable("page")String page) {
        return "page/"+page;
    }

    //用户物理视图
    @RequestMapping("/user/{demo}")
    public String getUserUrl( @PathVariable("demo") String demo) {
        return "user/"+demo;
    }


}

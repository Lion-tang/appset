package org.hust.app.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.hust.app.client.CRUDClient;
import org.hust.app.contract.ERC20;
import org.hust.app.entity.Customer;
import org.hust.app.entity.TotalAddress;
import org.hust.app.mapper.CustomerMapper;
import org.hust.app.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.Principal;


@Controller
public class ViewController {
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private CRUDClient crudClient;
    @Value("${address.webase}")
    private String webaseAddress;
    /**
    @Date 2021/11/26
    @Description 无前缀逻辑页面转换到物理页面
    @author zltang
    **/
    @RequestMapping("/{url}")
    public String getUrl(@PathVariable("url") String url) {
        return url;
    }

    /**
    @Date 2021/11/26
    @Description 登陆后管理员的根页面
    @author zltang
    **/
    @RequestMapping("/index")
    public String getIndex(Model model, Principal principal) throws InvocationTargetException, NoSuchMethodException, IllegalAccessException, ContractException {
        String userName = principal.getName();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", userName);
        Customer customer = customerMapper.selectOne(queryWrapper);
        TotalAddress totalAddress = SpringUtils.getBean(TotalAddress.class);
        ERC20 erc20 = crudClient.load(TotalAddress.ERC20, totalAddress.getContract(),
                totalAddress.getAccount(), ERC20.class, 1);
        BigInteger bigInteger = erc20.balanceOf(customer.getAddress());
        model.addAttribute("title",  userName + "(联盟币： " + bigInteger.intValue() + ")");
        model.addAttribute("role", customer.getRole());
        model.addAttribute("webase", "http://" + webaseAddress + ":5000/#/home");
        return "index";
    }

    /**
    @Date 2021/11/26
    @Description 未找到资源404视图,ErrorConfig已经拦截了404请求，然后映射到这个请求上
    @author zltang
    **/
    @RequestMapping("/404")
    public String notFound() {
        return "page/tips/404";
    }

    /**
    @Date 2021/11/26
    @Description 服务器错误500视图
    @author zltang
    **/
    @RequestMapping("/500")
    public String internalError() {
        return "page/tips/error";
    }

    /**
     *
     * @param principal
     * @return zltang
     * @Date 2022/5/15
     */
    @GetMapping("/home/upload")
    public ModelAndView getHomeUpload(Principal principal){
        ModelAndView modelAndView = new ModelAndView("home/upload");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("user_name", principal.getName());
        Customer customer = customerMapper.selectOne(queryWrapper);
        modelAndView.addObject("locate", customer.getLocate());
        return modelAndView;
    }
    /**
    @Date 2021/11/26
    @Description 主页物理视图
    @author zltang
    **/
    @GetMapping("/home/{page}")
    public String getHomeUrl(@PathVariable("page") String page) {
        return "home/" + page;
    }

    @GetMapping(value = "/home/{page}",params = {"uid"})
    public ModelAndView getHomeUrlParam(@PathVariable("page") String page, String uid) {
        ModelAndView modelAndView = new ModelAndView("home/"+page);
        modelAndView.addObject("uid",uid);
        return modelAndView;
    }

    /**
    @Date 2021/11/26
    @Description 组件物理视图
    @author zltang
    **/
    @RequestMapping("/component/{prefix}/{page}")
    public String getComponentUrl(@PathVariable("prefix") String prefix, @PathVariable("page") String page) {
        return "component/" + prefix + "/" + page;
    }

    /**
    @Date 2021/11/26
    @Description 设置物理视图
    @author zltang
    **/
    @RequestMapping("/set/{set}")
    public String getSetUrl(@PathVariable("set") String set) {
        return "set/" + set;
    }

    /**
    @Date 2021/11/26
    @Description 页面物理视图
    @author zltang
    **/
    @RequestMapping("/page/{page}")
    public String getPageUrl(@PathVariable("page") String page) {
        return "page/" + page;
    }

    /**
    @Date 2021/11/26
    @Description 用户物理视图
    @author zltang
    **/
    @RequestMapping("/user/{demo}")
    public String getUserUrl(@PathVariable("demo") String demo) {
        return "user/" + demo;
    }

    /**
    @Date 2021/11/26
    @Description 用户物理视图
    @author zltang
    **/
    @RequestMapping("/user/{firstParm}/{secondParm}")
    public String getUserAdminUrl( @PathVariable("firstParm")String firstParm,@PathVariable("secondParm") String seconParm) {
        return "user/" + firstParm +"/" + seconParm;
    }


}

package com.fisco.app.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fisco.app.entity.*;
import com.fisco.app.entity.VO.TxRecordVO;
import com.fisco.app.mapper.CustomerMapper;
import com.fisco.app.service.FIleService;
import com.fisco.app.service.FIleServiceImpl;
import com.fisco.app.utils.ElasticSearchClient;
import com.fisco.app.utils.FileUtils;
import com.fisco.app.utils.ShaUtils;
import org.apache.http.HttpResponse;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.crypto.keypair.CryptoKeyPair;
import org.fisco.bcos.sdk.model.CryptoType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Controller
public class TxController {
    @Autowired
    private FIleService fileService;
    @Autowired
    private CustomerMapper customerMapper;

    //上传文件接口
    @PostMapping("/upload")
    @ResponseBody
    public ResponseData upload(MultipartFile file, @RequestParam("uid") String uid, @RequestParam("desc")String desc, Principal principal) {
        if (file == null) {
            return ResponseData.error("请求接口数据接收异常");
        }
        if(file.isEmpty()){
            return ResponseData.error("文件为空，上传失败");
        }
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_name", principal.getName());
            String adressFile = customerMapper.selectOne(wrapper).getAddress();
            String result = fileService.uploadFile(file, uid, desc, principal.getName(), adressFile);
            //文件存在情况
            if (result.equals("文件上传失败，文件已经存在！") || result.equals("未上传任何文件")) {
                return ResponseData.error(result);
            }
            return ResponseData.success(result);
        } catch (Exception e) {
            e.printStackTrace();
            return  ResponseData.error("文件上传异常");
        }
    }

    @PostMapping("/query")
    @ResponseBody
    public ResponseListData query(String uid)  {
        if (uid == null || uid.equals("")) {
            return ResponseListData.error("请输入查询标识号");
        }
        List<TxRecordVO> result = null;
        try {
            result = fileService.queryRecord("entity.uid",uid);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseListData.error("服务器内部错误");
        }
        if (result == null) {
            return ResponseListData.error("未找到记录");
        }
        return ResponseListData.success(result);//返回前端JSON数据
    }

    @PostMapping("/download")
    @ResponseBody
    public void download(@RequestParam("fileNames") String fileNames, HttpServletResponse response) {

//        String result = null;
        String[] fileArrays = fileNames.split(",");
        List<String> fileLists = Arrays.asList(fileArrays);
        try {

            String zipFileName = FileUtils.getCurrentTime() + ".zip";
            fileService.zipDownload( response,fileLists, zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
            return ;
        }
//        if (result==null) {
//            return ;
//        }
        return ;
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseData register(@RequestBody Customer user) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("user_name", user.getUserName());
        if (customerMapper.selectOne(wrapper) == null) {
            user.setRole("user");
            user.setPassword(ShaUtils.code(user.getPassword(),ShaUtils.SHA_1));

            CryptoSuite cryptoSuite = new CryptoSuite(CryptoType.ECDSA_TYPE);
            CryptoKeyPair cryptoKeyPair = cryptoSuite.createKeyPair();
            String accountAddress = cryptoKeyPair.getAddress();

            user.setAddress(accountAddress);

            int rescode = customerMapper.insert(user);

            if (rescode == 0) {
                return ResponseData.error("服务器错误，注册失败");
            }
            return ResponseData.success("注册成功");
        }
        return ResponseData.error("用户名已存在，请使用其他用户名");
    }

//    @PostMapping("uploadRoute")
//    @ResponseBody
//    public ResponseData<String> uploadRoute(MultipartFile file) throws IOException {
//        System.out.println("跨服务器上传文件上传");
//        String path = "http://172.168.160.18:8088/upload";
//        CloseableHttpClient httpClient = HttpClients.createDefault();
//        HttpPost httpPost = new HttpPost(path);
//        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//
//            String filename = file.getOriginalFilename();
//            builder.addBinaryBody("file", file.getBytes(), ContentType.MULTIPART_FORM_DATA, filename);
//
//        try {
//
//            HttpEntity entity = builder.build();
//            httpPost.setEntity(entity);
//            CloseableHttpResponse response = httpClient.execute(httpPost);
//            System.out.println(response.getStatusLine().getStatusCode());
//            String s = response.getEntity().toString();
//            System.out.println(s);
//        } catch (Exception e) {
//            e.printStackTrace();
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
//            String date = sdf.format(new Date());
//            System.out.println(date);
//        } finally {
//            httpClient.close();
//        }
//        return ResponseData.success("sucess");
//    }

}

package org.hust.app.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.hust.app.entity.Customer;
import org.hust.app.entity.ResponseData;
import org.hust.app.entity.ResponseListData;
import org.hust.app.entity.VO.TxDetailVO;
import org.hust.app.entity.VO.TxRecordVO;
import org.hust.app.mapper.CustomerMapper;
import org.hust.app.service.FIleService;
import org.hust.app.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.util.Arrays;
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
            Customer customer = customerMapper.selectOne(wrapper);
            String adressFile = customer.getAddress();
            String locate = customer.getLocate();
            String result = fileService.uploadFile(file, uid, desc, principal.getName(), adressFile, locate);
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

    @PostMapping("/queryRecord")
    @ResponseBody
    public ResponseListData queryRecord(@RequestParam("uid") String uid)  {
        if (uid == null || uid.equals("")) {
            return ResponseListData.error("请输入查询标识号");
        }
        List<TxRecordVO> result = null;

        try {

            result = fileService.queryRecord(uid);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseListData.error("服务器内部错误");
        }
        if (result == null) {
            return ResponseListData.error("未找到记录");
        }
        return ResponseListData.success(result);//返回前端JSON数据
    }

    @PostMapping("/queryDetail")
    @ResponseBody
    public ResponseListData queryDetail(@RequestParam("txHash") String txHash)  {
        if (txHash == null || txHash.equals("")) {
            return ResponseListData.error("请输入查询标识号");
        }
        List<TxDetailVO> result = null;

        try {

            result = fileService.queryDetail(txHash);
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

        String[] fileArrays = fileNames.split(",");
        List<String> fileLists = Arrays.asList(fileArrays);
        try {

            String zipFileName = FileUtils.getCurrentTime() + ".zip";
            fileService.zipDownload( response,fileLists, zipFileName);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}

package com.fisco.app.service;

import com.fisco.app.entity.ResponseListData;
import com.fisco.app.entity.VO.TxRecordVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface FIleService {
    String uploadFile(MultipartFile file, String uid, String desc, String contractName,String addressFile) throws Exception;

    List<Map<String,Object>> query(String match, String id,String indexName) throws IOException;

    List<TxRecordVO> queryRecord(String match, String id) throws IOException;

    void downloadOne(String fileName, HttpServletResponse response) throws IOException;

    void zipDownload(HttpServletResponse response, List<String> fileList, String zipFileName) throws FileNotFoundException;


}

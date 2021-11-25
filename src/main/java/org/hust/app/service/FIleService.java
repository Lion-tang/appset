package org.hust.app.service;

import org.hust.app.contract.DetailCRUD;
import org.hust.app.entity.VO.TxDetailVO;
import org.hust.app.entity.VO.TxRecordVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface FIleService {
    String uploadFile(MultipartFile file, String uid, String desc, String contractName,String addressFile, String locate) throws Exception;

    List<TxDetailVO> queryDetail(String txHash) throws IOException;

    List<TxRecordVO> queryRecord(String uid) throws IOException;

    void downloadOne(String fileName, HttpServletResponse response) throws IOException;

    void zipDownload(HttpServletResponse response, List<String> fileList, String zipFileName) throws FileNotFoundException;


}

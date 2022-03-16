package org.hust.app.service;

import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.hust.app.entity.VO.TxDetailVO;
import org.hust.app.entity.VO.TxRecordVO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public interface FIleService {
    String uploadFile(MultipartFile file, String uid, String desc) throws Exception;

    String uploadDetail(String uid, String attr) throws ContractException, InvocationTargetException, NoSuchMethodException, IllegalAccessException;

    List<TxDetailVO> queryDetail(String txHash) throws IOException;

    List<TxRecordVO> queryRecord(String uid) throws IOException;

    void downloadOne(String fileName, HttpServletResponse response) throws IOException;

    void zipDownload(HttpServletResponse response, List<String> fileList, String zipFileName) throws FileNotFoundException;


}

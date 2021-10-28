package com.fisco.app.service;

import com.fisco.app.client.CRUDClient;

import com.fisco.app.contract.TestCRUD;
import com.fisco.app.entity.LoadConfig;
import com.fisco.app.entity.VO.TxRecordVO;
import com.fisco.app.utils.ElasticSearchClient;
import com.fisco.app.utils.FileUtils;
import com.fisco.app.utils.ShaUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FIleServiceImpl implements FIleService {
    public static final Logger logger = LoggerFactory.getLogger(FIleServiceImpl.class.getName());
    @Autowired
    private CRUDClient crudClient;

    public static final String record_index = "record_index";
    public static final String detail_index = "detail_index";

    @Override
    public String uploadFile(MultipartFile file, String uid, String desc, String contractName, String addressFile) throws Exception {

        if (file == null) {
            return "未上传任何文件";
        }
        File fileRealPath = FileUtils.getFileFromSystem("/static/files/", "/files/");

        logger.info("the file path is: {}", fileRealPath.getPath());//打印项目静态文件路径

        if (!fileRealPath.exists()) {//判断文件夹是否存在
            fileRealPath.mkdirs(); //不存在，创建父目录
        }

        int filePostfixIndex = file.getOriginalFilename().lastIndexOf(".");//文件后缀名
        String filePostfix = file.getOriginalFilename().substring(filePostfixIndex);
        String shaDigest = ShaUtils.code(file.getBytes(), ShaUtils.SHA_1) + filePostfix;//计算文件Hash值
        logger.info("hash digest file is : {}", shaDigest);
        //hash命名文件
        File newFileName = new File(fileRealPath.getPath() + "/" + shaDigest);
        //如果hash值存在，直接返回客户端,由于es是分词相似度查找，所以会有一些相似的，tag=0标识没有hash值一样的文件，tag=1则存在
        List<Map<String, Object>> datas = query("entity.hash", shaDigest, detail_index);
        List<String> list = new ArrayList<>();
        datas.forEach((singleData) -> {
            Map<String, Object> map = (HashMap) singleData.get("entity");
            //查询
            String resultConfig = map.get("hash").toString();
            list.add(resultConfig);
        });//数组接收ES结果集数据
        int tag = 0;
        for (String str : list) {
            if (str.equals(shaDigest)) {
                tag = 1;
                break;
            }
        }
        if (tag == 1) {
            return "文件上传失败，文件已经存在！";
        }
        //文件hash值在文件夹不存在，可以存储文件
        file.transferTo(newFileName);
        if (uid == null || "".equals(uid)) {
            uid = UUID.randomUUID().toString().trim().replaceAll("-", "");
        }
        //开启文件Hash上传至区块链
        TestCRUD testCRUD = crudClient.getTestCRUDFromAccount(1, addressFile);
        Map<String, Object> map = crudClient.getContractMap();
        map.put(contractName, testCRUD);
        crudClient.insert(uid, shaDigest, desc, contractName);
        return "文件上传成功,数据已同步至区块链。</br>唯一标识符为:" + uid + "</br>务必记住该商品标识符，便于后续溯源";
    }

    @Override
    public List<Map<String, Object>> query(String match, String id, String indexName) throws IOException {
        /**
         * @Date 2021/6/6
         * @Description match是es查询的字段名，id是需要查询字段的值
         * @return java.util.List<com.fisco.app.entity.VO.TxRecordVO>
         * @author Lyontang
         **/

        File filePath = null;//获取配置文件目录
        filePath = FileUtils.getFileFromSystem("/conf.ini", "/conf.ini");
        String fileString = filePath.getAbsolutePath();//获取配置文件路径字符串
        //FIleServiceImpl.logger.info("config.ini file path is: {}", fileString);//日志记录配置文件信息
        LoadConfig cfg = new LoadConfig(fileString);//读取配置文件
        ElasticSearchClient elasticSearchClient = new ElasticSearchClient(cfg.getElasticSearchConfig());//初始化ES配置类
        Map<String, Object> field = new HashMap<>();//构造查询字段
        field.put(match, id);//查询字段值
        String index = null;
        if (indexName.equals(record_index))
            index = cfg.getElasticSearchConfig().getRecord_index();//获取ES索引名
        else if (indexName.equals(detail_index) )
            index = cfg.getElasticSearchConfig().getDetail_index();
        ArrayList<Map<String, Object>> datas = elasticSearchClient.GetSrcTxData(index, field);//查询ES
        return datas;
    }

    public List<TxRecordVO> queryRecord(String match, String id) throws IOException {
        List<Map<String, Object>> datas = query(match, id, detail_index);
        List<TxRecordVO> result = new ArrayList<>();
        for (Map<String, Object> singleData : datas) {
            Map<String,Object> map = (HashMap) singleData.get("entity");
            String tx_hash = map.get("tx_hash").toString();
            List<Map<String,Object>> recordDatas = query("txHash", tx_hash, record_index);
            if (recordDatas.size() == 0)continue;
            Map<String, Object> recordMap = recordDatas.get(0);
            String blockTimeStamp = recordMap.get("blockTimeStamp").toString();
            blockTimeStamp = ElasticSearchClient.timeStampToFormatDate(Long.parseLong(blockTimeStamp));
            String blockHeight = recordMap.get("blockHeight").toString();
            blockHeight = Integer.toString(Integer.parseInt(blockHeight.substring(2), 16));
            String txHash = recordMap.get("txHash").toString();
            String txFrom = recordMap.get("txFrom").toString();
            String txTo = recordMap.get("txTo").toString();
            result.add(new TxRecordVO(blockTimeStamp, blockHeight, txHash, txFrom, txTo));
        }
        return result;
    }

    //根据单个文件名下载文件
    @Override
    public void downloadOne(String fileName, HttpServletResponse response) throws IOException {


        if (fileName != null) {
            File absolutePath = FileUtils.getFileFromSystem("/static/files/" + fileName, "/files/" + fileName);
            if (!absolutePath.exists()) {
                return;
            }
            // 读到流中
            InputStream inputStream = new FileInputStream(absolutePath);// 文件的存放路径
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
            ServletOutputStream outputStream = response.getOutputStream();
            byte[] b = new byte[1024];
            int len;
            //从输入流中读取一定数量的字节，并将其存储在缓冲区字节数组中，读到末尾返回-1
            while ((len = inputStream.read(b)) > 0) {
                outputStream.write(b, 0, len);
            }
            inputStream.close();
            outputStream.close();

        }

    }

    /**
     * @param response
     * @param fileList
     * @Description zip打包下载多个文件
     */
    public void zipDownload(HttpServletResponse response, List<String> fileList, String zipFileName) throws FileNotFoundException {
        // zip文件路径
        String zipPath = FileUtils.getFileFromSystem("/static/files/", "/files/", zipFileName).getAbsolutePath();
        try {
            //创建zip输出流
            try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath))) {
                //声明文件集合用于存放文件
                byte[] buffer = new byte[1024];
                //将文件放入zip压缩包
                for (int i = 0; i < fileList.size(); i++) {
                    File file = FileUtils.getFileFromSystem("/static/files/", "/files/", fileList.get(i));
                    try (FileInputStream fis = new FileInputStream(file)) {
                        out.putNextEntry(new ZipEntry(file.getName()));
                        int len;
                        // 读入需要下载的文件的内容，打包到zip文件
                        while ((len = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        out.closeEntry();
                    }
                }
            }
            //下载zip文件
            downFile(response, zipFileName);
        } catch (Exception e) {
            logger.error("文件下载出错", e);
        } finally {
            // zip文件也删除
            deleteFile(zipPath);
        }
    }


    /**
     * @param response
     * @param zipFileName
     * @Description 为zip打包文件下载
     */
    private void downFile(HttpServletResponse response, String zipFileName) {
        try {
            String path = FileUtils.getFileFromSystem("/static/files/", "/files/", zipFileName).getAbsolutePath();
            File file = new File(path);
            if (file.exists()) {
                try (InputStream ins = new FileInputStream(path);
                     BufferedInputStream bins = new BufferedInputStream(ins);
                     OutputStream outs = response.getOutputStream();
                     BufferedOutputStream bouts = new BufferedOutputStream(outs)) {
                    response.setContentType("application/x-download");
                    response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(zipFileName, "UTF-8"));
                    int bytesRead = 0;
                    byte[] buffer = new byte[8196];
                    while ((bytesRead = bins.read(buffer)) != -1) {
                        bouts.write(buffer, 0, bytesRead);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("文件下载出错", e);
        }

    }

    /**
     * @param
     * @return
     * @Description 下载完zip后，服务器删除打包文件
     */
    public void deleteFile(String fileString) {

        File file = new File(fileString);
        if (file.exists()) {
            file.delete();
        }
    }

}




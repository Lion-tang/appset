package org.hust.app.service;

import org.fisco.bcos.sdk.BcosSDK;
import org.fisco.bcos.sdk.abi.datatypes.generated.tuples.generated.Tuple2;
import org.fisco.bcos.sdk.model.TransactionReceipt;
import org.fisco.bcos.sdk.transaction.model.exception.ContractException;
import org.hust.app.client.CRUDClient;
import org.hust.app.contract.DetailCRUD;
import org.hust.app.contract.ERC20;
import org.hust.app.contract.RecordCRUD;
import org.hust.app.entity.TotalAddress;
import org.hust.app.entity.VO.TxDetailVO;
import org.hust.app.entity.VO.TxRecordVO;
import org.hust.app.mapper.TxDetailMapper;
import org.hust.app.mapper.TxRecordMapper;
import org.hust.app.utils.FileUtils;
import org.hust.app.utils.ShaUtils;
import org.hust.app.utils.SpringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
public class FIleServiceImpl implements FIleService {
    public static final Logger logger = LoggerFactory.getLogger(FIleServiceImpl.class.getName());
    @Autowired
    private CRUDClient crudClient;
    @Autowired
    private TxDetailMapper txDetailMapper;
    @Autowired
    private TxRecordMapper recordMapper;

    /**
     * @Date 2021/11/11
     * @Description
     * @param file 是前端上传的文件数据
     * @param uid 唯一标识码
     * @param desc 上传描述
     * @author Lyontang
     **/

    @Override
    public String uploadFile(MultipartFile file, String uid, String desc, String address) throws Exception {
        if (file == null) {
            return "未上传任何文件";
        }
        String descLocate = desc.substring(4, desc.indexOf(',', 4));
        List<TxRecordVO> recordVOs = queryRecord(uid);
        for (TxRecordVO recordVO : recordVOs) {
            String attr = recordVO.getAttr();
            String attrLocate = attr.substring(29, attr.indexOf(',', 29) );
            if(attrLocate.equals(descLocate))
                return "文件上传失败，文件已经存在！";
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
        File newFileName = new File(fileRealPath.getPath() + "/" + shaDigest);//hash命名文件
        if (newFileName.exists()) { //可能造成一致性问题
            return "文件上传失败，文件已经存在！";
        }
        file.transferTo(newFileName);//文件hash值在文件夹不存在，可以存储文件
        if (uid == null || "".equals(uid)) {//没有uid自动生成uid
            uid = UUID.randomUUID().toString().trim();//uid = UUID.randomUUID().toString().trim().replaceAll("-", "")去掉uuid 连接符
        }

        Map<String, Object> map = crudClient.getContractMap();//开启文件Hash上传至区块链
        if (!map.containsKey(TotalAddress.RECORD_CONTRACT_NAME)){
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
            crudClient.deploy(TotalAddress.RECORD_CONTRACT_NAME, RecordCRUD.class,sdk, 1);
        }
        RecordCRUD recordCRUD = ((RecordCRUD) map.get(TotalAddress.RECORD_CONTRACT_NAME));
        TransactionReceipt transactionReceipt = recordCRUD.insert(uid, shaDigest + "," + desc);
        logger.info(transactionReceipt.toString());
        TotalAddress totalAddress = SpringUtils.getBean(TotalAddress.class);
        ERC20 erc20 = crudClient.load(TotalAddress.ERC20, totalAddress.getContract(), totalAddress.getAccount(), ERC20.class, 1);
        TransactionReceipt transferReceipt = erc20.transfer(address, BigInteger.valueOf(1));
        logger.info(transferReceipt.toString());
        return "文件上传成功,数据已同步至区块链。</br>唯一标识符为:" + uid + "</br>务必记住该商品标识符，便于后续溯源";
    }

    /***
     @Date 2022/3/8
     @Description 首站产出商品，生成溯源码
     @param attr 商品信息
     @author zltang
    ***/

    @Override
    public String uploadDetail(String uid,  String attr) throws ContractException, InvocationTargetException, NoSuchMethodException, IllegalAccessException {
        if (crudClient.getContract(TotalAddress.DETAIL_CONTRACT_NAME) == null) {
            ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
            BcosSDK sdk = (BcosSDK) applicationContext.getBean("bcosSDK");
            crudClient.deploy(TotalAddress.DETAIL_CONTRACT_NAME,DetailCRUD.class, sdk, 2);

        }
        DetailCRUD detailCRUD = (DetailCRUD) crudClient.getContract(TotalAddress.DETAIL_CONTRACT_NAME);
        TransactionReceipt result = detailCRUD.insert(uid, attr);
        logger.info(result.toString());
        return "上传商品详情成功";

    }

    /**
     * @return java.util.List<com.fisco.app.entity.VO.TxRecordVO>
     * @Date 2021/6/6
     * @Description 查询详细信息
     * @author Lyontang
     **/
    @Override
    public List<TxDetailVO> queryDetail(String txHash) {
        List<TxDetailVO> list = txDetailMapper.selectDetailVO0(txHash);
        list.addAll(txDetailMapper.selectDetailVO1(txHash));
        return list;
    }

    /**
     * @Date 2021/11/12
     * @Description 查询Record链下数据库, match是想要查询的字段, id是查询字段的值
     * @author Lyontang
     **/

    public List<TxRecordVO> queryRecord(String uid) {
        List<TxRecordVO> list = recordMapper.selectRecordVO0(uid);
        list.addAll(recordMapper.selectRecordVO1(uid));
        Collections.sort(list);

        return list;
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




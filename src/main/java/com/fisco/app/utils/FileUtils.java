package com.fisco.app.utils;

import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtils {
    /**
     * @Date  2021/4/23
     * @Description 文件file类转二进制数组
     * @return byte[]
     * @author Lyontang
    **/
    public static byte[] fileToBinArray(File file){
        try {
            InputStream fis = new FileInputStream(file);
            byte[] bytes = FileCopyUtils.copyToByteArray(fis);
            return bytes;
        }catch (Exception ex){
            throw new RuntimeException("transform file into bin Array 出错",ex);
        }
    }

    /**
     * @Date  2021/5/25
     * @Description 返回工程项目根目录，winChildPath形如/static/img/,linuxChildPath形如/files/
     * @return 包含路径的文件抽象类
     * @author Lyontang
    **/
    public static File getFileFromSystem(String winChildPath,String linuxChildPath) throws FileNotFoundException {
        File fileRealPath=null;
        String os = System.getProperty("os.name");
        if (os.toLowerCase().startsWith("win")) {  //windows系统
            {
                fileRealPath = new File(System.getProperty("user.dir") + "/src/main/resources" + winChildPath);
            }
        } else {//linux系统
            //获取根目录
            //如果是在本地windows环境下，目录为项目的target\classes下
            //如果是linux环境下，目录为jar包同级目录
            File rootPath = new File(ResourceUtils.getURL("classpath:").getPath());
            if (!rootPath.exists()) {
                rootPath = new File("");
            }
            fileRealPath = new File(rootPath.getAbsolutePath() + linuxChildPath);
        }
        return fileRealPath;
    }
    public  static File getFileFromSystem(String winChildDir,String linuxChildDir,String fileName) throws FileNotFoundException {
        File file = getFileFromSystem(winChildDir + fileName, linuxChildDir + fileName);
        return file;
    }

    public static String getCurrentTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        return simpleDateFormat.format(date);
    }
}


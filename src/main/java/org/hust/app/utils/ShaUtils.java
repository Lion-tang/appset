package org.hust.app.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaUtils {
    public static String MD5 = "MD5";
    public static String SHA_1 = "SHA-1";
    public static String SHA_256 = "SHA-256";
    /**
     * @Date  2021/4/23
     * @Description 把二进制数组转换为十六进制字符串,可根据需要截取SHA-1产生的摘要位数,mode可选MD5或SHA_1
     * @return String
     * @author Lyontang
    **/


    public static String code(byte[] bytes,String mode) {

        try {
            MessageDigest md = MessageDigest.getInstance(mode);
            md.update(bytes);
            byte[] byteDigest = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < byteDigest.length; offset++) {
                i = byteDigest[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            //40位加密(小写)
//            return buf.toString();
            //40位加密(大写)
            //return buf.toString().toUpperCase();
            // 20位的加密(小写)
            return buf.toString().substring(9, 29);
            // 20位的加密(大写)
            //return buf.toString().substring(9, 29).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

    }
    public  static String code(String str, String mode){
        return code(str.getBytes(), mode);
    }

}

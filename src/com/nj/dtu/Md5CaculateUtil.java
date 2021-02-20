package com.nj.dtu;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.MessageDigest;
 
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
 
/**
 *MD5���㹤��
 */
public class Md5CaculateUtil {
 
    /**
     * ��ȡһ���ļ���md5ֵ(�ɴ�����ļ�)
     * @return md5 value
     */
    public static String getMD5(File file) {
        FileInputStream fileInputStream = null;
        try {
            MessageDigest MD5 = MessageDigest.getInstance("MD5");
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[8192];
            int length;
            while ((length = fileInputStream.read(buffer)) != -1) {
                MD5.update(buffer, 0, length);
            }
            return new String(Hex.encodeHex(MD5.digest()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (fileInputStream != null){
                    fileInputStream.close();
                    }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
 
    /**
     * ��һ���ַ�����md5ֵ
     * @param target �ַ���
     * @return md5 value
     */
    public static String MD5(String target) {
        return DigestUtils.md5Hex(target);
    }
 
    public static void main(String[] args) {
        long beginTime = System.currentTimeMillis();
        File file = new File("D:/1/pdi-ce-7.0.0.0-24.zip");
        String md5 = getMD5(file);
        long endTime = System.currentTimeMillis();
        System.out.println("MD5:" + md5 + "\n ��ʱ:" + ((endTime - beginTime) / 1000) + "s");
    }
}
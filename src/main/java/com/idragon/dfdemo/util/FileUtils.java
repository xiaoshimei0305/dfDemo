package com.idragon.dfdemo.util;

import java.io.*;

/**
 * @author chenxinjun
 * 文件工具
 */
public class FileUtils {
    /**
     * 读取文件内容
     * @param fileName
     * @return
     */
    public static String readFileContent(String fileName) {
        File file = new File(fileName);
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        InputStream is =null;
        try {
            //将字节流转为字符流并建立读缓存区
            is=new FileInputStream(new File(fileName));
            reader = new BufferedReader(new InputStreamReader(is,"UTF-8"));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr);
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return sbf.toString();
    }
}

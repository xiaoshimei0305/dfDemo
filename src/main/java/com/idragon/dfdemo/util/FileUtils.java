package com.idragon.dfdemo.util;

import store.idragon.tool.base.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;

/**
 * 文件工具
 * @author chenxinjun
 */
@Slf4j
public class FileUtils {
    /**
     * 获取文件对象
     * @param fileName
     * @param isCreateWhenNotExits
     * @return
     */
    public static File getFile(String fileName, boolean isCreateWhenNotExits) throws IOException {
        File temp= new File(fileName);
        //创建文件目录
        if(isCreateWhenNotExits&&!temp.exists()){
            initDir(temp.getParentFile());
        }
        return temp;
    }

    /**
     * 初始化文件目录
     * @param path
     */
    public static void initDir(String path){
        initDir(new File(path));
    }
    /**
     * 初始化目录文件
     * @param file
     * @throws IOException
     */
    public static void initDir(File file)  {
        if(file==null||file.exists()){
            return ;
        }
        initDir(file.getParentFile());
        if(!file.exists()){
            file.mkdir();
        }
    }
    /**
     * 写内容到文本文件当中
     * @param file
     * @param contentß
     */
    public static  void writeFileContent(File file,String content) throws IOException {
        writeFileContent(file,content,null);
    }
    /**
     * 写内容到文本文件当中
     * @param file
     * @param contentß
     */
    public static  void writeFileContent(File file,String content,String charSet) throws IOException {
        FileOutputStream fileOutputStream = null;
        if(!file.exists()){
            file.createNewFile();
        }
        fileOutputStream = new FileOutputStream(file);
        fileOutputStream.write(content.getBytes(StringUtils.isBlank(charSet)?"utf-8":charSet));
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    /**
     * 读取文件内容
     * @param file
     * @return
     */
    public static String readFileContent(File file) {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        try {
            reader = new BufferedReader(new FileReader(file));
            String tempStr;
            while ((tempStr = reader.readLine()) != null) {
                sbf.append(tempStr).append("\n");
            }
            reader.close();
            return sbf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
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


    public static void main(String[] args) throws Exception {

    }
}

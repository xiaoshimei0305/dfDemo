package com.idragon.dfdemo.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

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
}

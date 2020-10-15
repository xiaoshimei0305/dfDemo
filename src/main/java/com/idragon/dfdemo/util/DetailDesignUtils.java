package com.idragon.dfdemo.util;

import java.io.File;
import java.io.IOException;

/**
 * 详细设计生成工具列表
 * @author chenxinjun
 */
public class DetailDesignUtils {
    /**
     * 导出excel
     * @throws IOException
     */
    private static void exportExcel(String fileName) throws IOException {
        //数据存储到excel文档
        FileUtils.writeFileContent(new File(fileName),SwaggerInfoUtils.getToExcelStr(SwaggerInfoUtils.getFcmInterfaceInfo()),"GBK");
    }

    /**
     * 导出详细设计word文档
     */
    private static void exportWord(){


    }

    public static void main(String[] args) throws IOException {
        exportExcel("/Users/chenxinjun/Downloads/fcmInterface.csv");
        System.out.println("=================================");
    }
}

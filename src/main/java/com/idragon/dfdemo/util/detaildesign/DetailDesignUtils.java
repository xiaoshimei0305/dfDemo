package com.idragon.dfdemo.util.detaildesign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.FileUtils;
import com.idragon.dfdemo.util.SwaggerInfoUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        FileUtils.writeFileContent(new File(fileName), SwaggerInfoUtils.getToExcelStr(SwaggerInfoUtils.getFcmInterfaceInfo()),"GBK");
    }

    /**
     * 导出详细设计word文档
     */
    private static void exportWord(String excelName) throws IOException {
        JSONArray excelList = ExcelUtils.getExcelData(excelName).getJSONArray("FCM接口列表");
        JSONArray fcmSwaggerInfoList=SwaggerInfoUtils.getFcmInterfaceInfo();
        JSONObject addressMap=new JSONObject();
        if(fcmSwaggerInfoList!=null&&fcmSwaggerInfoList.size()>0){
            for(int i=0;i<fcmSwaggerInfoList.size();i++){
                JSONObject item=fcmSwaggerInfoList.getJSONObject(i);
                addressMap.put(item.getString("url"),item);
            }
        }

        List<MethodInfo> infoList=new ArrayList<>();
        if(excelList!=null&&excelList.size()>0){
            for(int i=0;i<excelList.size();i++){
                MethodInfo info=new MethodInfo(excelList.getJSONObject(i));
                info.initSwaggerInfo(addressMap);
                infoList.add(info);
            }
        }
       // System.out.println(infoList);
    }

    public static void main(String[] args) throws IOException {
        //exportExcel("/Users/chenxinjun/Downloads/fcmInterface.csv");
        exportWord("/Users/chenxinjun/Downloads/商城REST接口梳理.xlsx");
        System.out.println("=================================");
    }
}

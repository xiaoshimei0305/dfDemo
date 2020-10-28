package com.idragon.dfdemo.server;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.configure.FcmFileConfigure;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.FcmDataUtils;
import com.idragon.dfdemo.util.fcm.ServerParseUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import store.idragon.tool.excel.ExcelReadUtils;

import java.io.IOException;
import java.util.List;

/**
 * 数据获取服务
 * @author chenxinjun
 */
@Service
public class DataLoaderService {
    /**
     * 文件配置功能
     */
    FcmFileConfigure fcmFileConfigure;


    /**
     * 获取excel数据对象
     * @param excelFile excel文件
     * @return JSON格式数据
     * @throws IOException 网络异常
     */
    public JSONObject getJsonData(String excelFile) throws IOException {
        return ExcelReadUtils.getDataByFileName(fcmFileConfigure.getFileBasePath()+ fcmFileConfigure.getExcelDefaultName(excelFile));
    }

    /**
     * 获取实体列表
     * @param excelFile excel文件
     * @return 实体列表
     * @throws IOException 网络异常
     */
    public List<BeanInfo> getBeanList(String excelFile) throws IOException {
        return FcmDataUtils.getBeanInfos(getJsonData(excelFile),fcmFileConfigure.getEntitySheetName());
    }

    /**
     * 获取接口列表
     * @param excelFile excel文件
     * @return 接口列表
     * @throws IOException 网络异常
     */
    public List<InterfaceInfo> getInterfaceList(String excelFile) throws IOException {
        return FcmDataUtils.getInterfaceInfos(getJsonData(excelFile),fcmFileConfigure.getInterfaceSheetName());
    }

    /**
     * 获取实体解析对象
     * @param excelFile excel文件
     * @return 实体解析器
     * @throws IOException 网络异常
     */
    public BeanParseUtils getBeanParseUtils(String excelFile) throws IOException {
        List<BeanInfo>  beanInfos= getBeanList(excelFile);
        return new BeanParseUtils(beanInfos);
    }

    public ServerParseUtils getServerParseUtils(String excelFile) throws IOException {
        List<InterfaceInfo> list=getInterfaceList(excelFile);
        return new ServerParseUtils(list);
    }
    @Autowired
    public void setFcmFileConfigure(FcmFileConfigure fcmFileConfigure) {
        this.fcmFileConfigure = fcmFileConfigure;
    }
}

package com.idragon.dfdemo.server;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.configure.FcmFileCongiure;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.FcmDataUtils;
import com.idragon.dfdemo.util.fcm.ServerParseUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 数据获取服务
 * @author chenxinjun
 */
@Service
public class DataLoaderService {
    @Autowired
    FcmFileCongiure fcmFileCongiure;

    /**
     * 获取excel数据对象
     * @param excelFile excel文件
     * @return
     * @throws IOException
     */
    public JSONObject getJSONData(String excelFile) throws IOException {
        return ExcelUtils.getExcelData(fcmFileCongiure.getFileBasePath()+fcmFileCongiure.getExcelDefaultName(excelFile));
    }

    /**
     * 获取实体列表
     * @param excelFile excel文件
     * @return
     * @throws IOException
     */
    public List<BeanInfo> getBeanList(String excelFile) throws IOException {
        return FcmDataUtils.getBeanInfos(getJSONData(excelFile));
    }

    /**
     * 获取接口列表
     * @param excelFile excel文件
     * @return
     * @throws IOException
     */
    public List<InterfaceInfo> getInterfaceList(String excelFile) throws IOException {
        return FcmDataUtils.getInterfaceInfos(getJSONData(excelFile));
    }

    /**
     * 获取实体解析对象
     * @param excelFile excel文件
     * @return
     * @throws IOException
     */
    public BeanParseUtils getBeanParseUtils(String excelFile) throws IOException {
        List<BeanInfo>  beanInfos= getBeanList(excelFile);
        return new BeanParseUtils(beanInfos);
    }

    public ServerParseUtils getServerParseUtils(String excelFile) throws IOException {
        List<InterfaceInfo> list=getInterfaceList(excelFile);
        ServerParseUtils utils=new ServerParseUtils(list);
        return utils;
    }



}

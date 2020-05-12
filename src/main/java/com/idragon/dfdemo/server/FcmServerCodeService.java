package com.idragon.dfdemo.server;

import com.idragon.dfdemo.configure.FcmFileConfigure;
import com.idragon.dfdemo.constant.ServerCodeType;
import com.idragon.dfdemo.util.FileUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.ServerParseUtils;
import com.idragon.dfdemo.util.fcm.code.CodeLocationParseUtils;
import com.idragon.dfdemo.util.fcm.dto.ServiceInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;

/**
 * 控制层相关代码生成工具
 * @author chenxinjun
 */
@Service
public class FcmServerCodeService {
    @Autowired
    Configuration configuration;
    @Autowired
    FcmFileConfigure fcmFileConfigure;
    @Autowired
    DataLoaderService dataLoaderService;

    /**
     * 生成指定对象文档
     * @param beanCode 实体编码
     * @param excelFileName 数据文件
     * @param type
     * @throws IOException
     */
    public void buildServiceCodeFile(String excelFileName, ServerCodeType type) throws IOException, TemplateException {
        BeanParseUtils utils = dataLoaderService.getBeanParseUtils(excelFileName);
        ServerParseUtils serverUtils = dataLoaderService.getServerParseUtils(excelFileName);
        List<ServiceInfo> list = serverUtils.getServiceList();
        if(list!=null&&list.size()>0){
            for(ServiceInfo info:list){
                buildServer(info,utils,type);
            }
        }
    }
    /**
     * 创建实体服务
     * @param serviceInfo
     * @param utils
     * @throws IOException
     * @throws TemplateException
     */
    public void buildServer(ServiceInfo serviceInfo, BeanParseUtils utils,ServerCodeType type) throws IOException, TemplateException {
        if(serviceInfo!=null&&!checkServerFile(serviceInfo,type)){
            //解析相关依赖实体路径
            serviceInfo.initImportPackageList(utils,type);
            serviceInfo.setAuthor(fcmFileConfigure.getAuthor());
            FileUtils.initDir(getServerFileDir(serviceInfo,type));
            Template temp = configuration.getTemplate(type.getTemplateName());
            Writer out = new OutputStreamWriter(new FileOutputStream(getServerFile(serviceInfo,type)));
            temp.process(serviceInfo, out);
        }
    }


    /**
     * 判断实体对应代码文件是否存在
     * @param beanInfo
     * @return
     */
    public boolean checkServerFile(ServiceInfo serviceInfo,ServerCodeType type){
        if(serviceInfo!=null){
            //如果文件存在，加载文件不被修改部分代码
            //return getServerFile(serviceInfo,type).exists();
            serviceInfo.loadUnChangeCode(getServerFile(serviceInfo,type));
            return false;
        }
        return true;
    }


    /**
     * 获取实体对应文件路径
     * @param beanInfo
     * @return
     */
    public File getServerFile(ServiceInfo serviceInfo,ServerCodeType type){
        return new File(getServerFileDir(serviceInfo,type)+File.separator+serviceInfo.getCode()+type.getSuffixName()+".java");
    }

    /**
     * 获取实体文件目录
     * @param beanInfo 实体信息
     * @return
     */
    public String getServerFileDir(ServiceInfo serviceInfo,ServerCodeType type){
        return fcmFileConfigure.getCodeBasePath()+File.separator+ CodeLocationParseUtils.getModelLocation(serviceInfo.getModelName())
                +File.separator+CodeLocationParseUtils.getControllerPackageName(serviceInfo.getModelName(),type).replaceAll("\\.",File.separator);
    }



}

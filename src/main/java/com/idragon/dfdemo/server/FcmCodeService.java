package com.idragon.dfdemo.server;

import com.idragon.dfdemo.configure.FcmFileConfigure;
import com.idragon.dfdemo.constant.ServerCodeType;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.tool.base.StringUtils;
import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * 代码生成服务
 * @author chenxinjun
 */
@Service
@Slf4j
public class FcmCodeService {
    @Autowired
    DataLoaderService dataLoaderService;
    @Autowired
    Configuration configuration;
    @Autowired
    FcmFileConfigure fcmFileConfigure;
    @Autowired
    FcmBeanCodeService fcmBeanCodeService;
    @Autowired
    FcmServerCodeService fcmServerCodeService;
    /**
     * 生成指定对象文档
     * @param excelFileName 数据文件
     * @param beanCode 实体编码
     * @throws IOException
     */
    public void buildBeanCodeFile(String excelFileName,String beanCode) throws IOException, TemplateException {
        if(StringUtils.isBlank(beanCode)){
            buildBeanCodeFile(excelFileName);
            return ;
        }
        BeanParseUtils utils = dataLoaderService.getBeanParseUtils(excelFileName);
        BeanInfo beanInfo = utils.getBeanInfo(beanCode);
        if(beanInfo!=null){
            fcmBeanCodeService.buildBeanInfo(beanInfo,utils);
            log.info("找到了指定实体信息如下：{}",beanInfo);
        }else {
            log.info("未找到指定实体[{}]",beanCode);
        }
    }

    /**
     * 生成所有实体
     * @param excelFileName
     * @throws IOException
     * @throws TemplateException
     */
    public void buildBeanCodeFile(String excelFileName) throws IOException, TemplateException {
        BeanParseUtils utils = dataLoaderService.getBeanParseUtils(excelFileName);
        List<BeanInfo> beanInfos = utils.getAllBeans();
        for(BeanInfo item:beanInfos){
            fcmBeanCodeService.buildBeanInfo(item,utils);
        }
    }
    /**
     * 创建控制层代码
     * @param excelFileName
     * @throws IOException
     * @throws TemplateException
     */
    public void buildServiceCodeFile(String excelFileName, ServerCodeType type) throws IOException, TemplateException {
        fcmServerCodeService.buildServiceCodeFile(excelFileName,type);
    }


}

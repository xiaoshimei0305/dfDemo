package com.idragon.dfdemo.server;

import com.idragon.dfdemo.configure.FcmFileCongiure;
import com.idragon.dfdemo.constant.FcmCodeConstants;
import com.idragon.dfdemo.util.FileUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.code.CodeLocationParseUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;

/**
 * dto 相关实体生成服务
 * @author chenxinjun
 */
@Service
public class FcmBeanCodeService {
    @Autowired
    Configuration configuration;
    @Autowired
    FcmFileCongiure fcmFileCongiure;

    /**
     * 创建指定Bean代码
     * @param beanInfo
     */
    public void buildBeanInfo(BeanInfo beanInfo, BeanParseUtils utils) throws IOException, TemplateException {
        if(beanInfo!=null&&!checkBeanFile(beanInfo)){
            //解析相关依赖包
            beanInfo.initImportPackageList(utils);
            FileUtils.initDir(getBeanFileDir(beanInfo));
            Template temp = configuration.getTemplate(FcmCodeConstants.BEAN_CODE_MODEL_COMOND);
            Writer out = new OutputStreamWriter(new FileOutputStream(getBeanFile(beanInfo)));
            temp.process(beanInfo, out);
        }
    }

    /**
     * 判断实体对应代码文件是否存在
     * @param beanInfo
     * @return
     */
    public boolean checkBeanFile(BeanInfo beanInfo){
        if(beanInfo!=null){
            //如果文件存在，加载文件不被修改部分代码
            // return getBeanFile(beanInfo).exists();
            beanInfo.loadUnChangeCode(getBeanFile(beanInfo));
            return false;
        }
        return true;
    }


    /**
     * 获取实体对应文件路径
     * @param beanInfo
     * @return
     */
    public File getBeanFile(BeanInfo beanInfo){
        return new File(getBeanFileDir(beanInfo)+File.separator+beanInfo.getCode()+".java");
    }

    /**
     * 获取实体文件目录
     * @param beanInfo 实体信息
     * @return
     */
    public String getBeanFileDir(BeanInfo beanInfo){
        return fcmFileCongiure.getCodeBasePath()+File.separator+ CodeLocationParseUtils.getModelLocation(beanInfo.getModelName())
                +File.separator+beanInfo.getPackageName().replaceAll("\\.",File.separator);
    }

}

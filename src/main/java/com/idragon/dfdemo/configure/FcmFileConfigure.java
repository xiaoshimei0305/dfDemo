package com.idragon.dfdemo.configure;

import com.idragon.tool.base.StringUtils;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenxinjun
 */
@Data
@Component
@ConfigurationProperties(prefix = "fcmFile")
public class FcmFileConfigure {
    String fileBasePath;
    String excelDefaultName;
    String beansDir;
    String modelFileName;
    String resultFileName;
    String codeBasePath;
    String entitySheetName;
    String interfaceSheetName;
    String author;
    public String getExcelDefaultName(String excelFileName) {
        if(!StringUtils.isBlank(excelFileName)){
            return excelFileName;
        }
        return StringUtils.getValue(excelDefaultName,"930详设接口记录.xlsx");
    }

    /**
     * 获取实体文档配置路径
     * @return
     */
    public String getBeansDir(String beansDir) {
        if(!StringUtils.isBlank(beansDir)){
            return beansDir;
        }
        return StringUtils.getValue(this.beansDir,"beans");
    }

    /**
     * 获取模型文件
     * @param modelFileName
     * @return
     */
    public String getModelFileName(String modelFileName) {
        if(!StringUtils.isBlank(modelFileName)){
            return modelFileName;
        }
        return StringUtils.getValue(this.modelFileName,"model.docx");
    }

    /**
     * 获取接口文件名
     * @param resultFileName
     * @return
     */
    public String getResultFileName(String resultFileName) {
        if(!StringUtils.isBlank(resultFileName)){
            return resultFileName;
        }
        return StringUtils.getValue(this.resultFileName,"result.docx");
    }
}

package com.idragon.dfdemo.constant;

import lombok.Data;

/**
 * @author chenxinjun
 */
public enum ServerCodeType {
    /**
     * 控制层代码生成
     */
    CONTROLLER(".web","controller.ftl","Controller"),
    /**
     * 服务层代码生成
     */
    SERVICE(".server.service","service.ftl","Service"),
    /**
     * api代码生成
     */
    API(".server.adapter","api.ftl","Api");
     String packageName;
     String templateName;
     String suffixName;

    ServerCodeType(String packageName, String templateName, String suffixName) {
        this.packageName = packageName;
        this.templateName = templateName;
        this.suffixName = suffixName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getTemplateName() {
        return templateName;
    }

    public String getSuffixName() {
        return suffixName;
    }
}

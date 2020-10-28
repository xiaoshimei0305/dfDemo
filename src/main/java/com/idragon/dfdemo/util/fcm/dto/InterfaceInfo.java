package com.idragon.dfdemo.util.fcm.dto;

import lombok.Data;
import store.idragon.tool.base.StringUtils;

/**
 * @author chenxinjun
 * 接口信息
 */
@Data
public class InterfaceInfo{
    /**
     * 模块名称
     */
    private String modelName;
    /**
     * 方法名称【中文】
     */
    private String name;
    /**
     * 类名称
     */
    private String className;
    /**
     * 类Rest 路径
     */
    private String classPath;
    /**
     * 方法名名称
     */
    private String methodName;
    /**
     * 入参类型
     */
    private String inType;
    /**
     * 出参类型
     */
    private String outType;
    /**
     * 入参包路径
     */
    private String inPackagePath;
    /**
     * 出参包路径
     */
    private String outPackagePath;
    /**
     * 入参说明
     */
    private String inName;
    /**
     * 出参说明
     */
    private String outName;
    /**
     * 方法备注
     */
    private String remark;
    /**
     * Rest 方法路径
     */
    private String methodPath;
    /**
     * 是否需要rest接口
     */
    private boolean rest;
    /**
     * 是否需要api接口
     */
    private boolean api;
    /**
     * 标记是否为查询请求
     */
    private boolean queryReq;



    public void setInName(String inName) {
        this.inName = inName;
        if(StringUtils.isBlank(this.inName)&&!StringUtils.isBlank(this.inType)){
            this.inName=this.inType.toLowerCase();
        }
    }

    public void setOutName(String outName) {
        this.outName = outName;
        if(StringUtils.isBlank(this.outName)&&!StringUtils.isBlank(this.outType)){
            this.outName=this.outType.toLowerCase();
        }
    }

    public String getClassPath() {
        return StringUtils.getValue(classPath,"/api/"+className+"/");
    }

    public String getMethodPath() {
        return StringUtils.getValue(methodPath,methodName);
    }

    public String getRemark() {
        if(!StringUtils.isBlank(name)){
            remark=remark.replaceAll("\n"," ");
        }
        return remark;
    }
}

package com.idragon.dfdemo.util.fcm.dto;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author chenxinjun
 * 接口信息
 */
@Data
public class InterfaceInfo {
    private String modelName;
    private String name;
    private String className;
    private String methodName;
    private String inType;
    private String outType;
    private String inName;
    private String outName;
    private String remark;

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
}

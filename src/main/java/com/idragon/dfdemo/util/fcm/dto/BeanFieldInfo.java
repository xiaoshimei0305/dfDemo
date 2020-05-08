package com.idragon.dfdemo.util.fcm.dto;

import com.idragon.dfdemo.util.StringUtils;
import lombok.Data;

/**
 * @author chenxinjun
 * 实体字段信息
 */
@Data
public class BeanFieldInfo {
    private String name;
    private String code;
    /**
     * api对应的编码
     */
    private String apiCode;
    private String type;
    private String nameLength;
    /**
     * rest 是否必须填写
     */
    private boolean require;
    private String remark;
    /**
     * api数据是否比天
     */
    private boolean apiRequire;

    public String getApiCode() {
        return StringUtils.isBlank(apiCode)?code:apiCode;
    }
}

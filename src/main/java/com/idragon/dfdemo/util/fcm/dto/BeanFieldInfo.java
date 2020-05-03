package com.idragon.dfdemo.util.fcm.dto;

import lombok.Data;

/**
 * @author chenxinjun
 * 实体字段信息
 */
@Data
public class BeanFieldInfo {
    private String name;
    private String code;
    private String type;
    private String nameLength;
    private boolean require;
    private String remark;

}

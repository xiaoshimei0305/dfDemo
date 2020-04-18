package com.idragon.dfdemo.util.fcm.dto;

import lombok.Data;

import java.util.List;

/**
 * @author chenxinjun
 * 实体信息
 */
@Data
public class BeanInfo {
    private String modelName;
    private String name;
    private String code;
    private String remark;
    private EntityTypeEnum type;
    private List<BeanFieldInfo> fieldList;
}

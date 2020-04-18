package com.idragon.dfdemo.util.fcm.dto;

/**
 * @author chenxinjun
 * 实体类型枚举
 */

public enum  EntityTypeEnum {
    /**
     * 查询类型
     */
    QUERY("查询实体"),
    /**
     * 返回类型
     */
    INFO("结果实体"),
    /**
     * 修改类型
     */
    COMMAND("修改实体"),
    /**
     * 参数类型
     */
    PARAM("参数实体");
    private String name;

    EntityTypeEnum(String name) {
        this.name = name;
    }

}

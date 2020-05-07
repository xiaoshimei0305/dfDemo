package com.idragon.dfdemo.util.fcm.dto;

/**
 * @author chenxinjun
 * 实体类型枚举
 */

public enum  EntityTypeEnum {
    /**
     * 查询类型
     */
    QUERY("查询实体","bean-query.ftl","Query"),
    /**
     * 查询类型(REST 接口使用)
     */
    RESTQUERY("查询实体","bean-rest-query.ftl","RestQuery"),
    /**
     * 返回类型
     */
    INFO("结果实体","bean-info.ftl",""),
    /**
     * 修改类型
     */
    COMMAND("修改实体","bean-command.ftl",""),
    /**
     * 参数类型
     */
    PARAM("参数实体","bean-param.ftl","");
    private String name;
    private String templateName;
    private String suffixName;

    EntityTypeEnum(String name, String templateName, String suffixName) {
        this.name = name;
        this.templateName = templateName;
        this.suffixName = suffixName;
    }

    public String getSuffixName() {
        return suffixName;
    }

    public String getName() {
        return name;
    }

    public String getTemplateName() {
        return templateName;
    }
}

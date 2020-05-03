package com.idragon.dfdemo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 内部字符串处理工具
 * @author chenxinjun
 */
@Slf4j
public class StringUtils  extends org.apache.commons.lang3.StringUtils {
    /**
     * 字符串匹配表达式
     */
   private static Pattern  pattern= Pattern.compile("[Ll]ist<[\\s]*([\\S]*)[\\s]*>");
    /**
     * 获取定义字段类型
     * @param typeName
     * @return
     */
    public static String getFieldType(String typeName){
        Matcher matcher = pattern.matcher(typeName);
        if(matcher.find()) {
            String result=matcher.group(1);
            return result;
        }
        return typeName;
    }

    /**
     * 获取值
     * @param value 目标值
     * @param defaultValue 为空是默认值
     * @return
     */
    public static String getValue(String value,String defaultValue){
        if(!StringUtils.isBlank(value)){
            return value;
        }
        return defaultValue;
    }


}

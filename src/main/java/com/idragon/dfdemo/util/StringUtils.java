package com.idragon.dfdemo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 内部字符串处理工具
 * @author chenxinjun
 */
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
            return matcher.group(1);
        }
        return typeName;
    }

    public static void main(String[] args) {
        System.out.println(getFieldType("cherry"));
        System.out.println(getFieldType("list< merry >"));
    }
}

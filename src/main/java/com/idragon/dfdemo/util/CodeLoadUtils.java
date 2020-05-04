package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 代码加载工具
 */
public class CodeLoadUtils {
    /**
     * 定义固定代码前后标签
     *
     * //[编码]CodeStart
     *  这中间的代码不会被覆盖
     * //CodeEnd
     */
    private static  Pattern pattern=Pattern.compile("//\\[([\\S]*)\\]CodeStart([\\s\\S]*?)//CodeEnd");


    /**
     * 加载不修改部分代码
     * @return
     */
    public static  Map<String,String> getUnChangedCode(File file){
        Map<String,String> contents=new HashMap<>(16);
        if(file.exists()){
            String content=FileUtils.readFileContent(file);
            if(!StringUtils.isBlank(content)){
                Matcher m = pattern.matcher(content);
                while(m.find()){
                    contents.put(m.group(1),m.group(2));
                    System.out.println("hah:"+m.group(1)+",name:"+m.group(2));
                }
            }
        }
        return contents;
    }
}
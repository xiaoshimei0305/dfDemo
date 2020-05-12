package com.idragon.dfdemo.util.fcm.code;

import com.idragon.dfdemo.constant.FcmCodeConstants;
import com.idragon.dfdemo.constant.ServerCodeType;
import com.idragon.dfdemo.util.StringUtils;
import com.idragon.dfdemo.util.fcm.dto.EntityTypeEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * 代码位置解析工具柜
 * @author chenxinjun
 */
public class CodeLocationParseUtils {

    private static Map<String,String> mapperCache;
    /**
     * 模型名称与编码转换工作
     */
    public static String getModelCode(String modelName){
       String name= getMapper().get(modelName);
       if(StringUtils.isBlank(name)){
            name="cms";
        }
        return name;
    }

    private static Map<String,String> getMapper(){
        if(mapperCache == null){
            mapperCache=new HashMap<>();
            mapperCache.put("购物车","trade");
            mapperCache.put("订购流程","trade");
            mapperCache.put("订单中心","trade");
            mapperCache.put("逆向交易","trade");
            mapperCache.put("注册登录","member");
        }
        return mapperCache;
    }


    public static String getControllerPackageName(String modelName,ServerCodeType type){
        return getModelPackage(modelName)+type.getPackageName();
    }
    /**
     * 获取指定模块，指定类型实体的包名称
     * @param model
     * @param type
     * @return
     */
    public static String getBeanPackageName(String modelName, EntityTypeEnum type){
        String packageName=null;
        String projectPackageName=getModelPackage(modelName);
        switch (type){
            case INFO:
                packageName=".dto.info";
                break;
            case PARAM:
                packageName=".dto.param";
                break;
            case COMMAND:
                packageName=".dto.commond";
                break;
            default:
                packageName=".dto.query";
        }
        return projectPackageName+packageName;
    }

    /**
     * 获取项目总体包路径
     * @param modelName
     * @return
     */
    public static String getModelPackage(String modelName){
        return FcmCodeConstants.MODEL_PACKAGE_BASE_MODEL.replace("idrModel",getModelCode(modelName));
    }


    /**
     * 获取项目总体包路径
     * @param modelName
     * @return
     */
    public static String getModelLocation(String modelName){
        return FcmCodeConstants.MODEL_LOCATION_MODEL.replace("idrModel",getModelCode(modelName));
    }



}

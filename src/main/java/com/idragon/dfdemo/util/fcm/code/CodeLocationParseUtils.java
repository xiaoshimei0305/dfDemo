package com.idragon.dfdemo.util.fcm.code;

import com.idragon.dfdemo.constant.FcmCodeConstants;
import com.idragon.dfdemo.util.fcm.dto.EntityTypeEnum;

/**
 * 代码位置解析工具柜
 * @author chenxinjun
 */
public class CodeLocationParseUtils {
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

    /**
     * 模型名称与编码转换工作
     */
    private static String getModelCode(String modelName){
        if("购物车".equalsIgnoreCase(modelName)||"订购流程".equalsIgnoreCase(modelName)||"订单中心".equalsIgnoreCase(modelName)){
            return "trade";
        }
        return "cms";
    }

}

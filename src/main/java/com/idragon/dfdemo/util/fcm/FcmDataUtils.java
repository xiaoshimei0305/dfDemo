package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.fcm.dto.BeanFieldInfo;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.EntityTypeEnum;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxinjun
 * 接口信息获取工具
 */
public class FcmDataUtils {

    /**
     * 获取接口信息
     *
     * @param sheetInfo
     * @return
     */
    public static List<InterfaceInfo> getInterfaceInfos(JSONObject sheetInfo,String sheetName) {
        List<InterfaceInfo> interfaceInfos = new ArrayList<>();
        if (sheetInfo != null) {
            JSONArray ja = sheetInfo.getJSONArray(sheetName);
            if (ja != null) {
                for (int i = 0; i < ja.size(); i++) {
                    JSONObject item = ja.getJSONObject(i);
                    String methodName = item.getString("方法名称");
                    if (!StringUtils.isBlank(methodName)) {
                        InterfaceInfo interfaceInfo = new InterfaceInfo();
                        interfaceInfo.setMethodName(methodName);
                        interfaceInfo.setModelName(item.getString("模块"));
                        interfaceInfo.setName(item.getString("名称"));
                        interfaceInfo.setClassName(item.getString("类名称"));
                        interfaceInfo.setClassPath(item.getString("类路径"));
                        interfaceInfo.setInType(item.getString("入参类型"));
                        interfaceInfo.setOutType(item.getString("出参类型"));
                        interfaceInfo.setInName(item.getString("入参名称"));
                        interfaceInfo.setOutName(item.getString("出参名称"));
                        interfaceInfo.setRemark(item.getString("备注"));
                        interfaceInfo.setMethodPath(item.getString("方法路径"));
                        interfaceInfo.setRest(!"否".equalsIgnoreCase(item.getString("REST")));
                        interfaceInfo.setApi(!"否".equalsIgnoreCase(item.getString("API")));
                        interfaceInfos.add(interfaceInfo);
                    }
                }
            }
        }
        return interfaceInfos;
    }

    /**
     * 获取实体信息信息列表
     *
     * @param sheetInfo
     * @return
     */
    public static List<BeanInfo> getBeanInfos(JSONObject sheetInfo,String sheetName) {
        List<BeanInfo> beanList = new ArrayList();
        if (sheetInfo != null) {
            JSONArray ja = sheetInfo.getJSONArray(sheetName);
            if (ja != null && ja.size() > 0) {
                int length = ja.size();
                for (int i = 0; i < length; i++) {
                    JSONObject item = ja.getJSONObject(i);
                    String beanCode = item.getString("实体编码").trim();
                    BeanInfo beforeBean = null;
                    if(beanList.size()>0){
                        beforeBean = beanList.get(beanList.size()-1);
                    }
                    //判断当前实体编码是否一致
                    if (isChangeNewBean(beanCode,beforeBean)) {
                        BeanInfo currentBeanInfo = new BeanInfo();
                        currentBeanInfo.setCode(beanCode);
                        currentBeanInfo.setModelName(item.getString("模块"));
                        currentBeanInfo.setName(item.getString("实体名称"));
                        String valueType = item.getString("实体类型");
                        if (StringUtils.isBlank(valueType)) {
                            valueType = "query";
                        }
                        currentBeanInfo.setType(EntityTypeEnum.valueOf(valueType.toUpperCase()));
                        currentBeanInfo.setRemark(item.getString("实体说明"));
                        List<BeanFieldInfo> fieldList = new ArrayList<>();
                        currentBeanInfo.setFieldList(fieldList);
                        setField(currentBeanInfo,item);
                        beanList.add(currentBeanInfo);
                    } else {
                        if(beforeBean!=null){
                            setField(beforeBean,item);
                        }
                    }
                }
            }
        }
        return beanList;
    }

    /**
     * 判断实体编码是否有变更
     * @param currentCode
     * @param beforeBean
     * @return
     */
    private static boolean isChangeNewBean(String currentCode,BeanInfo beforeBean){
        return StringUtils.isNotBlank(currentCode)||(beforeBean==null||currentCode.equalsIgnoreCase(beforeBean.getCode()));
    }


    /**
     * 实体添加字段值
     * @param beanInfo
     * @param item
     */
    private static void setField(BeanInfo beanInfo,JSONObject item){
        if(beanInfo!=null&&item!=null){
            BeanFieldInfo field = new BeanFieldInfo();
            field.setCode(item.getString("字段编码"));
            field.setApiCode(item.getString("API编码"));
            field.setName(item.getString("字段名称"));
            field.setRemark(item.getString("备注"));
            field.setRequire(!"否".equalsIgnoreCase(item.getString("REST是否必填")));
            field.setType(item.getString("字段类型"));
            field.setNameLength(item.getString("长度"));
            field.setApiRequire(!"否".equalsIgnoreCase(item.getString("API是否必填")));
            if(!StringUtils.isBlank(field.getCode())){
                beanInfo.getFieldList().add(field);
            }
        }
    }
}

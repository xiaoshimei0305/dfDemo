package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.fcm.dto.BeanFieldInfo;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.EntityTypeEnum;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.apache.commons.collections4.CollectionUtils;
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
    public static List<InterfaceInfo> getInterfaceInfos(JSONObject sheetInfo) {
        List<InterfaceInfo> interfaceInfos = new ArrayList<>();
        if (sheetInfo != null) {
            JSONArray ja = sheetInfo.getJSONArray("接口列表");
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
                        interfaceInfo.setInType(item.getString("入参类型"));
                        interfaceInfo.setOutType(item.getString("出参类型"));
                        interfaceInfo.setInName(item.getString("入参名称"));
                        interfaceInfo.setOutName(item.getString("出参名称"));
                        interfaceInfo.setRemark(item.getString("备注"));
                        interfaceInfo.setRestUrl(item.getString("Rest访问地址"));
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
    public static List<BeanInfo> getBeanInfos(JSONObject sheetInfo) {
        List<BeanInfo> beanList = new ArrayList();
        if (sheetInfo != null) {
            JSONArray ja = sheetInfo.getJSONArray("实体表");
            if (ja != null && ja.size() > 0) {
                int length = ja.size();
                for (int i = 0; i < length; i++) {
                    JSONObject item = ja.getJSONObject(i);
                    String beanCode = item.getString("实体编码").trim();
                    if (StringUtils.isNotBlank(beanCode)) {
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
                        BeanFieldInfo field = setField(item);
                        List<BeanFieldInfo> fieldList = new ArrayList<>();
                        fieldList.add(field);
                        currentBeanInfo.setFieldList(fieldList);
                        beanList.add(currentBeanInfo);
                    } else {
                        if (CollectionUtils.isEmpty(beanList)) {
                            continue;
                        }
                        int listLength = beanList.size();
                        int listIndex = listLength - 1;
                        BeanInfo currentBeanInfo = beanList.get(listIndex);
                        beanList.remove(currentBeanInfo);
                        BeanFieldInfo beanInfo = setField(item);
                        currentBeanInfo.getFieldList().add(beanInfo);
                        beanList.add(currentBeanInfo);
                    }
                }
            }
        }
        return beanList;
    }



    private static BeanFieldInfo setField(JSONObject item) {
        BeanFieldInfo field = new BeanFieldInfo();
        field.setCode(item.getString("字段编码"));
        field.setName(item.getString("字段名称"));
        field.setRequire("是".equalsIgnoreCase(item.getString("是否必填")));
        field.setType(item.getString("字段类型"));
        field.setNameLength(item.getString("长度"));
        return field;
    }
}

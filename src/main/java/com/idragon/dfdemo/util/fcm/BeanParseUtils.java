package com.idragon.dfdemo.util.fcm;

import com.idragon.dfdemo.util.StringUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanFieldInfo;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;

import java.util.*;

/**
 * 实体依赖分析工具
 */
public class BeanParseUtils {
    /**
     * 实体容器
     */
    private Map<String, BeanInfo> beanMap;

    /**
     * 获取所有实体对象
     * @return
     */
    public List<BeanInfo> getAllBeans(){
        List<BeanInfo> list=new ArrayList<>();
        if(this.beanMap!=null&&this.beanMap.size()>0){
            Set<String> keys = this.beanMap.keySet();
            for(String key:keys){
                list.add(this.beanMap.get(key));
            }
        }
        return list;
    }


    public BeanParseUtils(List<BeanInfo> beanList) {
        this.beanMap=new HashMap<>();
        if(beanList!=null&&beanList.size()>0){
            for(BeanInfo beanInfo:beanList){
               addBean(beanInfo);
            }
        }
    }


    public BeanInfo getBeanInfo(String code){
        if(!StringUtils.isBlank(code)){
            return this.beanMap.get(code.trim());
        }
        return null;
    }


    /**
     * 添加实体信息
     * @param beanInfo
     */
    public void addBean(BeanInfo beanInfo){
        if(beanInfo!=null&&!StringUtils.isBlank(beanInfo.getCode())){
            this.beanMap.put(beanInfo.getCode(),beanInfo);
        }
    }

    /**
     * 解析当前实体所有相关依赖对象
     * @param beanInfo
     * @return
     */
    public List<BeanInfo> parseBeanList(BeanInfo beanInfo){
        List<BeanInfo> list=new ArrayList<>();
        parseBeanList(beanInfo,list);
        return list;
    }

    /**
     * 解析当前实体所有相关依赖对象
     * @param beanInfo
     * @return
     */
    private void parseBeanList(BeanInfo beanInfo,List<BeanInfo> list){
        if(list!=null&&beanInfo!=null&&!StringUtils.isBlank(beanInfo.getCode())){
            list.add(beanInfo);
            List<BeanFieldInfo> fields = beanInfo.getFieldList();
            if(fields!=null&&fields.size()>0){
                for(BeanFieldInfo fieldInfo:fields){
                    String code=StringUtils.getFieldType(fieldInfo.getType());
                    parseBeanList(getBeanInfo(code),list);
                }
            }
        }
    }
}

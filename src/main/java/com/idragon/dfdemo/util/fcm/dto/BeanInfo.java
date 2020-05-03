package com.idragon.dfdemo.util.fcm.dto;

import com.idragon.dfdemo.util.StringUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.code.CodeLocationParseUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxinjun
 * 实体信息
 */
@Data
public class BeanInfo {
    private String modelName;
    private String name;
    private String code;
    private String remark;
    private EntityTypeEnum type;
    private String packageName;
    private List<BeanFieldInfo> fieldList;
    /**
     * 实体关联的包列表
     */
    private List<String> importPackageList;

    /**
     * 获取包类型
     * @return
     */
    public String getPackageName() {
        if(StringUtils.isBlank(packageName)){
            this.packageName= CodeLocationParseUtils.getBeanPackageName(modelName,type);
        }
        return this.packageName;
    }

    /**
     * 解析当前包引入的相关实体
     * @param utils
     */
    public void initImportPackageList(BeanParseUtils utils){
        importPackageList=new ArrayList<>();
        if(fieldList!=null && fieldList.size()>0){
            for(BeanFieldInfo info:fieldList){
                if(info!=null){
                    BeanInfo beanInfo=utils.getBeanInfo(StringUtils.getFieldType(info.getType()));
                    if(beanInfo!=null){
                        String packageList=CodeLocationParseUtils.getBeanPackageName(beanInfo.getModelName(),beanInfo.getType())+"."+beanInfo.getCode();
                        importPackageList.add(packageList);
                    }
                }
            }
        }
    }


}

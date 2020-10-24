package com.idragon.dfdemo.util.fcm.dto;

import com.idragon.dfdemo.util.CodeLoadUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.code.CodeLocationParseUtils;
import com.idragon.tool.base.StringUtils;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    private String author;
    private List<BeanFieldInfo> fieldList;
    /**
     * 实体关联的包列表
     */
    private List<String> importPackageList;
    /**
     * 缓存不被修改的代码部分
     */
    private Map<String,String> unChangedCode;

    /**
     * 加载不被修改的代码部分
     * @param file
     */
    public void loadUnChangeCode(File file){
        this.unChangedCode=CodeLoadUtils.getUnChangedCode(file);
    }

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
                    BeanInfo beanInfo=utils.getBeanInfo(BeanParseUtils.getFieldType(info.getType()));
                    if(beanInfo!=null){
                        String packageList=CodeLocationParseUtils.getBeanPackageName(beanInfo.getModelName(),beanInfo.getType())+"."+beanInfo.getCode();
                        importPackageList.add(packageList);
                    }
                }
            }
        }
    }

    public String getStr(){
        StringBuffer sb=new StringBuffer("参数：");
        if(this.fieldList!=null&&this.fieldList.size()>0){
            for(BeanFieldInfo info:this.fieldList){
                sb.append("|").append(info.getName());
            }
        }
        return sb.toString();
    }


}

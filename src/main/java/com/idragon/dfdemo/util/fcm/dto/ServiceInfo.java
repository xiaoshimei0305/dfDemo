package com.idragon.dfdemo.util.fcm.dto;

import com.idragon.dfdemo.constant.ServerCodeType;
import com.idragon.dfdemo.util.CodeLoadUtils;
import com.idragon.dfdemo.util.StringUtils;
import com.idragon.dfdemo.util.fcm.BeanParseUtils;
import com.idragon.dfdemo.util.fcm.FreeMarkUtils;
import com.idragon.dfdemo.util.fcm.code.CodeLocationParseUtils;
import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chenxinjun
 * 服务定义对象
 */
@Data
public class ServiceInfo {

    private String name;
    private String code;
    private String modelName;
    private String classPath;;
    private String modelCode;
    private String author;
    /**
     * 实体关联的包列表
     */
    private List<String> importPackageList;

    private List<InterfaceInfo> methods;
    /**
     * 缓存不被修改的代码部分
     */
    private Map<String,String> unChangedCode;

    private FreeMarkUtils utils=new FreeMarkUtils();

    /**
     * 加载不被修改的代码部分
     * @param file
     */
    public void loadUnChangeCode(File file){
        this.unChangedCode= CodeLoadUtils.getUnChangedCode(file);
    }

    public void addInterface(InterfaceInfo interfaceInfo){
        if(methods==null){
            this.name=interfaceInfo.getName();
            this.code=interfaceInfo.getClassName();
            this.modelName=interfaceInfo.getModelName();
            this.classPath=interfaceInfo.getClassPath();
            this.modelCode=CodeLocationParseUtils.getModelCode(this.modelName);
            methods=new ArrayList<>();
        }
        if(interfaceInfo!=null){
            methods.add(interfaceInfo);
        }
    }

    /**
     * 对方法内说有的参数进行
     * @param utils
     * @param type
     */
    public  void initImportPackageList(BeanParseUtils utils, ServerCodeType type){
        importPackageList=new ArrayList<>();
        if(utils!=null){
            if(methods!=null&&methods.size()>0){
                for(int i=0;i<methods.size();i++){
                    InterfaceInfo info=methods.get(i);
                    String inPath=getImportPath(info.getInType(),utils);
                    if(!StringUtils.isBlank(inPath)){
                        BeanInfo inBean=utils.getBeanInfo(info.getInType());
                        switch (inBean.getType()){
                            case QUERY:
                                info.setQueryReq(true);
                                break;
                            default:
                                info.setQueryReq(false);
                        }
                        if(info.isQueryReq()){
                            switch (type){
                                case CONTROLLER:
                                    inPath+="RestQuery";
                                    info.setInType(info.getInType()+"RestQuery");
                                    break;
                                default:
                                    inPath+="Query";
                                    info.setInType(info.getInType()+"Query");
                            }

                        }
                        importPackageList.add(inPath);
                    }
                    String outPath=getImportPath(info.getOutType(),utils);
                    if(!StringUtils.isBlank(inPath)){
                        importPackageList.add(outPath);
                    }
                }
            }
        }
    }

    /**
     * 获取实体引入的包路径
     * @param code
     * @param utils
     * @return
     */
    private String getImportPath(String code,BeanParseUtils utils){
        String inType= StringUtils.capitalize(code);
        if(!StringUtils.isBlank(inType)){
            BeanInfo beanInfo = utils.getBeanInfo(inType);
            if(beanInfo!=null){
                return beanInfo.getPackageName()+"."+inType;
            }
        }
        return null;
    }
}

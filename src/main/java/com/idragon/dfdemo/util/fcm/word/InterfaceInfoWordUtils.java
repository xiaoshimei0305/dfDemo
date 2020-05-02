package com.idragon.dfdemo.util.fcm.word;

import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.BeanDepenceUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author chenxinjun
 * 接口信息替换
 */
public class InterfaceInfoWordUtils {
    /**
     * 接口模型文档地址
     *
     */
    private String interfaceModelPath;
    /**
     * 实体模型工具
     */
    private BeanInfoWordUtils beanInfoWordUtils;


    /**
     * word操作工具
     */
    private WordUtils utils;

    public InterfaceInfoWordUtils(String interfaceModelPath, BeanInfoWordUtils beanInfoWordUtils) {
        this.interfaceModelPath = interfaceModelPath;
        this.beanInfoWordUtils = beanInfoWordUtils;
        this.utils = new WordUtils();
        if(StringUtils.isBlank(interfaceModelPath)){
            this.interfaceModelPath=this.getClass().getResource("/").getPath()+"doc/interfaceModel.docx";
        }
        if(this.beanInfoWordUtils==null){
            this.beanInfoWordUtils=new BeanInfoWordUtils();
        }
    }

    public InterfaceInfoWordUtils() {
        this(null,null);
    }

    public XWPFDocument getInterfaceDocument() throws IOException {
        return utils.getDocument(this.interfaceModelPath);
    }

    /**
     * 把文档接口内容导入文档中
     * @param doc
     * @param interfaceInfos
     * @param beanDepenceUtils
     * @return
     * @throws Exception
     */
    public XWPFDocument importDocumentByMethods(XWPFDocument doc, List<InterfaceInfo> interfaceInfos, BeanDepenceUtils beanDepenceUtils) throws Exception {
        if(interfaceInfos!=null&&interfaceInfos.size()>0){
            for(int i=0;i<interfaceInfos.size();i++){
                InterfaceInfo item=interfaceInfos.get(i);
                String key="idr_method_"+item.getMethodName();
                System.out.println("start find[key:"+key+",name:"+item.getName()+"]....");
                doc=utils.importModelToDocument(doc,getInterfaceDocumentWithData(item,beanDepenceUtils),key);
                System.out.println("finish find[key:"+key+",name:"+item.getName()+"]....");
            }
        }
        return doc;
    }


    /**
     * 完成接口文档输出
     * @param interfaceInfo
     * @param beanDepenceUtils
     * @return
     */
    public XWPFDocument getInterfaceDocumentWithData(InterfaceInfo interfaceInfo, BeanDepenceUtils beanDepenceUtils) throws Exception {
        XWPFDocument document=getInterfaceDocument();
        if(interfaceInfo!=null){
            utils.replaceText(document,"idr_interface_name",interfaceInfo.getName());
            utils.replaceText(document,"idr_interface_remark",interfaceInfo.getRemark());
            utils.replaceText(document,"idr_interface_inType",interfaceInfo.getInType());
            utils.replaceText(document,"idr_interface_outType",interfaceInfo.getOutType());
            utils.replaceText(document,"idr_interface_inName",interfaceInfo.getInName());
            utils.replaceText(document,"idr_interface_outName",interfaceInfo.getOutName());
            utils.replaceText(document,"idr_interface_restUrl",interfaceInfo.getRestUrl());
            document=utils.importModelToDocument(document,
                    beanInfoWordUtils.getBeanDocumentWithData(beanDepenceUtils.getBeanInfo(interfaceInfo.getOutType()),beanDepenceUtils),"idr_bean_resp");
            document=utils.importModelToDocument(document,
                    beanInfoWordUtils.getBeanDocumentWithData(beanDepenceUtils.getBeanInfo(interfaceInfo.getInType()),beanDepenceUtils),"idr_bean_req");
        }
        return document;
    }
}

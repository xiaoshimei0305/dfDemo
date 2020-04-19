package com.idragon.dfdemo.util.fcm.word;

import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
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
     * 实体信息登记容器
     */
    private Map<String, BeanInfo> beanInfoMap;


    /**
     * word操作工具
     */
    private WordUtils utils;

    public InterfaceInfoWordUtils(String interfaceModelPath, BeanInfoWordUtils beanInfoWordUtils) {
        this.interfaceModelPath = interfaceModelPath;
        this.beanInfoWordUtils = beanInfoWordUtils;
        this.utils = new WordUtils();
        if(StringUtils.isBlank(interfaceModelPath)){
            this.interfaceModelPath=this.getClass().getResource("/").getPath()+"/doc/interfaceModel.docx";
        }
        if(this.beanInfoWordUtils==null){
            this.beanInfoWordUtils=new BeanInfoWordUtils();
        }
        this.beanInfoMap=new HashMap<>();
    }

    public InterfaceInfoWordUtils() {
        this(null,null);
    }

    /**
     * 注册实体信息
     * @param beanInfos
     */
    public void registerBeanInfo(List<BeanInfo> beanInfos){
        if(beanInfos!=null&&beanInfos.size()>0){
            for(BeanInfo beanInfo:beanInfos){
                this.beanInfoMap.put(beanInfo.getCode(),beanInfo);
            }
        }
    }


    public XWPFDocument getInterfaceDocument() throws IOException {
        return utils.getDocument(this.interfaceModelPath);
    }

    /**
     * 完成接口文档输出
     * @param interfaceInfo
     * @return
     */
    public XWPFDocument getInterfaceDocumentWithData(InterfaceInfo interfaceInfo) throws Exception {
        XWPFDocument document=getInterfaceDocument();
        if(interfaceInfo!=null){
            utils.replaceText(document,"idr_interface_name",interfaceInfo.getName());
            utils.importModelToDocument(document,
                    beanInfoWordUtils.getBeanDocumentWithData(this.beanInfoMap.get(interfaceInfo.getOutType())),"idr_bean_resp");
            utils.importModelToDocument(document,
                    beanInfoWordUtils.getBeanDocumentWithData(this.beanInfoMap.get(interfaceInfo.getInType())),"idr_bean_req");
        }
        return document;
    }


    /**
     * 段落文档替换工具
     * @param doc
     * @param interfaceInfo
     */
    public   void replaceDoc(XWPFDocument doc, InterfaceInfo interfaceInfo) {
        WordUtils tools=new WordUtils();
        //段落替换
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInParagraph(para, interfaceInfo);
        }
        //表格替换
        List<XWPFTable> tables = doc.getTables();
        for(int i=0;i<tables.size();i++){
            XWPFTable table=tables.get(i);
            tools.addOrRemoveRow(table,2,2);
        }
        System.out.println(tables.size());
    }
    public  void replaceInParagraph(XWPFParagraph para,InterfaceInfo interfaceInfo){
        utils.replaceInParagraph(para, "${name}",interfaceInfo.getName());
    }
}

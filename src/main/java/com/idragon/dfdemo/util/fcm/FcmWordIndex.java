package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import com.idragon.dfdemo.util.fcm.word.BeanInfoWordUtils;
import com.idragon.dfdemo.util.fcm.word.InterfaceInfoWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

/**
 *
 * @author chenxinjun
 * FCM word文档生产工具
 */
public class FcmWordIndex {
    public FcmWordIndex() {
    }

    public static void main(String[] args) throws Exception {
        //原始录入数据
        String sourceData="/Users/chenxinjun/Downloads/930.xlsx";
//        // doc文档输出工具
        String beanListDoc = "/Users/chenxinjun/Downloads/beanList.docx";
        JSONObject data= ExcelUtils.getExcelData(sourceData);
        // 处理实体信息
        List<BeanInfo> booleanList = FcmDataUtils.getBeanInfos(data);
        BeanInfoWordUtils beanInfoWordUtils=new BeanInfoWordUtils();
        WordUtils utils=new WordUtils();
        XWPFDocument document = beanInfoWordUtils.getBeanListDocument(booleanList);
        utils.exportFile(document,beanListDoc);
        
        //处理接口信息    
        String interfaceListDoc = "/Users/chenxinjun/Downloads/interfaceList.docx";
        List<InterfaceInfo> interfaceBeanInfos = FcmDataUtils.getInterfaceInfos(data);
        InterfaceInfoWordUtils interfaceInfoWordUtils=new InterfaceInfoWordUtils();
        interfaceInfoWordUtils.registerBeanInfo(booleanList);
        XWPFDocument listDoc = interfaceInfoWordUtils.getInterfaceDocumentWithData(interfaceBeanInfos.get(0));
        utils.exportFile(listDoc,interfaceListDoc);

    }
}

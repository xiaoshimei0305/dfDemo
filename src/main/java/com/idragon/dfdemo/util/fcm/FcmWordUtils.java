package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import com.idragon.dfdemo.util.fcm.word.BeanInfoWordUtils;
import com.idragon.dfdemo.util.fcm.word.InterfaceInfoWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.List;

/**
 *
 * @author chenxinjun
 * FCM word文档生产工具
 */
public class FcmWordUtils {
    /**
     * 构建过程中的临时文件存放位置
     */
    public static String wordTemp="/Users/rocking/Downloads/temp.docx";
    public FcmWordUtils() {
    }

    /**
     * word文档生成工具
     * @param tagertFilesName  生成目标文件
     * @param modelFileName  生成模版文件
     * @param dataSourceFileName  数据来源excel文件
     */
    public void buildWordDocument(String tagertFilesName,String modelFileName,String dataSourceFileName) throws Exception {
        JSONObject data= ExcelUtils.getExcelData(dataSourceFileName);
        // 处理实体信息
        List<BeanInfo> booleanList = FcmDataUtils.getBeanInfos(data);
        WordUtils utils=new WordUtils();
        List<InterfaceInfo> interfaceBeanInfos = FcmDataUtils.getInterfaceInfos(data);
        //接口参数初始化
        InterfaceInfoWordUtils interfaceInfoWordUtils=new InterfaceInfoWordUtils();
        interfaceInfoWordUtils.registerBeanInfo(booleanList);
        XWPFDocument contentDoc = utils.getDocument(modelFileName);
        contentDoc= interfaceInfoWordUtils.importDocumentByMethods(contentDoc,interfaceBeanInfos);
        //完成文档内容，生成地址
        utils.exportFile(contentDoc,tagertFilesName);
    }


    public static void main(String[] args) throws Exception {
        FcmWordUtils utils=new FcmWordUtils();
        String docx1 = "/Users/rocking/Downloads/hh1.docx";
        // String docx2 = "/Users/rocking/Desktop/亚信2020.1.1/上海东方购项目/交易/购物车/购物车详设(初版).docx";
        String docx2 = "/Users/rocking/Downloads/购物车详设(初版).docx";
        String xlsx1 = "/Users/rocking/Downloads/930详设接口记录.xlsx";
        utils.buildWordDocument(docx1, docx2, xlsx1);
    }
}

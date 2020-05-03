package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import com.idragon.dfdemo.util.fcm.word.InterfaceInfoWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

/**
 *
 * @author chenxinjun
 * FCM word文档生产工具
 */
public class FcmWordUtils {
    public FcmWordUtils() {
    }

    /**
     * word文档生成工具
     * @param targetFilesName  生成目标文件
     * @param modelFileName  生成模版文件
     * @param dataSourceFileName  数据来源excel文件
     */
    public void buildWordDocument(String targetFilesName,String modelFileName,String dataSourceFileName) throws Exception {
        JSONObject data= ExcelUtils.getExcelData(dataSourceFileName);
        // 处理实体信息
        List<BeanInfo> beanList = FcmDataUtils.getBeanInfos(data);
        BeanParseUtils beanParseUtils =new BeanParseUtils(beanList);
        WordUtils utils=new WordUtils();
        List<InterfaceInfo> interfaceBeanInfos = FcmDataUtils.getInterfaceInfos(data);
        //接口参数初始化
        InterfaceInfoWordUtils interfaceInfoWordUtils=new InterfaceInfoWordUtils();
        XWPFDocument contentDoc = utils.getDocument(modelFileName);
        contentDoc= interfaceInfoWordUtils.importDocumentByMethods(contentDoc,interfaceBeanInfos, beanParseUtils);
        //完成文档内容，生成地址
        utils.exportFile(contentDoc,targetFilesName);
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

package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.configure.FcmFileConfigure;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import com.idragon.dfdemo.util.fcm.word.InterfaceInfoWordUtils;
import com.idragon.tool.excel.ExcelReadUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

/**
 *
 * @author chenxinjun
 * FCM word文档生产工具
 */
public class FcmWordUtils {
    FcmFileConfigure fcmFileConfigure;

    public FcmWordUtils(FcmFileConfigure fcmFileConfigure) {
        this.fcmFileConfigure=fcmFileConfigure;
    }

    /**
     * word文档生成工具
     * @param excelFileName
     * @param modelFileName
     * @param resultFileName
     * @throws Exception
     */
    public void buildWordDocument(String excelFileName,String modelFileName,String resultFileName) throws Exception {
        String workPath= fcmFileConfigure.getFileBasePath();
        String targetFilesName = workPath+ fcmFileConfigure.getResultFileName(resultFileName);
        String modelFilesName = workPath+ fcmFileConfigure.getModelFileName(modelFileName);
        String dataSourceFileName = workPath+ fcmFileConfigure.getExcelDefaultName(excelFileName);
        JSONObject data= ExcelReadUtils.getDataByFileName(dataSourceFileName);
        // 处理实体信息
        List<BeanInfo> beanList = FcmDataUtils.getBeanInfos(data,fcmFileConfigure.getEntitySheetName());
        BeanParseUtils beanParseUtils =new BeanParseUtils(beanList);
        WordUtils utils=new WordUtils();
        List<InterfaceInfo> interfaceBeanInfos = FcmDataUtils.getInterfaceInfos(data,fcmFileConfigure.getInterfaceSheetName());
        //接口参数初始化
        InterfaceInfoWordUtils interfaceInfoWordUtils=new InterfaceInfoWordUtils();
        XWPFDocument contentDoc = utils.getDocument(modelFilesName);
        contentDoc= interfaceInfoWordUtils.importDocumentByMethods(contentDoc,interfaceBeanInfos, beanParseUtils);
        //完成文档内容，生成地址
        utils.exportFile(contentDoc,targetFilesName);
    }

}

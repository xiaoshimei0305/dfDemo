package com.idragon.dfdemo;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.BeanDepenceUtils;
import com.idragon.dfdemo.util.fcm.FcmDataUtils;
import com.idragon.dfdemo.util.fcm.FcmWordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.word.BeanInfoWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.IOException;
import java.util.List;

/**
 * 工具测试统一入口
 */
public class Application {


    public static void main(String[] args) throws Exception {
        //testDataGet();
        testAllDoc();
        //testBeanBuild();
        //testImportBean();
    }
    private static void testAllDoc() throws Exception {
        FcmWordUtils utils=new FcmWordUtils();
        String workPath="/Users/chenxinjun/Downloads/";
        String targetFilesName = workPath+"结果文件.docx";
        String modelFileName = workPath+"idrtest.docx";
        String dataSourceFileName = workPath+"930详设接口记录.xlsx";
        utils.buildWordDocument(targetFilesName, modelFileName, dataSourceFileName);
    }

    private static void testDataGet() throws IOException {
        String sourceDataFile="/Users/chenxinjun/Downloads/930详设接口记录.xlsx";
        //读取表格数据
        JSONObject sourceData = ExcelUtils.getExcelData(sourceDataFile);
        List<BeanInfo> beanList = FcmDataUtils.getBeanInfos(sourceData);
        System.out.println("获取到到实体数据:"+JSONObject.toJSONString(beanList));
    }

    private static void testBeanBuild() throws Exception {
        String workPath="/Users/chenxinjun/Downloads/";
        String dataSourceFileName = workPath+"930详设接口记录.xlsx";
        String taregetDir=workPath+"beans/";
        JSONObject data= ExcelUtils.getExcelData(dataSourceFileName);
        // 处理实体信息
        List<BeanInfo> beanList = FcmDataUtils.getBeanInfos(data);
        BeanDepenceUtils beanDepenceUtils=new BeanDepenceUtils(beanList);
        WordUtils utils=new WordUtils();
        List<BeanInfo> beanInfos = FcmDataUtils.getBeanInfos(data);

        BeanInfoWordUtils beanInfoWordUtils=new BeanInfoWordUtils();
        for(BeanInfo info:beanInfos){
            XWPFDocument content = beanInfoWordUtils.getBeanDocumentWithData(info, beanDepenceUtils);
            utils.exportFile(content,taregetDir+info.getCode()+".docx");
        }
    }

    private static void testImportBean() throws Exception {
        WordUtils util=new WordUtils();
        String workPath="/Users/chenxinjun/Downloads/";
        String parent=workPath+"idrtest.docx";
        String children=workPath+"/beans/CartAddParam.docx";
        //XWPFDocument result = util.importModelToDocument(util.getDocument(children), util.getDocument(parent), "idr_method_addCart");
        XWPFDocument result = util.importModelToDocument(util.getDocument(parent), util.getDocument(children), "idr_method_addCart");
        util.exportFile(result,workPath+"/fuck.docx");
    }


}

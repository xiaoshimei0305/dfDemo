package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.word.BeanInfoWordUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.util.List;

/**
 *
 * @author chenxinjun
 * FCM word文档生产工具
 */
public class FcmWordIndex {
    public static void main(String[] args) throws Exception {
        //原始录入数据
        String sourceData="/Users/chenxinjun/Downloads/930.xlsx";
        // doc文档输出工具
        String targetName = "/Users/chenxinjun/Downloads/hh.docx";
        // 解析录入数据，生成对应实体
        JSONObject data= ExcelUtils.getExcelData(sourceData);
        List<BeanInfo> list = FcmDataUtils.getBeanInfos(data);
        String beanModel = FcmWordIndex.class.getResource("/").getPath()+"/doc/beanModel.docx";

        XWPFDocument document = BeanInfoWordUtils.getBeanListDoc(beanModel, list);
        WordUtils utils=new WordUtils();
        utils.exportFile(document,targetName);
    }
}

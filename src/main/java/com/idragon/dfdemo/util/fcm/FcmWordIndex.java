package com.idragon.dfdemo.util.fcm;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.ExcelUtils;
import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.FcmDataUtils;
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
        JSONObject data= ExcelUtils.getExcelData("/Users/chenxinjun/Downloads/930.xlsx");
        List<BeanInfo> list = FcmDataUtils.getBeanInfos(data);
        String wordModelName="/Users/chenxinjun/Downloads/model.docx";
        String targetName = "/Users/chenxinjun/Downloads/hh.docx";
        String beanModel = "/Users/chenxinjun/Downloads/beanModel.docx";
        XWPFDocument document = BeanInfoWordUtils.getBeanListDoc(beanModel, list);
        WordUtils utils=new WordUtils();
        utils.exportFile(document,targetName);
    }
}

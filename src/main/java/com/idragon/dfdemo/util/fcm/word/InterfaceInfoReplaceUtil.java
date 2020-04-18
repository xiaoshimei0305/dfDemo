package com.idragon.dfdemo.util.fcm.word;

import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.apache.poi.xwpf.usermodel.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author chenxinjun
 * 接口信息替换
 */
public class InterfaceInfoReplaceUtil {
    /**
     * 段落文档替换工具
     * @param doc
     * @param interfaceInfo
     */
    public static  void replaceDoc(XWPFDocument doc, InterfaceInfo interfaceInfo) {
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
            WordUtils.addOrRemoveRow(table,2,2);
        }
        System.out.println(tables.size());
    }
    public static void replaceInParagraph(XWPFParagraph para,InterfaceInfo interfaceInfo){
        WordUtils.replaceInParagraph(para, "${name}",interfaceInfo.getName());
    }
}

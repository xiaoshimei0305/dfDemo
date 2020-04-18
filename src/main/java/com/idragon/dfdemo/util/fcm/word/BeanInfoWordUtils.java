package com.idragon.dfdemo.util.fcm.word;

import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanFieldInfo;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.Iterator;
import java.util.List;

/**
 * @author chenxinjun
 * 接口信息替换
 */
public class BeanInfoWordUtils {
    /**
     * 文档实体生成列表
     * @param beanModelFileName
     * @param beanInfos
     * @return
     */
    public static XWPFDocument getBeanListDoc(String beanModelFileName,List<BeanInfo> beanInfos) throws Exception {
        WordUtils utils=new WordUtils();
        XWPFDocument document = utils.getDocument(beanModelFileName);
        if(beanInfos!=null&&beanInfos.size()>0){
            //初始化第一个文档
            replaceBean(document,beanInfos.get(0));
            if(beanInfos.size()>1){
                //追加文档
                for(int i=1;i<beanInfos.size();i++){
                    XWPFDocument temp = utils.getDocument(beanModelFileName);
                    replaceBean(temp,beanInfos.get(i));
                    utils.mergeWord(document,temp);
                }
            }
        }
        return document;
    }
    /**
     * 段落文档替换工具
     * @param doc
     * @param beanInfo
     */
    public static  void replaceBean(XWPFDocument doc, BeanInfo beanInfo) {
        //段落替换
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInParagraph(para, beanInfo);
        }
        //表格替换，有且只有一个表格
        List<XWPFTable> tables = doc.getTables();
        if(tables!=null&&tables.size()>0){
            initBeanTable(tables.get(0),beanInfo.getFieldList());
        }
    }

    /**
     * 段落文档内容替换
     * @param para
     * @param beanInfo
     */
    public static void replaceInParagraph(XWPFParagraph para,BeanInfo beanInfo){
        WordUtils.replaceInParagraph(para, "${name}",beanInfo.getName());
    }
    /**
     * 表格数据初始化
     * @param table
     * @param fieldInfos
     */
    public static void initBeanTable(XWPFTable table, List<BeanFieldInfo> fieldInfos){
        if(fieldInfos!=null&&fieldInfos.size()>0){
            if(fieldInfos.size()>1){
                WordUtils.addOrRemoveRow(table,fieldInfos.size()-1,2);
            }
            for(int i=0;i<fieldInfos.size();i++){
                BeanFieldInfo info=fieldInfos.get(i);
                XWPFTableRow row = table.getRow(i + 1);
                row.getCell(0).setText(info.getName());
                row.getCell(1).setText(info.getCode());
                row.getCell(2).setText(info.getType());
                row.getCell(3).setText(info.getNameLength());
                row.getCell(4).setText(info.isRequire()?"是":"否");
                row.getCell(5).setText(info.getRemark());
            }
        }
    }

}

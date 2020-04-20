package com.idragon.dfdemo.util.fcm.word;

import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.dto.BeanFieldInfo;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.io.IOException;
import java.util.List;

/**
 * @author chenxinjun
 * 接口信息替换
 */
public class BeanInfoWordUtils {
    /**
     * 实体模型路径
     */
    public String modelPath;
    /**
     * 表格模型路径
     */
    private String tableModelPath;
    /**
     * word操作工具
     */
    private WordUtils utils;

    public BeanInfoWordUtils(String modelPath,String tableModelPath) {
        this.modelPath = modelPath;
        this.tableModelPath=tableModelPath;
        this.utils=new WordUtils();
        if(StringUtils.isBlank(this.modelPath)){
            this.modelPath= this.getClass().getResource("/").getPath()+"doc/beanModel.docx";
            this.tableModelPath=this.getClass().getResource("/").getPath()+"doc/tableModel.docx";
        }
    }

    public BeanInfoWordUtils() {
        this(null,null);
    }

    /**
     * 获取批量实体文档内容
     * @param beanInfos
     * @return
     */
    public XWPFDocument getBeanListDocument(List<BeanInfo> beanInfos) throws Exception {
        if(beanInfos!=null&&beanInfos.size()>0){
            XWPFDocument doc=getBeanDocumentWithData(beanInfos.get(0));
            if(beanInfos.size()>1){
                for(int i=1;i<beanInfos.size();i++){
                    utils.appendDocument(doc,getBeanDocumentWithData(beanInfos.get(i)));
                }
            }
            return doc;
        }
        return null;
    }


    /**
     * 获取实体文档
     * @param info
     * @return
     * @throws Exception
     */
    public XWPFDocument getBeanDocumentWithData(BeanInfo info) throws Exception {
        XWPFDocument beanDocument = getBeanDocument();
        utils.replaceText(beanDocument,"idr_bean_name",info.getName());
        beanDocument= utils.importModelToDocument(beanDocument,getTableDocumentWithData(info),"idr_bean_table");
        return beanDocument;
    }

    /**
     * 通过表格模版，获取加入数据的表格实体
     * @return
     */
    public XWPFDocument getTableDocumentWithData(BeanInfo info) throws IOException {
        XWPFDocument doc=getTableDocument();
        if(info!=null&&info.getFieldList()!=null&&info.getFieldList().size()>0){
            List<XWPFTable> tables = doc.getTables();
            if(tables!=null&&tables.size()>0){
                initBeanTable(tables.get(0),info.getFieldList());
            }
        }
        return doc;
    }

    /**
     * 获取实体模型文档
     * @return
     * @throws IOException
     */
    public XWPFDocument getBeanDocument() throws IOException {
        return utils.getDocument(this.modelPath);
    }

    /**
     * 获取表格模型文档
     * @return
     * @throws IOException
     */
    public XWPFDocument getTableDocument() throws IOException {
        return utils.getDocument(this.tableModelPath);
    }

    /**
     * 表格数据初始化
     * @param table
     * @param fieldInfos
     */
    public  void initBeanTable(XWPFTable table, List<BeanFieldInfo> fieldInfos){
        if(fieldInfos!=null&&fieldInfos.size()>0){
            if(fieldInfos.size()>1){
                new WordUtils().addOrRemoveRow(table,fieldInfos.size()-1,2);
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

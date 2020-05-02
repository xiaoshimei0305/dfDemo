package com.idragon.dfdemo.util.fcm.word;

import com.idragon.dfdemo.util.WordUtils;
import com.idragon.dfdemo.util.fcm.BeanDepenceUtils;
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
     * 获取实体文档
     * @param info
     * @param beanDepenceUtils
     * @return
     * @throws Exception
     */
    public XWPFDocument getBeanDocumentWithData(BeanInfo info, BeanDepenceUtils beanDepenceUtils) throws Exception {
        System.out.println("生成实体文档对象,name:"+info.getName()+",code:"+info.getCode());
        XWPFDocument beanDocument = getBeanDocument();
        if(info==null){
            info=new BeanInfo();
            info.setName("无返回值");
        }
        utils.replaceText(beanDocument,"idr_bean_name",info.getName());
        beanDocument= utils.importModelToDocument(beanDocument,getTableDocumentWithData(info,beanDepenceUtils),"idr_bean_table");
        return beanDocument;
    }

    /**
     * 通过表格模版，获取加入数据的表格实体
     * @return
     */
    public XWPFDocument getTableDocumentWithData(BeanInfo info, BeanDepenceUtils beanDepenceUtils) throws IOException {
        XWPFDocument doc=getTableDocument();
        if(info!=null&&info.getFieldList()!=null&&info.getFieldList().size()>0){
            List<XWPFTable> tables = doc.getTables();
            if(tables!=null&&tables.size()>0){
                XWPFTable table=tables.get(0);
                List<BeanInfo> list = beanDepenceUtils.parseBeanList(info);
                for(int i=0;i<list.size();i++){
                    addBeanInfo(table,list.get(i),i==0);
                }
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
     * 获取指定行对象
     * @param row -1 时返回最后一行数据
     * @return
     */
    private XWPFTableRow getRow(XWPFTable table,int row){
        if(row<0){
            return table.getRow(table.getRows().size()-1);
        }
        return table.getRow(row);
    }

    /**
     * 在表格模版中添加实体信息
     * @param table 表格对象
     * @param beanInfo 实体信息
     */
    private void addBeanInfo(XWPFTable table,BeanInfo beanInfo,boolean isFirstBean){
        int startIndex=isFirstBean?1:table.getRows().size()+1;
        List<BeanFieldInfo> fieldInfos=beanInfo.getFieldList();
        WordUtils utils=new WordUtils();
        if(!isFirstBean){
            utils.addRowStartLast(table,1,1);
            getRow(table,-1).getCell(0).setText(beanInfo.getName());
            utils.addRowStartLast(table,1,2);
            utils.copyRowData(table,1,table.getRows().size()-1);
            utils.addRowStartLast(table,1,3);
        }else{
            getRow(table,0).getCell(0).setText(beanInfo.getName());
        }
        if(fieldInfos!=null&&fieldInfos.size()>0){
            if(fieldInfos.size()>1){
                utils.addRowStartLast(table,fieldInfos.size()-1,3);
            }
            for(int i=0;i<fieldInfos.size();i++){
                BeanFieldInfo info=fieldInfos.get(i);
                XWPFTableRow row = table.getRow(i + startIndex+1);
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

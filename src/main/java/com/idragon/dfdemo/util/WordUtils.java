package com.idragon.dfdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.util.ZipSecureFile;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.*;


/**
 * @author chenxinjun
 * word文档操作工具
 */
@Slf4j
public class WordUtils {
    /**
     * 临时文件存储位置
     */
    private static final String tempFile="temp.docx";


    /**
     * 添加或者删除表格
     * @param table 表格对象
     * @param rows 增加或删除行数 if rows>0 增加行 rows<0 删除行
     * @param locationRow 添加开始位置【以开始行为模版】
     */
    public   void addOrRemoveRow(XWPFTable table, int rows, int locationRow){
      addOrRemoveRow(table,rows,locationRow,locationRow);
    }

    /**
     * 尾数后面添加行
     * @param table 表格对象
     * @param rows 增加或删除行数 if rows>0 增加行 rows<0 删除行
     * @param modelRow
     */
    public void addRowStartLast(XWPFTable table, int rows, int modelRow){
        int fromRow=table.getRows().size();
        addOrRemoveRow(table,rows,fromRow,modelRow);
    }

    /**
     * 复制行数据
     * @param table 表格
     * @param sourceRow  原始数据行
     * @param targetRow  目标数据行
     */
    public void copyRowData(XWPFTable table, int sourceRow, int targetRow){
        XWPFTableRow source = table.getRow(sourceRow);
        XWPFTableRow target = table.getRow(targetRow);
        List<XWPFTableCell> cellList = source.getTableCells();
        if(cellList!=null&&cellList.size()>0){
            for(int i=0;i<cellList.size();i++){
                String text=source.getCell(i).getText();
                target.getCell(i).setText(text);
            }
        }
    }
    /**
     * 添加或者删除表格
     * @param table 表格对象
     * @param rows 增加或删除行数 if rows>0 增加行 rows<0 删除行
     * @param locationRow 添加开始位置
     * @param  modelRow 模版行位置
     */
    public   void addOrRemoveRow(XWPFTable table, int rows, int fromRow,int modelRow){
        XWPFTableRow row = table.getRow(modelRow-1);
        if(rows>0){
            while(rows>0){
                copyPro(row,table.insertNewTableRow(fromRow));
                rows--;
            }
        } else {
            while(rows<0)
            {
                table.removeRow(fromRow-1);
                rows++;
            }
        }
    }

    /**
     * 文本内容回撤替换
     * @param cell
     */
    public  void addBreakInCell(XWPFTableCell cell) {
            for (XWPFParagraph p : cell.getParagraphs()) {
                //XWPFRun对象定义具有一组公共属性的文本区域
                for (XWPFRun run : p.getRuns()) {
                    if(run.getText(0)!= null && run.getText(0).contains("\n")) {
                        String[] lines = run.getText(0).split("\n");
                        if(lines.length > 0) {
                            // set first line into XWPFRun
                            run.setText(lines[0], 0);
                            for(int i=1;i<lines.length;i++){
                                // add break and insert new text //中断
                                run.addBreak();
				                //run.addCarriageReturn();//回车符，但是不起作用
                                run.setText(lines[i]);
                            }
                        }
                    }
                }
            }
    }

    /**
     * 复制内容
     * @param sourceRow
     * @param targetRow
     */
    public  void copyPro(XWPFTableRow sourceRow,XWPFTableRow targetRow) {
        //复制行属性
        targetRow.getCtRow().setTrPr(sourceRow.getCtRow().getTrPr());
        List<XWPFTableCell> cellList = sourceRow.getTableCells();
        if (null == cellList) {
            return;
        }
        //添加列、复制列以及列中段落属性
        XWPFTableCell targetCell = null;
        for (XWPFTableCell sourceCell : cellList) {
            targetCell = targetRow.addNewTableCell();
            //列属性
            targetCell.getCTTc().setTcPr(sourceCell.getCTTc().getTcPr());
            //段落属性
            targetCell.getParagraphs().get(0).getCTP().setPPr(sourceCell.getParagraphs().get(0).getCTP().getPPr());
        }
    }

    /**
     * 文档字符串替换工具
     * @param doc
     * @param oldString
     * @param newString
     */
    public  void replaceText(XWPFDocument doc,String oldString, String newString){
        //段落替换
        Iterator<XWPFParagraph> iterator = doc.getParagraphsIterator();
        XWPFParagraph para;
        while (iterator.hasNext()) {
            para = iterator.next();
            replaceInParagraph(para, oldString,newString);
        }
    }

    /**
     * 替换段落中的字符串
     *
     * @param xwpfParagraph
     * @param oldString
     * @param newString
     */
    public  void replaceInParagraph(XWPFParagraph xwpfParagraph, String oldString, String newString) {
        Map<String, Integer> pos_map = findSubRunPosInParagraph(xwpfParagraph, oldString);
        if (pos_map != null) {
            List<XWPFRun> runs = xwpfParagraph.getRuns();
            XWPFRun modelRun = runs.get(pos_map.get("end_pos"));
            XWPFRun xwpfRun = xwpfParagraph.insertNewRun(pos_map.get("end_pos") + 1);
            xwpfRun.setText(newString);
            copyStyleToText(modelRun,xwpfRun);
            for (int i = pos_map.get("end_pos"); i >= pos_map.get("start_pos"); i--) {
                xwpfParagraph.removeRun(i);
            }
        }
    }
    /**
     * 找到段落中子串的起始XWPFRun下标和终止XWPFRun的下标
     *
     * @param xwpfParagraph
     * @param substring
     * @return
     */
    public  Map<String, Integer> findSubRunPosInParagraph(XWPFParagraph xwpfParagraph, String substring) {
        List<XWPFRun> runs = xwpfParagraph.getRuns();
        int start_pos = 0;
        int end_pos = 0;
        String subtemp = "";
        for (int i = 0; i < runs.size(); i++) {
            subtemp = "";
            start_pos = i;
            for (int j = i; j < runs.size(); j++) {
                if (runs.get(j).getText(runs.get(j).getTextPosition()) == null) continue;
                subtemp += runs.get(j).getText(runs.get(j).getTextPosition());
                if (subtemp.equals(substring)) {
                    end_pos = j;
                    Map<String, Integer> map = new HashMap<>();
                    map.put("start_pos", start_pos);
                    map.put("end_pos", end_pos);
                    return map;
                }
            }
        }
        return null;
    }

    /**
     * 文本替换过程需要 复制的字体样式信息
     * @param modelRun
     * @param targetRun
     */
    public  void copyStyleToText(XWPFRun modelRun,XWPFRun targetRun){
        if (modelRun.getFontSize() != -1) {
            targetRun.setFontSize(modelRun.getFontSize());
        }
        //默认值是五号字体，但五号字体getFontSize()时，返回-1
        targetRun.setFontFamily(modelRun.getFontFamily());
        targetRun.setColor(modelRun.getColor());
    }

    /**
     * 文档模版导入工具
     * @param contentDoc 环境文档
     * @param modelDoc 导入模版内容
     * @param key 导入模版的标识文字【注意不能使用&{name} 这种格式，直接使用字符串name即可】
     * @return
     * @throws Exception
     */
    public  XWPFDocument importModelToDocument(XWPFDocument contentDoc,XWPFDocument modelDoc,String key) throws Exception {
        XWPFDocument src1Document =contentDoc ;
        replaceText(contentDoc,key,"${"+key+"}");
        CTBody src1Body = src1Document.getDocument().getBody();
        XWPFDocument src2Document = modelDoc;
        CTBody src2Body = src2Document.getDocument().getBody();
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = src2Body.xmlText(optionsOuter);
        String srcString = src1Body.xmlText();
        String prefix = srcString.substring(0,srcString.indexOf(">")+1);
        String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
        String suffix = srcString.substring( srcString.lastIndexOf("<") );
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        String content=prefix+mainPart.replaceAll("\\$\\{"+key+"\\}",addPart)+suffix;
        CTBody makeBody = CTBody.Factory.parse(content);
        src1Body.set(makeBody);
        String tempPath=this.getClass().getResource("/").getPath()+"doc/"+tempFile;
        exportFile(src1Document,tempPath);
        return getDocument(tempPath);
    }


    /**
     * 获取文档对象
     * @param srcPath
     * @return
     * @throws IOException
     */
    public XWPFDocument getDocument(String srcPath) throws IOException {
        File file=new File(srcPath);
        FileInputStream fis = new FileInputStream(file);
        ZipSecureFile.setMinInflateRatio(-1.0d);
        XWPFDocument document=new XWPFDocument(fis);
        fis.close();
        return document;
    }
    /**
     * 内容导出
     * @param hwpfDocument
     * @param targetFileName
     */
    public  void exportFile(XWPFDocument document,String targetFileName){
        ByteArrayOutputStream ostream = new ByteArrayOutputStream();
        OutputStream outs=null;
        try {
            document.write(ostream);
            //输出word文件
           File temp= FileUtils.getFile(targetFileName,true);
           outs=new FileOutputStream(temp);
            outs.write(ostream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(outs !=null ){
                try {
                    outs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}

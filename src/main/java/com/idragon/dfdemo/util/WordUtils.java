package com.idragon.dfdemo.util;

import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


/**
 * @author chenxinjun
 * word文档操作工具
 */
public class WordUtils {
    /**
     *
     * @param table 对应表格
     * @param add 增加或删除行数 if add>0 增加行 add<0 删除行
     * @param fromRow 添加开始行位置（fromRow-1是模版行）
     */
    public   void addOrRemoveRow(XWPFTable table, int add, int fromRow){
        XWPFTableRow row = table.getRow(fromRow-1);
        if(add>0){
            while(add>0){
                copyPro(row,table.insertNewTableRow(fromRow));
                add--;
            }
        } else {
            while(add<0)
            {
                table.removeRow(fromRow-1);
                add++;
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
     * 文档合并工具
     * @param contentDoc 环境文档
     * @param appendDoc 追加文档
     * @return
     */
    public XWPFDocument appendDocument(XWPFDocument contentDoc,XWPFDocument appendDoc) throws XmlException {
        XWPFDocument src1Document =contentDoc ;
        CTBody src1Body = src1Document.getDocument().getBody();
        XWPFDocument src2Document = appendDoc;
        CTBody src2Body = src2Document.getDocument().getBody();
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = src2Body.xmlText(optionsOuter);
        String srcString = src1Body.xmlText();
        String prefix = srcString.substring(0,srcString.indexOf(">")+1);
        String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
        String suffix = srcString.substring( srcString.lastIndexOf("<") );
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        String content=prefix+mainPart+addPart+suffix;
        CTBody makeBody = CTBody.Factory.parse(content);
        src1Body.set(makeBody);
        return src1Document;
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
        System.out.println("replace is :"+key);
        replaceText(contentDoc,key,key);
        CTBody src1Body = src1Document.getDocument().getBody();
        XWPFParagraph p = src1Document.createParagraph();
        //设置分页符
        p.setPageBreak(false);
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
        String content=prefix+mainPart.replaceAll(key,addPart)+suffix;
        CTBody makeBody = CTBody.Factory.parse(content);
        src1Body.set(makeBody);
        return src1Document;
    }


    /**
     * 获取文档对象
     * @param srcPath
     * @return
     * @throws IOException
     */
    public XWPFDocument getDocument(String srcPath) throws IOException {
        FileInputStream fis = new FileInputStream(new File(srcPath));
        return new XWPFDocument(fis);
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
           outs=new FileOutputStream(new File(targetFileName));
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

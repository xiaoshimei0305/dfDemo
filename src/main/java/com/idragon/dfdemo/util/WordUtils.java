package com.idragon.dfdemo.util;

import com.idragon.dfdemo.util.fcm.word.InterfaceInfoReplaceUtil;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import org.apache.poi.xwpf.usermodel.*;
import org.apache.xmlbeans.XmlOptions;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBody;

import java.io.*;
import java.util.HashMap;
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


    public  static void addOrRemoveRow(XWPFTable table, int add, int fromRow){
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
    public static void copyPro(XWPFTableRow sourceRow,XWPFTableRow targetRow) {
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
     * 替换段落中的字符串
     *
     * @param xwpfParagraph
     * @param oldString
     * @param newString
     */
    public static void replaceInParagraph(XWPFParagraph xwpfParagraph, String oldString, String newString) {
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
    public static Map<String, Integer> findSubRunPosInParagraph(XWPFParagraph xwpfParagraph, String substring) {
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
    public static void copyStyleToText(XWPFRun modelRun,XWPFRun targetRun){
        if (modelRun.getFontSize() != -1) targetRun.setFontSize(modelRun.getFontSize());
        //默认值是五号字体，但五号字体getFontSize()时，返回-1
        targetRun.setFontFamily(modelRun.getFontFamily());
        targetRun.setColor(modelRun.getColor());
    }
    /**
     * 对象追加
     * @param document
     * @param doucDocument2
     * @return
     * @throws Exception
     */
    public  XWPFDocument mergeWord(XWPFDocument document,XWPFDocument doucDocument2) throws Exception {
        XWPFDocument src1Document =document ;
        XWPFParagraph p = src1Document.createParagraph();
        //设置分页符
        p.setPageBreak(true);
        CTBody src1Body = src1Document.getDocument().getBody();
        XWPFDocument src2Document = doucDocument2;
        CTBody src2Body = src2Document.getDocument().getBody();
//		    XWPFParagraph p2 = src2Document.createParagraph();
        XmlOptions optionsOuter = new XmlOptions();
        optionsOuter.setSaveOuter();
        String appendString = src2Body.xmlText(optionsOuter);
        String srcString = src1Body.xmlText();
        String prefix = srcString.substring(0,srcString.indexOf(">")+1);
        String mainPart = srcString.substring(srcString.indexOf(">")+1,srcString.lastIndexOf("<"));
        String sufix = srcString.substring( srcString.lastIndexOf("<") );
        String addPart = appendString.substring(appendString.indexOf(">") + 1, appendString.lastIndexOf("<"));
        CTBody makeBody = CTBody.Factory.parse(prefix+mainPart+addPart+sufix);
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
     * 获取输出文档
     * @param xmlContent
     * @return
     * @throws IOException
     */
    public XWPFDocument getDocumentByXml(String xmlContent) throws IOException {
        InputStream is = new ByteArrayInputStream(xmlContent.getBytes());
        return new XWPFDocument(is);
    }

    /**
     * 内容替换工具
     * @param document
     * @param interfaceInfo 111
     */
    public void replaceContent(XWPFDocument document,InterfaceInfo interfaceInfo){
        InterfaceInfoReplaceUtil.replaceDoc(document,interfaceInfo);
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

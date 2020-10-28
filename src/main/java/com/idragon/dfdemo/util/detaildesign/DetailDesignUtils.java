package com.idragon.dfdemo.util.detaildesign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.*;
import store.idragon.tool.base.StringUtils;
import store.idragon.tool.excel.ExcelReadUtils;
import org.apache.poi.xwpf.usermodel.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 详细设计生成工具列表
 * @author chenxinjun
 */
public class DetailDesignUtils {

    public static String debuggerAddress="";
    /**
     * 导出excel
     * @throws IOException
     */
    private static void exportExcel(String fileName) throws IOException {
        //数据存储到excel文档
        FileUtils.writeFileContent(new File(fileName), SwaggerInfoUtils.getToExcelStr(SwaggerInfoUtils.getFcmInterfaceInfo()),"GBK");
    }

    /**
     *  导出文档
     * @param excelName
     * @param wordModelName
     * @param exportFileName
     * @throws Exception
     */
    private static void exportWord(String excelName,String wordModelName,String exportDir) throws Exception {
        //收集接口数据
        JSONArray excelList = ExcelReadUtils.getDataByFileName(excelName).getJSONArray("FCM接口列表");
        JSONArray fcmSwaggerInfoList=SwaggerInfoUtils.getFcmInterfaceInfo();
        JSONObject addressMap=new JSONObject();
        if(fcmSwaggerInfoList!=null&&fcmSwaggerInfoList.size()>0){
            for(int i=0;i<fcmSwaggerInfoList.size();i++){
                JSONObject item=fcmSwaggerInfoList.getJSONObject(i);
                addressMap.put(item.getString("url"),item);
            }
        }
        List<String> authors=new ArrayList<>();
        List<MethodInfo> infoList=new ArrayList<>();
        if(excelList!=null&&excelList.size()>0){
            for(int i=0;i<excelList.size();i++){
                MethodInfo info=new MethodInfo(excelList.getJSONObject(i));
                info.initSwaggerInfo(addressMap);
                if(!authors.contains(info.getAuthor())){
                    authors.add(info.getAuthor());
                }
                infoList.add(info);
            }
        }
        int maxSize=100;
        for(String author:authors){
           int result= exportWord(infoList,wordModelName,exportDir+"/"+author+"【"+maxSize+"个一组】",author,maxSize);
            System.out.println("【"+author+"】接口总数："+result);
        }
    }

    /**
     * 导出接口到文档中
     * @param infoList
     * @param wordModelName
     * @param exportDir
     * @param author
     * @param maxSize
     * @return
     * @throws Exception
     */
    private static int exportWord(List<MethodInfo> infoList,String wordModelName,String exportDir,String author,int maxSize) throws Exception {
        int total=0;
        int documentSize=1;
        int cur=0;
        String currentRestName="";
        //获取文档模版
        WordUtils utils=new WordUtils();
        XWPFDocument contentDoc=utils.getDocument(wordModelName);
        for(int i=0;i<infoList.size();i++){
            MethodInfo methodInfo=infoList.get(i);

            String address=methodInfo.getAddress();
            address=address.replaceAll("\\{","_");
            address=address.replaceAll("\\}","_");
            if(!StringUtils.isBlank(author)&&!author.equalsIgnoreCase(methodInfo.getAuthor())){
                utils.replaceText(contentDoc,address,"");
                continue;
            }else{
                total++;
                cur++;
            }
            XWPFDocument modelItem = utils.getDocument(DetailDesignUtils.class.getResource("/").getPath()+"doc/restInterfaceModel.docx");
            utils.replaceText(modelItem,"name",methodInfo.getName());
            utils.replaceText(modelItem,"remark",methodInfo.getRemark());
            utils.replaceText(modelItem,"requestExample",methodInfo.getRequestExample());
            utils.replaceText(modelItem,"responseExample",methodInfo.getResponseExample());
            utils.replaceText(modelItem,"rpcDesc",methodInfo.getRpcDesc());
            setInterfaceBaseInfo(modelItem,methodInfo);
            setRespBaseInfo(modelItem,methodInfo);
            modelItem=utils.importModelToDocument(modelItem,getTableDocumentWithData(methodInfo.getRequest()),"Idr_req_table");
            modelItem=utils.importModelToDocument(modelItem,getTableDocumentWithData(methodInfo.getResponse()),"Idr_resp_table");
            contentDoc=utils.importModelToDocument(contentDoc,modelItem,address);
            currentRestName=methodInfo.getName();
            if(cur>=maxSize){
                //超过指定文档，添加新文档导出
                documentSize++;
                utils.exportFile(contentDoc,exportDir+"/"+currentRestName+"【"+documentSize+"】.docx");
                contentDoc=utils.getDocument(wordModelName);
                cur=0;
            }
        }
        //导出文档
        utils.exportFile(contentDoc,exportDir+"/"+currentRestName+"【"+documentSize+"】.docx");
        return total;
    }


    /**
     * 获取接口基本参数
     * @param modelItem
     * @param methodInfo
     */
    private static void setInterfaceBaseInfo(XWPFDocument modelItem,MethodInfo methodInfo){
        XWPFTable table = modelItem.getTables().get(0);
        table.getRow(0).getCell(1).setText(methodInfo.getModel());
        table.getRow(1).getCell(1).setText(methodInfo.getName());
        table.getRow(2).getCell(1).setText(methodInfo.getAddress());
        table.getRow(3).getCell(1).setText(methodInfo.useDesc());
        table.getRow(4).getCell(1).setText(methodInfo.getPostWay());
    }

    private  static void setRespBaseInfo(XWPFDocument modelItem,MethodInfo methodInfo){
        XWPFTable table = modelItem.getTables().get(1);
        WordUtils utils=new WordUtils();
        setMutilLineText(table.getRow(0).getCell(1),StringUtils.toPrettyJsonString(methodInfo.getRequestExample()));
        utils.addBreakInCell(table.getRow(0).getCell(1));
        setMutilLineText(table.getRow(1).getCell(1),StringUtils.toPrettyJsonString(methodInfo.getResponseExample()));
        utils.addBreakInCell(table.getRow(1).getCell(1));
    }

    /**
     * 设置多行文本到单元格
     * @param cell
     * @param toPrettyFormatJson
     */
    private static void setMutilLineText(XWPFTableCell cell, String toPrettyFormatJson) {
        if(cell!=null&&!StringUtils.isBlank(toPrettyFormatJson)){
            String[] lineStr=toPrettyFormatJson.split("\n");
            XWPFParagraph para=cell.getParagraphs().get(0);
            for(String item:lineStr){
                XWPFRun run = para.createRun();
                run.setText(item);
                run.addBreak();
            }
        }
    }




    /**
     * 通过表格模版，获取加入数据的表格实体
     * @return
     */
    private static XWPFDocument getTableDocumentWithData(CodeTitle codeTitle) throws IOException {
       List<CodeTitle> codeTitleList=new ArrayList<>();
       if(codeTitle!=null){
           codeTitleList.add(codeTitle);
       }
       return getTableDocumentWithData(codeTitleList);
    }
    /**
     * 通过表格模版，获取加入数据的表格实体
     * @return
     */
    private static XWPFDocument getTableDocumentWithData(List<CodeTitle> codeTitleList) throws IOException {
        WordUtils utils=new WordUtils();
        if(codeTitleList==null){
            codeTitleList=new ArrayList<>();
        }
        XWPFDocument doc=utils.getDocument(DetailDesignUtils.class.getResource("/").getPath()+"doc/restInterfaceTableModel.docx");
            List<XWPFTable> tables = doc.getTables();
            if(tables!=null&&tables.size()>0){
                XWPFTable table=tables.get(0);
                for(int i=0;i<codeTitleList.size();i++){
                    addTableData(table,codeTitleList.get(i),i==0);
                }
            }
        return doc;
    }

    /**
     * 表格添加数据
     * @param table
     * @param codeTitle
     */
    private static void addTableData(XWPFTable table, CodeTitle codeTitle,boolean isFirst){
        WordUtils utils=new WordUtils();
        if(isFirst){
            table.getRow(0).getCell(0).setText(codeTitle.getParentPath());
        }else{
            utils.addRowStartLast(table,1,1);
            getLastRow(table).getCell(0).setText(codeTitle.getParentPath());
        }
        if(codeTitle!=null){
            for(int i=0;i<codeTitle.getFieldInfoList().size();i++){
                setLineData(codeTitle.getDepMark(),table,codeTitle.getFieldInfoList().get(i),isFirst&&i==0);
            }
        }

    }

    /**
     * 设置一行数据
     * @param table
     * @param fieldInfo
     */
    private static void setLineData(String prefix,XWPFTable table,FieldInfo fieldInfo,boolean isFirstRow){
        WordUtils utils=new WordUtils();
        if(fieldInfo.getSubInfo()!=null){
            addTableData(table,fieldInfo.getSubInfo(),false);
        }else{
            if(!isFirstRow){
                utils.addRowStartLast(table,1,3);
            }
            String length="";
            if(!StringUtils.isBlank(fieldInfo.getField())){
                if(fieldInfo.getField().endsWith("id")||fieldInfo.getField().endsWith("code")||fieldInfo.getField().endsWith("Id")||fieldInfo.getField().endsWith("Code")){
                    length="V32";
                }
            }
            setLineData(getLastRow(table),prefix+fieldInfo.getField(),fieldInfo.getType(),length,fieldInfo.isRequired()?"是":"否",fieldInfo.getDescription());
        }

    }


    /**
     * 设置一行表格数据
     * @param row
     * @param name
     * @param type
     * @param length
     * @param required
     * @param desc
     */
    private static void setLineData(XWPFTableRow row,String name,String type,String length,String required,String desc){
        row.getCell(0).setText(name);
        row.getCell(1).setText(type);
        row.getCell(2).setText(length);
        row.getCell(3).setText(required);
        row.getCell(4).setText(desc);
    }
    /**
     * 获取表格最后一行
     * @param table
     * @return
     */
    private static XWPFTableRow getLastRow(XWPFTable table){
        return table.getRow(table.getRows().size()-1);
    }

    public static void main(String[] args) throws Exception {
        JSONObject data = ExcelReadUtils.getDataByFileName("/Users/chenxinjun/Downloads/商城REST接口梳理.xlsx");
        System.out.println(data.toJSONString());
        //exportExcel("/Users/chenxinjun/Downloads/fcmInterface.csv");
       // exportWord("/Users/chenxinjun/Downloads/商城REST接口梳理.xlsx","/Users/chenxinjun/Downloads/model.docx","/Users/chenxinjun/Downloads/model");
//        JSONArray info = SwaggerInfoUtils.getFcmInterfaceInfo();
//        System.out.println(info);
        System.out.println("=================================");
    }
}

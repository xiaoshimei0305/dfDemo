package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author chenxinjun
 * excel文档内容获取工具
 */
public class ExcelUtils {
    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    public static void main(String[] args) throws IOException {
        JSONObject data = getExcelData("/Users/rocking/Downloads/931.xlsx");
        System.out.println(data.getJSONArray("接口列表").toJSONString());
    }


    /**
     * 获取excel文档内容
     * @param fileName 文件名称
     * @return
     */
    public static JSONObject getExcelData(String fileName) throws IOException {
        Workbook wb = getWorkbok(fileName);
        JSONObject data=new JSONObject();
        int sheetSize = wb.getNumberOfSheets();
        for(int i=0;i<sheetSize;i++){
            Sheet sheet = wb.getSheetAt(i);
            String sheetName=sheet.getSheetName();
            JSONArray list=new JSONArray();
            //获得总列数
            int coloumNum=sheet.getRow(0).getPhysicalNumberOfCells();
            //获得总行数
            int rowNum=sheet.getPhysicalNumberOfRows();
            if(rowNum>1){
                //初始化字段名称,使用首行
                String[] columns=new String[coloumNum];
                Row row = sheet.getRow(0);
                //定位最后一个不为空cell位置
                int lastValueIndex=0;
                for(int j=0;j<coloumNum;j++){
                    columns[j]=getCellValue(row,j,j+"");
                    if(!columns[j].equals(j+"")){
                        //标记最后一个不为空列
                        lastValueIndex=j;
                    }
                }
                for(int j=1;j<rowNum;j++){
                    Row tempRow=sheet.getRow(j);
                    JSONObject item=new JSONObject();
                    boolean allEmpty=true;
                    for(int k=0;k<=lastValueIndex;k++){
                        String value=getCellValue(tempRow,k,"");
                        item.put(columns[k],value);
                        if(!StringUtils.isBlank(value)){
                            allEmpty=false;
                        }
                    }
                    if(!allEmpty){
                        list.add(item);
                    }
                }
            }
            data.put(sheetName,list);
        }
        return data;
    }

    /**
     * 获取指定cell值
     * @param row
     * @param index
     * @return
     */
    private static String getCellValue(Row row,int index,String defalutValue){
        if(row!=null){
            Cell cell = row.getCell(index);
            if(cell!=null){
                cell.setCellType(CellType.STRING);
                return StringUtils.isBlank(cell.getStringCellValue())?defalutValue:cell.getStringCellValue();
            }
        }
        return defalutValue;
    }


    /**
     * 判断Excel的版本,获取Workbook
     * @param fileName
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbok(String fileName) throws IOException {
        Workbook wb = null;
        File file =new File(fileName.trim());
        FileInputStream in = new FileInputStream(file);
        if(file.getName().endsWith(EXCEL_XLS)){
            wb = new HSSFWorkbook(in);
        }else if(file.getName().endsWith(EXCEL_XLSX)){
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }
}

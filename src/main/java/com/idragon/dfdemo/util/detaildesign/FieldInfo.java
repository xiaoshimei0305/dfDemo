package com.idragon.dfdemo.util.detaildesign;

import lombok.Data;

import java.util.List;

/**
 * @author chenxinjun
 */
@Data
public class FieldInfo {

    private String field;
    private String description;
    private String type;
    private CodeTitle subInfo;
    private boolean required;
    public void initTable(int depth,List<TableItem> responseTable){
        if(subInfo!=null){
            responseTable.add(new TableItem(depth,field+"["+description+"]"));
            subInfo.initTable(responseTable);
        }else{
            responseTable.add(new TableItem(depth,field,type,"",required,description));
        }
    }
}

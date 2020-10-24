package com.idragon.dfdemo.util.detaildesign;

import com.idragon.tool.base.StringUtils;
import lombok.Data;

/**
 * 表格项目
 * @author chenxinjun
 */
@Data
public class TableItem {
    private String name;
    private String type;
    private String length;
    private boolean required;
    private String desc;
    /**
     * 参数深度
     */
    private int depth;
    public TableItem(int depth,String desc) {
        this.depth=depth;
        this.desc=desc;
    }

    public TableItem(int depth,String name, String type, String length, boolean required, String desc) {
        this.depth=depth;
        this.name = name;
        this.type = type;
        this.length = length;
        this.required = required;
        this.desc = desc;
    }

    @Override
    public String toString() {
        StringBuffer sb=new StringBuffer();
        for(int i=1;i<depth;i++){
            sb.append("\t");
        }
        if(StringUtils.isBlank(this.name)){
            return sb.toString()+desc;
        }else{
            return sb.toString()+name+"\t"+type+"\t"+length+"\t"+required+"\t"+desc;
        }
    }
}

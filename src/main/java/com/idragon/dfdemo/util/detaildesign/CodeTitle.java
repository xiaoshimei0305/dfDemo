package com.idragon.dfdemo.util.detaildesign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenxinjun
 */
@Data
public class CodeTitle {
    private String code;
    private String title;
    /**
     * 参数深度
     */
    private int depth;
    private List<FieldInfo> fieldInfoList;

    public CodeTitle(JSONObject info) {
        this(null,info);
    }

    /**
     *
     * @param info
     */
    public CodeTitle(CodeTitle parent,JSONObject info) {
        if(parent==null){
            this.depth=1;
        }else{
            this.depth=parent.getDepth()+1;
        }
        if(info!=null){
            this.code=info.getString("code");
            this.title=info.getString("title");
            this.fieldInfoList=new ArrayList<>();
            JSONArray propertiesList = info.getJSONArray("propertiesList");
            if(propertiesList!=null&&propertiesList.size()>0){
                for(int i=0;i<propertiesList.size();i++){
                    JSONObject item=propertiesList.getJSONObject(i);
                    if(item!=null){
                        FieldInfo fieldInfo=new FieldInfo();
                        fieldInfo.setField(item.getString("field"));
                        fieldInfo.setRequired(item.getBoolean("required"));
                        fieldInfo.setType(item.getString("type"));
                        fieldInfo.setDescription(item.getString("description"));
                        if(item.containsKey("subInfo")){
                            fieldInfo.setSubInfo(new CodeTitle(this,item.getJSONObject("subInfo")));
                        }
                        this.fieldInfoList.add(fieldInfo);
                    }
                }

            }

        }
    }

    public void initTable(List<TableItem> responseTable){
        responseTable.add(new TableItem(this.getDepth(),title+"（"+code+"）"));
        if(this.fieldInfoList!=null&&this.fieldInfoList.size()>0){
            for(int i=0;i<this.fieldInfoList.size();i++){
                this.fieldInfoList.get(i).initTable(this.getDepth(),responseTable);
            }
        }
    }

}

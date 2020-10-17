package com.idragon.dfdemo.util.detaildesign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.StringUtils;
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
    private CodeTitle parent;

    public CodeTitle(JSONObject info) {
        this(null,info);
    }

    /**
     *
     * @param info
     */
    public CodeTitle(CodeTitle parent,JSONObject info) {
        this.fieldInfoList=new ArrayList<>();
        if(parent==null){
            this.depth=1;
        }else{
            this.depth=parent.getDepth()+1;
            this.parent=parent;
        }
        if(info!=null){
            this.code=info.getString("code");
            this.title=info.getString("title");
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
                            JSONObject data=item.getJSONObject("subInfo");
                            if(data!=null&&data.containsKey("result")){
                                if(data.getBoolean("result")){
                                    fieldInfo.setSubInfo(new CodeTitle(this,data));
                                }
                            }
                        }
                        this.fieldInfoList.add(fieldInfo);
                    }
                }

            }

        }
    }

    public void initTable(List<TableItem> responseTable){
        responseTable.add(new TableItem(this.getDepth(),title+"（"+code+"）"));
        for(int i=0;i<this.fieldInfoList.size();i++){
            this.fieldInfoList.get(i).initTable(this.getDepth(),responseTable);
        }
    }

    public String getParentPath(){
        StringBuffer sb=new StringBuffer();
        sb.append(getDepMark());
        if(!StringUtils.isBlank(code)){
            sb.append(code);
        }
        return sb.toString();
    }

    public String getDepMark(){
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<depth-1;i++){
            sb.append("\t");
        }
        return sb.toString();
    }

}

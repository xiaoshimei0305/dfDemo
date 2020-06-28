package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * swagger 接口信息收集工具
 * @author chenxinjun
 */
public class SwaggerInfoUtils {

    private String url;

    private String modelName;

    private String[] ignoreTags=new String[]{"basic-error-controller","operation-handler","web-mvc-links-handler","server-controller"};

    public SwaggerInfoUtils(String url,String modelName) {
        this.url = url;
        this.modelName=modelName;
    }
    private String getTag(JSONObject data){
        return data.getJSONArray("tags").getString(0);
    }

    /**
     * 判断check是否合法
     * @param tag
     * @return
     */
    private boolean checkTag(String tag){
        if(StringUtils.isBlank(tag)){
            return false;
        }
        for(int i=0;i<ignoreTags.length;i++){
            if(ignoreTags[i].equalsIgnoreCase(tag)){
                return false;
            }
        }
        return true;
    }


    public JSONArray getMethods(){
        JSONArray ja=new JSONArray();
        JSONObject resutlJson= HttpRequestUtils.get(url);
        if(resutlJson!=null){
            JSONObject paths=resutlJson.getJSONObject("paths");
            Set<String> keys=paths.keySet();
            for(String key:keys){
                JSONObject methodJson=new JSONObject();
                JSONObject data=paths.getJSONObject(key);

                if(data.containsKey("get")){
                    data=data.getJSONObject("get");
                }else if(data.containsKey("post")){
                    data=data.getJSONObject("post");
                }else{
                    continue;
                }
                String tag=getTag(data);
                if(checkTag(tag)){
                    // 添加接口信息
                    methodJson.put("url",key);
                    methodJson.put("name",data.getString("summary").replaceAll(",","||"));
                    methodJson.put("tag",tag);
                    methodJson.put("modelName",modelName);
                    ja.add(methodJson);
                }
            }
        }
        return ja;
    }

    public static String getToExcelStr(JSONArray methodList){
        StringBuffer sb=new StringBuffer();
        if(methodList!=null&&methodList.size()>0){
            for(int i=0;i<methodList.size();i++){
                sb.append(getMethodLine(methodList.getJSONObject(i))).append("\n");
            }
        }
        return sb.toString();
    }


    private static String getMethodLine(JSONObject item){
        return item.getString("modelName")+","+item.getString("tag")+","+item.getString("name")+","+item.getString("url");
    }

    public static JSONArray getMethodByModelName(String modelName){
        SwaggerInfoUtils tools=new SwaggerInfoUtils("http://fcm-dev.ocj.com.cn/api/newMedia/"+modelName+"/v2/api-docs",modelName);
        return tools.getMethods();
    }




    public static void main(String[] args) throws IOException {
        JSONArray methodList=new JSONArray();
        methodList.addAll(getMethodByModelName("cms"));
        methodList.addAll(getMethodByModelName("login"));
        methodList.addAll(getMethodByModelName("trade"));
        methodList.addAll(getMethodByModelName("member"));
        methodList.addAll(getMethodByModelName("search"));
        methodList.addAll(getMethodByModelName("item"));
        methodList.addAll(getMethodByModelName("marketing"));
        FileUtils.writeFileContent(new File("/Users/chenxinjun/Downloads/fuck.csv"),getToExcelStr(methodList),"GBK");
        System.out.println("=================================");
    }
}

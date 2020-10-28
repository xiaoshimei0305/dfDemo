package com.idragon.dfdemo.util.detaildesign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.HttpRequestUtils;
import store.idragon.tool.base.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * swagger 接口信息收集工具
 * @author chenxinjun
 */
public class SwaggerInfoUtils {

    private String url;

    private String modelName;

    private String[] ignoreTags=new String[]{"basic-error-controller","operation-handler","web-mvc-links-handler","server-controller"};
    /**
     * 忽略接口定义
     */
    public static Set<String> ingoreMethodSet=null;

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

    /**
     * 获取参数请求例子
     * @param name
     * @param objectMap
     * @return
     */
    public JSONObject getExample(String name,JSONObject objectMap){
        JSONObject example=new JSONObject();
        if(StringUtils.isBlank(name)){
            return example;
        }
        name=name.replace("#/definitions/","").trim();
        JSONObject objectInfo=objectMap.getJSONObject(name);
        if(objectInfo==null){
            return example;
        }
        JSONObject properties = objectInfo.getJSONObject("properties");
        if(properties==null){
            return example;
        }
        Set<String> propertiesKeys = properties.keySet();
        for(String itemKey:propertiesKeys){
            JSONObject itemContent=properties.getJSONObject(itemKey);
            //引用实体定义
            if(itemContent.containsKey("$CherryRef")){
                example.put(itemKey,getExample(itemContent.getString("$CherryRef"),objectMap));
            }else if(itemContent.containsKey("example")){
                example.put(itemKey,itemContent.getString("example"));
            }else{
                String type=itemContent.getString("type");
                if("array".equalsIgnoreCase(type)){
                    example.put(itemKey,new JSONArray());
                    if(itemContent.containsKey("items")){
                        String typeObject= itemContent.getJSONObject("items").getString("$CherryRef");
                        //死循环情况，中止操作
                        if(!StringUtils.isBlank(typeObject)&&typeObject.replace("#/definitions/","").equalsIgnoreCase(name)){
                            example.getJSONArray(itemKey).add(new JSONArray());
                        }else{
                            example.getJSONArray(itemKey).add(getExample(typeObject,objectMap));
                        }
                    }
                } else if("object".equalsIgnoreCase(type)){
                    example.put(itemKey,new JSONObject());
                }else{
                    example.put(itemKey,type);
                }
            }
        }
        return example;
    }

    /**
     * 获取指定对象的属性列表
     * @param name
     * @param objectMap
     * @return
     */
    public JSONObject getObjectProperties(String name,JSONObject objectMap){
        JSONObject objectDesc=new JSONObject();
        if(StringUtils.isBlank(name)){
            objectDesc.put("desc","字段名称不能为空");
            objectDesc.put("result",false);
            return objectDesc;
        }
        name=name.replace("#/definitions/","").trim();
        objectDesc.put("code",name);
        JSONObject objectInfo=objectMap.getJSONObject(name);
        if(objectInfo==null){
            objectDesc.put("desc","暂时未获取到指定实体描述信息"+name);
            objectDesc.put("result",false);
            return objectDesc;
        }
        objectDesc.put("title",objectInfo.getString("title"));
        objectDesc.put("type",objectInfo.getString("type"));
        JSONArray requiredList = objectInfo.getJSONArray("required");
        JSONObject properties = objectInfo.getJSONObject("properties");
        JSONArray propertiesList=new JSONArray();
        if(properties!=null){
            Set<String> propertiesKeys = properties.keySet();
            for(String itemKey:propertiesKeys){
                JSONObject item=new JSONObject();
                JSONObject itemContent=properties.getJSONObject(itemKey);
                item.put("field",itemKey);
                item.put("description",itemContent.getString("description"));
                item.put("format",itemContent.getString("format"));
                item.put("type",itemContent.getString("type"));
                item.put("required",requiredList==null?false:requiredList.contains(itemKey));
                if(itemContent.containsKey("example")){
                    item.put("example",itemContent.getString("example"));
                }else{
                    item.put("example",itemContent.getString("type"));
                }
                //引用实体定义
                if(itemContent.containsKey("$CherryRef")){
                    item.put("subInfo",getObjectProperties(itemContent.getString("$CherryRef"),objectMap));
                }else if("array".equalsIgnoreCase(itemContent.getString("type"))){
                    if(itemContent.containsKey("items")){
                        String typeObject= itemContent.getJSONObject("items").getString("$CherryRef");
                        //死循环情况，中止操作
                        if(!StringUtils.isBlank(typeObject)&&typeObject.replace("#/definitions/","").equalsIgnoreCase(name)){
                            item.put("subInfo",new JSONObject());
                        }else{
                            item.put("subInfo",getObjectProperties(typeObject,objectMap));
                        }
                    }
                }
                propertiesList.add(item);
            }
        }
        objectDesc.put("propertiesList",propertiesList);
        objectDesc.put("result",true);
        return objectDesc;
    }


    public JSONArray getMethods(){
        JSONArray ja=new JSONArray();
        JSONObject resultJson= HttpRequestUtils.getReplaceRef(url);
        if(resultJson!=null){
            JSONObject paths=resultJson.getJSONObject("paths");
            //获取定义的实体信息
            JSONObject objectMap=resultJson.getJSONObject("definitions");
            Set<String> keys=paths.keySet();
            for(String key:keys){
                if(getIgnoreSet().contains(key)){
                    System.out.println("接口被忽略掉[反过来]："+key);
                    continue;
                }
                if(DetailDesignUtils.debuggerAddress.equalsIgnoreCase(key)){
                    System.out.println("这里要进行调试了");
                }
                JSONObject methodJson=new JSONObject();
                JSONObject data=paths.getJSONObject(key);
                String postWay="POST";
                if(data.containsKey("get")){
                    data=data.getJSONObject("get");
                    postWay="GET";
                }else if(data.containsKey("post")){
                    data=data.getJSONObject("post");
                }else{
                    continue;
                }
                String tag=getTag(data);
                //过滤不被使用的接口
                if(data.getBoolean("deprecated")){
                    continue;
                }
                if(checkTag(tag)){
                    // 添加接口信息
                    methodJson.put("url",key);
                    methodJson.put("name",data.getString("summary").replaceAll(",","||"));
                    methodJson.put("tag",tag);
                    methodJson.put("postWay",postWay);
                    methodJson.put("modelName",modelName);
                    JSONArray parameters=data.getJSONArray("parameters");
                    if(parameters==null){
                        parameters=new JSONArray();
                    }
                    JSONObject requestExample=new JSONObject();
                    if(parameters.size()>0){
                        for(int i=0;i<parameters.size();i++){
                            JSONObject item=parameters.getJSONObject(i);
                            String type=item.getString("type");
                            if(!StringUtils.isBlank(type)){
                                if("array".equalsIgnoreCase(type)){
                                    requestExample.put(item.getString("name"),new JSONArray());
                                } else if("object".equalsIgnoreCase(type)){
                                    requestExample.put(item.getString("name"),new JSONObject());
                                }else{
                                    requestExample.put(item.getString("name"),type);
                                }
                            }
                            if(item.containsKey("schema")){
                                String myRef=item.getJSONObject("schema").getString("$CherryRef");
                                requestExample.put(item.getString("name"),getExample(myRef,objectMap));
                                item.put("properties",getObjectProperties(myRef,objectMap));
                            }
                        }
                    }
                    //请求
                    methodJson.put("request",parameters);
                    methodJson.put("requestExample",requestExample);
                    //响应
                    methodJson.put("response",data.getJSONObject("responses").getJSONObject("200"));
                    JSONObject response=methodJson.getJSONObject("response");
                    if(response.containsKey("schema")){
                        String myRef=response.getJSONObject("schema").getString("$CherryRef");
                        response.put("example",getExample(myRef,objectMap));
                        response.put("properties",getObjectProperties(myRef,objectMap));
                    }
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
        StringBuffer sb=new StringBuffer(item.getString("modelName"));
        sb.append(",").append(item.getString("tag"));
        sb.append(",").append(item.getString("name"));
        sb.append(",").append(item.getString("url"));
        sb.append(",").append("http://fcm-uat.ocj.com.cn/api/newMedia/").append(item.getString("modelName")).append("/swagger-ui.html#").append(item.getString("tag"));
        sb.append(",").append("http://fcm-uat.ocj.com.cn").append(item.getString("url"));
        return sb.toString();
    }

    /**
     * 获取指定模块到swagger信息
     * @param modelName 模块名称
     * @param groupName 分组
     * @return
     */
    public static JSONArray getMethodByModelName(String modelName,String groupName){
        String path="http://fcm-uat.ocj.com.cn/api/newMedia/"+modelName+"/v2/api-docs";
        if(!StringUtils.isBlank(groupName)){
            path+="?group="+groupName;
        }
        SwaggerInfoUtils tools=new SwaggerInfoUtils(path,modelName);
        return tools.getMethods();
    }
    public static JSONArray getMethodByModelName(String modelName){
        return getMethodByModelName(modelName,null);
    }
    /**
     * 获取过滤的方法
     * @return
     */
    private Set<String> getIgnoreSet(){
        if(ingoreMethodSet==null){
            ingoreMethodSet=new HashSet<>();
            String[] ignoreList = ignoreMethodAddress.split("\n");
            for(String item:ignoreList){
                if(!StringUtils.isBlank(item)){
                    ingoreMethodSet.add(item.trim());
                }
            }
        }
        return ingoreMethodSet;
    }

    /**
     * 获取fcm所有接口列表信息
     * @return
     */
    public static JSONArray getFcmInterfaceInfo(){
        JSONArray methodList=new JSONArray();
        methodList.addAll(getMethodByModelName("cms"));
        methodList.addAll(getMethodByModelName("login"));
        methodList.addAll(getMethodByModelName("trade"));
        methodList.addAll(getMethodByModelName("member"));
        methodList.addAll(getMethodByModelName("search"));
        methodList.addAll(getMethodByModelName("item"));
        methodList.addAll(getMethodByModelName("marketing"));
        return methodList;
    }
    private static String ignoreMethodAddress="接口地址\n";

}

package com.idragon.dfdemo.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.util.detaildesign.SwaggerInfoUtils;
import store.idragon.tool.base.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

/**
 * @author xiaoshimei0305
 * date  2020/11/6 4:59 下午
 * description
 * @version 1.0
 */
public class OcjVueRouterRead {

    public static JSONObject read(String dir){
        JSONObject data=new JSONObject();
        File file=new File(dir);
        File[] list = file.listFiles();
        for(File item : list){
            String key=item.getName();
            if(!item.isDirectory()){
                continue;
            }
            String routerName=dir+"/"+item.getName()+"/src/router.js";
            String content=FileUtils.readFileContent(new File(routerName));
            content=content.replace("let routes = [];","");
            int start = content.indexOf("routes = [");
            content=content.substring(start);
            content=content.substring(0,content.indexOf("];")+1);
            content=content.replaceAll("//.*","");
            content=content.replaceAll("'","\"");
            content=content.replaceAll("pages\\.[a-zA-Z0-9]*","\"\"");
            content=content.replaceAll(",[\\s]*\\]","]");
            content=content.replaceAll(",[\\s]*\\}","}");
            content=content.replaceAll("path:","\"path\":");
            content=content.replaceAll("component:","\"component\":");
            content=content.replaceAll("children:","\"children\":");
            content=content.replaceAll("redirect:","\"redirect\":");
            content=content.replace("routes = ","");
           // System.out.println(item.getName()+"|||\n"+content);
            JSONArray routerList= JSON.parseArray(content);
            data.put(key,routerList);
        }
        return data;
    }

    /**
     * 解析地址
     * @param list
     * @param parent
     * @param resultLit
     */
    private static void parsePath(String model,String parentPath,JSONArray routers,JSONObject parent,JSONArray resultLit){
        if(routers!=null&&routers.size()>0){
            for(int i=0;i<routers.size();i++){
                JSONObject item=routers.getJSONObject(i);
                if(item!=null){
                    JSONObject data=new JSONObject();
                    data.put("model",model);
                    String name=item.getString("name");
                    if(StringUtils.isBlank(name)){
                        if(item.containsKey("meta")&&item.getJSONObject("meta").containsKey("otk-param")&&item.getJSONObject("meta").getJSONObject("otk-param").containsKey("desc")){
                            name=item.getJSONObject("meta").getJSONObject("otk-param").getString("desc");
                        }
                    }
                    data.put("name",name);
                    if(StringUtils.isBlank(parentPath)){
                        data.put("path",item.getString("path"));
                    }else{
                        data.put("path",parentPath+"/"+item.getString("path"));
                    }
                    if(item.containsKey("children")){
                        JSONArray subList=item.getJSONArray("children");
                        if(subList!=null&&subList.size()>0){
                            parsePath(model,data.getString("path"),subList,item,resultLit);
                        }
                    }else{
                        resultLit.add(data);
                    }
                }
            }
        }
    }


    /**
     * 导出excel
     * @throws IOException
     */
    private static void exportExcel(String fileName,JSONArray paths) throws IOException {
        //数据存储到excel文档
        StringBuffer sb=new StringBuffer();
        for(int i=0;i<paths.size();i++){
            JSONObject item=paths.getJSONObject(i);
            sb.append(item.getString("model")).append(",");
            String name=item.getString("name");
            if(StringUtils.isBlank(name)){
                sb.append("").append(",");
            }else{
                sb.append(name).append(",");
            }

            sb.append(item.getString("path")).append("\n");
        }
        FileUtils.writeFileContent(new File(fileName),sb.toString(),"GBK");
    }

    public static void main(String[] args) throws IOException {
        String dir="/Users/chenxinjun/work/code/ocj/新系统前端";
        JSONObject data=read(dir);
        //System.out.println(data.toJSONString());
        //解析数据
        JSONArray paths=new JSONArray();
        Set<String> keys = data.keySet();
        for(String key:keys){
            parsePath(key,null,data.getJSONArray(key),null,paths);
        }
        exportExcel("/Users/chenxinjun/Downloads/pages.csv",paths);
//        for(int i=0;i<paths.size();i++){
//            JSONObject path=paths.getJSONObject(i);
//
//            System.out.println("["+i+"]:"+path.toJSONString());
//        }
       // System.out.println("得到的路径:"+paths.toJSONString());
        System.out.println("导出完毕！！"+paths.size());
    }
}

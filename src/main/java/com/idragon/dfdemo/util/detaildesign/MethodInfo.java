package com.idragon.dfdemo.util.detaildesign;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 方法名称
 * @author chenxinjun
 */
@Data
public class MethodInfo {
    /**
     * 是否IOS调用
     */
    private boolean ios;
    /**
     *  是否安卓调用
     */
    private boolean android;
    /**
     * 是否M版调用
     */
    private boolean m;
    /**
     * 是否小程序
     */
    private boolean mini;
    /**
     * 是否pc调用
     */
    private boolean pc;
    /**
     * 负责人
     */
    private String author;
    /**
     * 接口地址
     */
    private String address;
    /**
     * 接口名称
     */
    private String name;
    /**
     * 接口调用描述
     */
    private String rpcDesc;
    /**
     * 接口所在类
     */
    private String methodInClass;
    /**
     * 模块
     */
    private String model;
    /**
     * 降级说明
     */
    private String reduceDesc;

    private String remark;
    /**
     * 响应示例
     */
    private String responseExample;
    /**
     * 请求示例
     */
    private String requestExample;
    /**
     * 请求信息描述
     */
    private List<CodeTitle> request;

    /**
     * 响应信息描述
     */
    private CodeTitle response;
    /**
     * 响应表格数据
     */
    private List<TableItem> responseTable;
    /**
     * 请求表格数据
     */
    private List<TableItem> requestTable;


    public MethodInfo(JSONObject item) {
        this.model=item.getString("模块");
        this.methodInClass=item.getString("接口所在类");
        this.rpcDesc=item.getString("RPC接口调用说明");
        this.author=item.getString("接口开发人");
        this.address=item.getString("接口地址");
        this.ios="是".equalsIgnoreCase(item.getString("IOS"));
        this.android="是".equalsIgnoreCase(item.getString("安卓"));
        this.m="是".equalsIgnoreCase(item.getString("M端"));
        this.mini="是".equalsIgnoreCase(item.getString("小程序"));
        this.pc="是".equalsIgnoreCase(item.getString("PC"));
        this.reduceDesc=item.getString("降级情况");
        this.remark=item.getString("接口标签");
        this.name=item.getString("接口名称");
    }

    /**
     * 初始化swagger获取到的信息
     * @param info
     */
    public void initSwaggerInfo(JSONObject info){
        if(info!=null){
            JSONObject item=info.getJSONObject(this.address);
            if(item!=null){
                JSONObject response=item.getJSONObject("response");
                this.requestTable=new ArrayList<>();
                this.responseTable=new ArrayList<>();
                this.request=new ArrayList<>();
                if(response!=null){
                    JSONObject example=response.getJSONObject("example");
                    if(example!=null){
                        this.responseExample=example.toJSONString();
                    }else{
                        this.responseExample="{}";
                    }
                    this.response=new CodeTitle(response.getJSONObject("properties"));
                    this.response.initTable(responseTable);
                }
                this.requestExample=item.getString("requestExample");
                JSONArray requestList=item.getJSONArray("request");
                if(requestList!=null&&requestList.size()>0){
                    for(int i=0;i<requestList.size();i++){
                        CodeTitle codeTitle=new CodeTitle(requestList.getJSONObject(i).getJSONObject("properties"));
                        codeTitle.initTable(requestTable);
                        this.request.add(codeTitle);
                    }
                }
                System.out.println("=========requestTable===============");
                for(int i=0;i<this.requestTable.size();i++){
                    System.out.println(this.requestTable.get(i));
                }
                System.out.println("=========responseTable===============");
                for(int i=0;i<this.responseTable.size();i++){
                    System.out.println(this.responseTable.get(i));
                }
                System.out.println("=========end===============");
            }
        }
    }

}

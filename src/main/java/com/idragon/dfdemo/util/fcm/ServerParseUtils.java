package com.idragon.dfdemo.util.fcm;



import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import com.idragon.dfdemo.util.fcm.dto.ServiceInfo;

import java.util.*;

/**
 * @author chenxinjun
 * 解析服务接口
 */
public class ServerParseUtils {

    private Map<String, ServiceInfo> serverInfoMap;

    /**
     *
     * @param serverInfos
     */
    public ServerParseUtils(List<InterfaceInfo> interfaceInfos) {
        this.serverInfoMap = new HashMap<>();
        if(interfaceInfos!=null&&interfaceInfos.size()>0){
            for(InterfaceInfo info:interfaceInfos){
                addInterface(info);
            }
        }
    }

    /**
     * 容器添加接口
     * @param interfaceInfo
     */
    public void addInterface(InterfaceInfo interfaceInfo){
        String name=interfaceInfo.getClassName();
        ServiceInfo serviceInfo=serverInfoMap.get(name);
        if(serviceInfo==null){
            serviceInfo=new ServiceInfo();
            serverInfoMap.put(name,serviceInfo);
        }
        serviceInfo.addInterface(interfaceInfo);
    }

    /**
     * 获取服务列表
     * @return
     */
    public List<ServiceInfo> getServiceList(){
        List<ServiceInfo> list=new ArrayList<>();
        Set<String> keys = this.serverInfoMap.keySet();
        for(String key:keys){
            list.add(this.serverInfoMap.get(key));
        }
        return list;
    }

}

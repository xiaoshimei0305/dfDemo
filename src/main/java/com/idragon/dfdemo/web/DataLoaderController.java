package com.idragon.dfdemo.web;

import com.alibaba.fastjson.JSONObject;
import com.idragon.dfdemo.server.DataLoaderService;
import com.idragon.dfdemo.util.fcm.dto.BeanInfo;
import com.idragon.dfdemo.util.fcm.dto.InterfaceInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author chenxinjun
 * 数据加载类
 */
@RequestMapping("/dataLoader")
@Api("excel文件处理")
@RestController
public class DataLoaderController {
    @Autowired
    DataLoaderService dataLoaderService;

    @ApiOperation(value = "获取excel表格数据", notes="通过本机文件获取表格数据JSON格式")
    @RequestMapping(value = "JSONData", method= RequestMethod.POST)
    @ResponseBody
    public JSONObject getJSONData(@RequestParam(value = "excelFile",required = false) String excelFile) throws IOException {
        return dataLoaderService.getJSONData(excelFile);
    }
    @ApiOperation(value = "获取实体对象", notes="解析实体对象信息")
    @RequestMapping(value = "beanList", method= RequestMethod.POST)
    @ResponseBody
    public List<BeanInfo> getBeanList(@RequestParam(value = "excelFile",required = false) String excelFile) throws IOException {
        return dataLoaderService.getBeanList(excelFile);
    }
    @ApiOperation(value = "获取接口对象信息", notes="解析接口信息")
    @RequestMapping(value = "InterfaceList", method= RequestMethod.POST)
    @ResponseBody
    public List<InterfaceInfo> getInterfaceList(@RequestParam(value = "excelFile",required = false) String excelFile) throws IOException {
        return dataLoaderService.getInterfaceList(excelFile);
    }
}

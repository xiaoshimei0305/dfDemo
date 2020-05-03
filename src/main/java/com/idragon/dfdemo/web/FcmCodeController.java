package com.idragon.dfdemo.web;

import com.idragon.dfdemo.server.FcmCodeService;
import freemarker.template.TemplateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @author chenxinjun
 * 代码生成控制层
 */
@RequestMapping("/fcmCode")
@Api("fcm 代码文档处理类")
@RestController
public class FcmCodeController {
    @Autowired
    FcmCodeService fcmCodeService;


    @ApiOperation(value = "生成指定实体代码", notes="生成指定实体的代码文档")
    @RequestMapping(value = "buildBeanCodeFile", method= RequestMethod.POST)
    @ResponseBody
    public void buildBeanCodeFile(@RequestParam(value = "excelFile",required = false) String excelFile,
                                   @RequestParam(value = "beanCode",required = false) String beanCode) throws IOException, TemplateException {
        fcmCodeService.buildBeanCodeFile(excelFile,beanCode);
    }
}
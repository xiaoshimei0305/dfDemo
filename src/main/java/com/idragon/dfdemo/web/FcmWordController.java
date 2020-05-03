package com.idragon.dfdemo.web;

import com.idragon.dfdemo.server.FcmWordService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


/**
 * word文档处理工具
 * @author chenxinjun
 */
@RequestMapping("/fcmWord")
@Api("fcm word文档处理类")
@RestController
public class FcmWordController {
    @Autowired
    FcmWordService fcmWordService;


    @ApiOperation(value = "生成所有实体文档", notes="生成所有实体文档，并放到指定目录下面")
    @RequestMapping(value = "buildBeansWordFile", method= RequestMethod.POST)
    @ResponseBody
    public void buildBeansWordFile(@RequestParam(value = "excelFile",required = false) String excelFile,
                                  @RequestParam(value = "beanDir",required = false) String beanDir) throws Exception {
        fcmWordService.buildBeansWordFile(excelFile,beanDir);
    }
    @ApiOperation(value = "生成文档", notes="生成目标文旦")
    @RequestMapping(value = "buildWorFile", method= RequestMethod.POST)
    @ResponseBody
    public void buildWorFile(@RequestParam(value = "excelFile",required = false) String excelFile,
                            @RequestParam(value = "modelFileName",required = false) String modelFileName,
                             @RequestParam(value = "resultFileName",required = false) String resultFileName) throws Exception {
        fcmWordService.buildWorFile(excelFile,modelFileName,resultFileName);
    }
}

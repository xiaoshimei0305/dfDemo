package cn.com.ocj.giant.platform.newmedia.api.${modelCode}.web;


import cn.com.ocj.giant.framework.api.rest.dto.RestResponse;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
<#list importPackageList as item>
import ${item};
</#list>
//[personImport]CodeStart
$${(unChangedCode.personImport)!}
//CodeEnd
/**
* @author
*/
@ApiOperation("${name}")
@RestController
@CrossOrigin
@Slf4j
@RequestMapping(value = "${classPath}")
public class ${code?cap_first}Controller{
    //[Autowired]CodeStart
    ${(unChangedCode.Autowired)!}
    //CodeEnd
<#list methods as method>
    @ApiOperation("${method.remark}")
    @PostMapping(name = "${method.methodPath}", value = "/${method.methodPath}")
    public @ResponseBody RestResponse<${method.outType?cap_first}> ${method.methodName?uncap_first}(@RequestBody ${method.inType?cap_first} ${method.inType?uncap_first}) {
    //[${method.methodName?uncap_first}]CodeStart
        ？？？
    //CodeEnd
        return null;
    }
</#list>

}
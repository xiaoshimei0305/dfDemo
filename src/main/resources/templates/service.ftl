package cn.com.ocj.giant.platform.newmedia.api.${modelCode}.server.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
<#list importPackageList as item>
    import ${item};
</#list>
//[personImport]CodeStart
${(unChangedCode.personImport)!}
//CodeEnd
/**
* @author:tanhh
* @description:${name}
* @create: 2020-04-11 10:48
**/
@Service
@Slf4j
public class ${code?cap_first}Service{
    //[Autowired]CodeStart
    ${(unChangedCode.Autowired)!}
    //CodeEnd
<#list methods as method>

    /**
    * ${method.remark}
    *
    * @param ${method.inType?uncap_first} ${method.inName}
    * @return ${method.outName}
    */
    public ${method.outType?cap_first} ${method.methodName?uncap_first}(${method.inType?cap_first} ${method.inType?uncap_first}) {
        return null;
    }
</#list>
}
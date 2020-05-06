package cn.com.ocj.giant.platform.newmedia.api.${modelCode}.server.adapater;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import cn.com.ocj.giant.framework.server.exception.AdaptorFailureException;
import cn.com.ocj.giant.framework.server.exception.GiantException;
<#list importPackageList as item>
import ${item};
</#list>
//[personImport]CodeStart
${(unChangedCode.personImport)!}
//CodeEnd
/**
* @description: ${name}
* @author: ${author}
* @create: 2019-07-31 17:09
**/
@Component
@Slf4j
public class ${code}Api{
        //[Autowired]CodeStart
                ${(unChangedCode.Autowired)!}
        //CodeEnd
<#list methods as method>

        /**
        * @Description: ${method.remark}
        * @Param: ${method.inType?uncap_first}: ${method.inName}
        * @return: ${method.outType?cap_first}: ${method.outName}
        * @Author: 石权
        * @Date: 2020/4/11
        */
        @HystrixCommand(ignoreExceptions = {GiantException.class, AdaptorFailureException.class})
        public ${method.outType?cap_first} ${method.methodName?uncap_first}(${method.inType?cap_first} ${method.inType?uncap_first}) {
            return null;
        }
</#list>
}
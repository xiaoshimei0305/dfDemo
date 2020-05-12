package ${packageName};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Date;
import java.util.Map;
import cn.com.ocj.giant.framework.api.rest.dto.AbstractQueryRestRequest;
import cn.com.ocj.giant.framework.api.util.ParamUtil;
<#list importPackageList as item>
import ${item};
</#list>
//[personImport]CodeStart
${(unChangedCode.personImport)!}
//CodeEnd
/**
* ${remark}
* @author ${author}
*/
@ApiModel(description = "${name}")
@Data
public class ${code}${type.suffixName} extends AbstractQueryRestRequest {
    //[Autowired]CodeStart
    ${(unChangedCode.Autowired)!}
    //CodeEnd
<#list fieldList as field>
    @ApiModelProperty(value = "${field.name}", required = ${field.require?string('true','false')})
    private ${field.type} ${field.code};
</#list>
    @Override
    public void checkInput() {
        super.checkInput();
        <#list fieldList as field>
            <#if field.require>
                ParamUtil.nonNull(${field.code}, "${field.name}不能为空");
            </#if>
        </#list>
    }

}
package ${packageName};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.math.BigDecimal;
import java.util.*;
import cn.com.ocj.giant.framework.api.dto.AbstractOutputInfo;
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
public class ${code}${type.suffixName} extends AbstractOutputInfo {
    //[Autowired]CodeStart
    ${(unChangedCode.Autowired)!}
    //CodeEnd
<#list fieldList as field>
    @ApiModelProperty(value = "${field.name}", required = ${field.require?string('true','false')})
    private ${field.type} ${field.code};
</#list>
}
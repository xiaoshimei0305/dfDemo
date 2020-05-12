package ${packageName};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
import java.util.Date;
import java.util.Map;
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
public class ${code}${type.suffixName} {
    //[Autowired]CodeStart
    ${(unChangedCode.Autowired)!}
    //CodeEnd
<#list fieldList as field>
    @ApiModelProperty(value = "${field.name}", required = ${field.require?string('true','false')})
    private ${field.type} ${field.code};
</#list>
}
package ${packageName};

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
<#list importPackageList as item>
import ${item};
</#list>

/**
* ${remark}
* @author chenxinjun
*/
@ApiModel(description = "${name}")
@Data
public class ${code}{
<#list fieldList as field>
    @ApiModelProperty(value = "${field.name}", required = ${field.require?string('true','false')})
    private ${field.type} ${field.code};
</#list>
}
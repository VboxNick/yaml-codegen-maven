<#assign pojoClassName = output.file.name?keep_before(".")>
<#assign fields = model.content.fields>

public final class ${pojoClassName} {

<#list fields as f>
    private final ${f.type?cap_first} ${f.name};
</#list>

    public ${pojoClassName}(<#list fields as f>final ${f.type?cap_first} ${f.name}<#sep>, </#sep></#list>) {
    <#list fields as f>
        this.${f.name} = ${f.name};
    </#list>
    }

    <#list fields as f>
    public ${f.type?cap_first} get${f.name?cap_first}() {
        return ${f.name};
    }

    </#list>
}
<#ftl outputFormat="plainText">
Welcome to ${model.content.name}!
<#list model.content.employees as employee>
    ${employee.name}
    ${employee.title}
</#list>
--
customVarKey=${tmpl.vars.customVarKey}
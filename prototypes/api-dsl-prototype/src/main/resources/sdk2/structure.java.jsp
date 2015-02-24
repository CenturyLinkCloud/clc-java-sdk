

public class ${structure.name} {

    <% for (field in structure.fields) { %>
    private ${Types.classOf(field.type).name} ${field.name};
    <% } %>

    <% for (field in structure.fields) { %>
    /**
     * ${field.description}
     */
    public ${structure.name} ${field.name}(${Types.classOf(field.type).name} ${field.name}) {
        this.${field.name} = ${field.name};
        return this;
    }
    <% } %>

    <% for (field in structure.fields) { %>
    /**
     * ${field.description}
     */
    public ${Types.classOf(field.type).name} get${field.name.capitalize()}() {
        return ${field.name};
    }
    <% } %>
}
package ${cls.package.name};

public class ${cls.shortName}Payload {

    public static ${cls.name}Payload builder() {
        return new ${cls.name}Payload();
    }

    <% for (field in cls.fields) { %>
    private ${field.type.name} ${field.name};
    <% } %>

    <% for (field in cls.fields) { %>
    public ${cls.name}Payload ${field.name}(${field.type.name} ${field.name}) {
        this.${field.name} = ${field.name};
        return this;
    }
    <% } %>

    public ${cls.name} build() {
        final ${cls.name} data = new ${cls.name}();

        <% for(field in cls.fields) {%>
        data.${field.setter.name}(this.${field.name});
        <% } %>

        return data;
    }
}
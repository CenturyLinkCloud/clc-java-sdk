
function ${cls.shortName}() {
    var self = this;
    var data = { };

    <% for (field in cls.fields) { %>
    self.${field.name} = function (a${field.name}) {
        data.${field.name} = a${field.name};
        return self;
    };
    <% } %>

    self.build = function () {
        return data;
    };
}
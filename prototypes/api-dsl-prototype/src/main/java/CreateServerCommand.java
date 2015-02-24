public class CreateServerCommand {


    private java.lang.String name;

    private java.lang.String groupId;

    private java.lang.String sourceServerId;

    private java.lang.Integer cpu;

    private java.lang.Integer memoryGB;

    private java.lang.Integer type;



    /**
     * Name of the server to create. Alphanumeric characters and dashes only. Must be between 1-7 characters depending on the length of the account alias. (This name will be appended with a two digit number and prepended with the datacenter code and account alias to make up the final server name.)
     */
    public CreateServerCommand name(java.lang.String name) {
        this.name = name;
        return this;
    }

    /**
     * ID of the parent group. Retrieved from query to parent group, or by looking at the URL on the UI pages in the Control Portal.
     */
    public CreateServerCommand groupId(java.lang.String groupId) {
        this.groupId = groupId;
        return this;
    }

    /**
     * ID of the server to use a source. May be the ID of a template, or when cloning, an existing server ID. The list of available templates for a given account in a data center can be retrieved from the Get Data Center Deployment Capabilities API operation.
     */
    public CreateServerCommand sourceServerId(java.lang.String sourceServerId) {
        this.sourceServerId = sourceServerId;
        return this;
    }

    /**
     * Number of processors to configure the server with (1-16)
     */
    public CreateServerCommand cpu(java.lang.Integer cpu) {
        this.cpu = cpu;
        return this;
    }

    /**
     * Number of GB of memory to configure the server with (1-128)
     */
    public CreateServerCommand memoryGB(java.lang.Integer memoryGB) {
        this.memoryGB = memoryGB;
        return this;
    }

    /**
     * Whether to create standard or hyperscale server
     */
    public CreateServerCommand type(java.lang.Integer type) {
        this.type = type;
        return this;
    }



    /**
     * Name of the server to create. Alphanumeric characters and dashes only. Must be between 1-7 characters depending on the length of the account alias. (This name will be appended with a two digit number and prepended with the datacenter code and account alias to make up the final server name.)
     */
    public java.lang.String getName() {
        return name;
    }

    /**
     * ID of the parent group. Retrieved from query to parent group, or by looking at the URL on the UI pages in the Control Portal.
     */
    public java.lang.String getGroupId() {
        return groupId;
    }

    /**
     * ID of the server to use a source. May be the ID of a template, or when cloning, an existing server ID. The list of available templates for a given account in a data center can be retrieved from the Get Data Center Deployment Capabilities API operation.
     */
    public java.lang.String getSourceServerId() {
        return sourceServerId;
    }

    /**
     * Number of processors to configure the server with (1-16)
     */
    public java.lang.Integer getCpu() {
        return cpu;
    }

    /**
     * Number of GB of memory to configure the server with (1-128)
     */
    public java.lang.Integer getMemoryGB() {
        return memoryGB;
    }

    /**
     * Whether to create standard or hyperscale server
     */
    public java.lang.Integer getType() {
        return type;
    }

}

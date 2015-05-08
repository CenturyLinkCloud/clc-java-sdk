package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.ip.CreatePublicIpConfig;
import com.centurylink.cloud.sdk.servers.services.domain.ip.port.PortConfig;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;


/**
 * @author ilya.drabenia
 */
public class CreateServerConfig implements ServerConfig {
    private String id;
    private String name;
    private String description;
    private ServerType type;
    private Group group;
    private Template template;
    private Machine machine = new Machine();
    private String password;
    private NetworkConfig network = new NetworkConfig();
    private TimeToLive timeToLive;
    private boolean managedOS = false;

    public static CreateServerConfig baseServerConfig() {
        return new CreateServerConfig()
            .name("ALTR")
            .type(STANDARD)

            .machine(new Machine()
                    .cpuCount(1)
                    .ram(2)
            )

            .template(Template.refByOs()
                    .dataCenter(US_CENTRAL_SALT_LAKE_CITY)
                    .type(CENTOS)
                    .version("6")
                    .architecture(x86_64)
            )
            .timeToLive(ZonedDateTime.now().plusHours(2));
    }

    public static CreateServerConfig mysqlServer() {
        return baseServerConfig()
            .name("MySQL")
            .description("MySQL");
    }

    public static CreateServerConfig nginxServer() {
        CreateServerConfig nginx = baseServerConfig();

        nginx.getMachine()
            .disk(new DiskConfig()
                .type(DiskType.RAW)
                .size(10));

        return nginx
            .name("Nginx")
            .description("Nginx")
            .network(new NetworkConfig()
                .publicIpConfig(new CreatePublicIpConfig()
                    .openPorts(PortConfig.HTTP)));
    }

    public static CreateServerConfig apacheHttpServer() {
        return baseServerConfig()
            .name("Apache")
            .description("Apache");
    }

    public CompositeServerConfig count(int count) {
        return new CompositeServerConfig().server(this).count(count);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CreateServerConfig id(String id) {
        setId(id);
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CreateServerConfig description(String description) {
        setDescription(description);
        return this;
    }

    public CreateServerConfig name(String name) {
        setName(name);
        return this;
    }

    public ServerType getType() {
        return type;
    }

    public void setType(ServerType type) {
        this.type = type;
    }

    public CreateServerConfig type(ServerType type) {
        setType(type);
        return this;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public CreateServerConfig group(Group group) {
        setGroup(group);
        return this;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public CreateServerConfig template(Template template) {
        setTemplate(template);
        return this;
    }

    public Machine getMachine() {
        return machine;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public CreateServerConfig machine(Machine machine) {
        setMachine(machine);
        return this;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public CreateServerConfig password(String password) {
        setPassword(password);
        return this;
    }

    public NetworkConfig getNetwork() {
        return network;
    }

    public void setNetwork(NetworkConfig network) {
        this.network = network;
    }

    public CreateServerConfig network(NetworkConfig network) {
        setNetwork(network);
        return this;
    }

    public TimeToLive getTimeToLive() {
        return timeToLive;
    }

    public void setTimeToLive(TimeToLive timeToLive) {
        this.timeToLive = timeToLive;
    }

    public CreateServerConfig timeToLive(TimeToLive timeToLive) {
        setTimeToLive(timeToLive);
        return this;
    }

    public CreateServerConfig timeToLive(ZonedDateTime expirationTime) {
        setTimeToLive(new TimeToLive(expirationTime));
        return this;
    }

    public boolean isManagedOS() {
        return managedOS;
    }

    public void setManagedOS(boolean managedOS) {
        this.managedOS = managedOS;
    }

    public CreateServerConfig managedOs() {
        setManagedOS(true);
        return this;
    }

    @Override
    public CreateServerConfig[] getServerConfig() {
        return new CreateServerConfig[]{this};
    }
}

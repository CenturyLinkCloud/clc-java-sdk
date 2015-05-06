package com.centurylink.cloud.sdk.servers.services.domain.server;

import com.centurylink.cloud.sdk.servers.services.domain.group.ISubItemConfig;
import com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group;
import com.centurylink.cloud.sdk.servers.services.domain.template.refs.Template;

import java.time.ZonedDateTime;

import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.DE_FRANKFURT;
import static com.centurylink.cloud.sdk.common.management.services.domain.datacenters.refs.DataCenter.US_CENTRAL_SALT_LAKE_CITY;
import static com.centurylink.cloud.sdk.servers.services.domain.group.refs.Group.DEFAULT_GROUP;
import static com.centurylink.cloud.sdk.servers.services.domain.server.ServerType.STANDARD;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.CpuArchitecture.x86_64;
import static com.centurylink.cloud.sdk.servers.services.domain.template.filters.os.OsType.CENTOS;


/**
 * @author ilya.drabenia
 */
public class CreateServerConfig implements ISubItemConfig {
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
    private int count = 1;

    //TODO temporary config
    private static CreateServerConfig loadTestConfig() {
        return new CreateServerConfig()
            .name("ALTR")
            .type(STANDARD)

            .group(Group.refByName()
                    .name(DEFAULT_GROUP)
                    .dataCenter(DE_FRANKFURT)
            )

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
        return loadTestConfig();
    }

    public static CreateServerConfig nginxServer() {
        return loadTestConfig();
    }

    public static CreateServerConfig apacheHttpServer() {
        return loadTestConfig();
    }

    public int getCount() {
        return count;
    }

    public CreateServerConfig count(int count) {
        assert count > 0;
        this.count = count;
        return this;
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
}

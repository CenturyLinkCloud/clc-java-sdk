
Installation
------------

The recommended way to get started using spring-framework in your project
is with a dependency management system – the snippet below can be copied and pasted into your build.
Need help ? See our getting started guides on building with Maven and Gradle. <br /> <br />

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <version>4.1.6.RELEASE</version>
        </dependency>
    </dependencies>

Spring Framework includes a number of different modules.
Here we are showing spring-context which provides core functionality.
Refer to the getting started guides on the right for other options.
Once you've set up your build with the spring-context dependency, you'll be able `to do the following` :


Provide Credentials
--------------------

First that you should do is provide your credentials. There are various ways to do this :

  * use system variables
  * use environment variables
  * use properties files

`DefaultCredentialsProvider` loads credentials in the order listed above.

<i>code example</i>

``` java

new DefaultCredentialsProvider();
new DefaultCredentialsProvider("centurylink-clc-sdk-uat.properties");
new DefaultCredentialsProvider("john.doe", "hey@WMe8u");

```

Once you've provide your credentials you can create a server

Create Server
-------------
`ServerService.createServer(CreateServerConfig command)` creates a new server with necessary params <br />
which should be specified in the `CreateServerConfig` such as name, group, os, disks settings etc.

<i>code example</i>

``` java

serverService.create(new CreateServerConfig()
    .name("ex-srv")
    .type(STANDARD)
    .password(PASSWORD)
    .group(Group.refByName()
        .dataCenter(DataCenter.refById(DE_FRANKFURT.getId()))
        .name(DEFAULT_GROUP)
    )
    .machine(new Machine()
        .cpuCount(1)
        .ram(2)
    )
    .template(Template.refByOs()
        .dataCenter(DE_FRANKFURT)
        .type(CENTOS)
        .version("6")
        .architecture(x86_64)
    )
)

```

Search Servers
--------------

Than you can get the server data using `ServerService.find(ServerFilter filter)` note that `ServerFilter` allows us to
find severs by id, name, datacenter, group etc. 

<i>code example</i>

``` java

serverService.find(new ServerFilter().id("ex-srv189"));
serverService.find(new ServerFilter().nameContains("ex"));
serverService.find(new ServerFilter().groups(group));

```

Power Operations
----------------

Also you have an ability to do different operations on server :

  * Power on a server (`ServerService.powerOn`)
  * Power off a server (`ServerService.powerOff`)
  * Archive a server (`ServerService.archive`)
  * Create snapshot of a server (`ServerService.createSnapshot`)
  * Restart of a server (`ServerService.restart`)
  * Pause a server (`ServerService.pause`)
  * etc.

<i>code example</i>

``` java

serverService.powerOn(server);
serverService.archive(server);
serverService.restart(server);

```
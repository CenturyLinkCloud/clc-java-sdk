
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
Once you've provide your credentials you can create a server

Create Server
-------------
`ServerService.createServer(CreateServerConfig command)` creates a new server with necessary params <br />
which should be specified in the `CreateServerConfig` such as name, group, os, disks settings etc.


Search Servers
--------------

Than you can get the server data using `ServerService.find(ServerFilter filter)` note that `ServerFilter` allows us to
find severs by id, name, datacenter, group etc. 


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


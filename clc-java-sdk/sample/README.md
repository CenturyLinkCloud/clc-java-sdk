App description
---------------
The application consists of the following two parts:

1. `Web application`
2. `Scripts`

The Web application is a simple Web UI that provides major SDK functions. `web-app` includes the `app` and `client` parts:

* `app` provides the functionality to run SpringBootApplication.
* `client` is the UI part of `web-app` that contains gulp scripts, as well as HTML and CSS files.

Scripts are tests written using the TestNG Java framework. Their main purpose is to demonstrate how the SDK functionality works.

Build process
-----------------

To build sources, you first need to install Gradle 2.2.1 or later.

<b>The Web application (sample/web-app)</b><br />

This is a simple Web application for the SDK. To build and run `sample/web-app`, execute the following Gradle tasks in the command line:
```
$ gradle clientBuild
$ gradle run
```
By default, the Web application is running on the 8080 port. If you want to change it to, e.g., 9000, add the following lines to `application.yaml`:
```
server:
    port: 9000
```
Other SpringBootApplication configuration parameters can be added into this file, as well.

<b>Scripts (sample/scripts)</b><br />

This section contains a couple of test scripts that demonstrate how the SDK works. To run them, execute the following Gradle task:
```
gradle clean runSamples -Dclc.username=your_ysername -Dclc.password=your_password
```
Here, `your_username` and `your_password` are your CenturyLink credentials.

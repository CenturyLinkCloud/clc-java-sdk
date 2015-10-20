Installation
------------

If you use Maven or Gradle, you can simply add the CLC SDK as a dependency.

Maven: 
```xml
<dependencies>
    <dependency>
        <groupId>com.centurylink.cloud</groupId>
        <artifactId>clc-sdk-spring-adapter</artifactId>
        <version>1.1.0</version>
    </dependency>
</dependencies>
```

Gradle:
```groovy
compile 'com.centurylink.cloud:clc-sdk-spring-adapter:1.1.0'
```


Spring Adapter
---------------
To use spring adapter you need:

1. `Import sdk-spring-adapter.jar in your project`
2. `Provide Spring configuration using @EnableClcSdk annotation`

```java
@Configuration
@EnableClcSdk
class MyConfig {

    @Bean
    public CredentialsProvider clcCredentialsProvider() {
        return new StaticCredentialsProvider("john.doe", "strong_password");
    }

    @Bean
    public SdkConfiguration clcSdkConfig() {
        return new SdkConfigurationBuilder().build();
    }

}

```

Then you can autowire necessary sdk services

```java
@Autowired
ServerService serverService;

@Autowired
GroupService groupService;
```

Or get them from spring context directly

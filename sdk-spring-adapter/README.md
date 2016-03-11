Add Spring CLC SDK dependency
------------

Maven: 
```xml
<dependencies>
    <dependency>
        <groupId>com.centurylink.cloud</groupId>
        <artifactId>spring-clc-sdk</artifactId>
        <version>1.2.3</version>
    </dependency>
</dependencies>
```

Gradle:
```groovy
compile 'com.centurylink.cloud:spring-clc-sdk:1.2.3'
```


Provide credentials and config
---------------

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
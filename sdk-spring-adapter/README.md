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

package com.centurylink.sdk2.dsl.generator.java;

import com.centurylink.sdk2.dsl.language.endpoint.Endpoint;
import com.centurylink.sdk2.api.server.ServerService;
import com.centurylink.sdk2.api.server.create.CreateServerCommand;
import com.centurylink.sdk2.api.server.create.CreateServerResult;
import com.centurylink.sdk2.dsl.language.endpoint.EndpointBuilder;
import org.reflections.Reflections;
import org.simart.writeonce.utils.FileUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * @author ilya.drabenia
 */
public class Generator {

    public static void main(String... args) throws Exception {
        generateRequest();
        generateResponse();
        generateEndpoint();
    }

    private static void generateRequest() throws Exception {
        final String template = FileUtils.read("src\\main\\resources\\sdk2\\structure.java.jsp");

        final org.simart.writeonce.application.Generator generator =
                org.simart.writeonce.application.Generator.create(template);

        System.out.println(
            generator
                .bindValue("Types", new Types())
                .bindValue("structure", new CreateServerCommand().specification())
                .generate()
        );
    }

    private static void generateResponse() throws Exception {
        final String template = FileUtils.read("src\\main\\resources\\sdk2\\structure.java.jsp");

        final org.simart.writeonce.application.Generator generator =
                org.simart.writeonce.application.Generator.create(template);

        System.out.println(
            generator
                .bindValue("Types", new Types())
                .bindValue("structure", new CreateServerResult().specification())
                .generate()
        );
    }

    private static void generateEndpoint() throws Exception {
        new ServerService().create();
        final String template = FileUtils.read("src\\main\\resources\\sdk2\\service.java.jsp");

        final org.simart.writeonce.application.Generator generator =
                org.simart.writeonce.application.Generator.create(template);

        final Reflections reflections = new Reflections("com.centurylink.sdk2.api.server");
        final Set<Method> datas = reflections.getMethodsAnnotatedWith(Endpoint.class);

        List<EndpointBuilder> endpoints = new ArrayList<>();

        for (Method curMethod : datas) {
            endpoints.add((EndpointBuilder) curMethod.invoke(new ServerService()));
        }

        System.out.println(
            generator
                .bindValue("endpoints", Arrays.asList(new ServerService().create()))
                .generate()
        );
    }


}

package com.centurylink.sdk;


import org.reflections.Reflections;
import org.simart.writeonce.application.Generator;
import org.simart.writeonce.utils.FileUtils;

import java.util.Set;

public class ApiGen {

    public static void main(String... args) throws Exception {
// find interesting classes - it's easy with org.reflections.Reflections
        final Reflections reflections = new Reflections("com.centurylink.sdk");
        final Set<Class<?>> datas = reflections.getTypesAnnotatedWith(Payload.class);

// load template - Builder.java - this is not a java class - it's groovy template
        final String template = FileUtils.read("src\\main\\resources\\payload.java.jsp");

// create generator
        final Generator generator = Generator.create(template);

// define builder (interpreter)
        generator.bindBuilder("cls", Class.class);

// go go go
        for (Class<?> data : datas) {
            // bind value and generate
            final String sourceCode = generator.bindValue("cls", data).generate();
            // you can generate file name, too
            final String fileName = generator.generate("${cls.package.path}${cls.shortName}Builder.java");
            // save generated file
//            final String filePath = "src\\main\\resources\\" + fileName;
//            FileUtils.write(filePath, sourceCode);

            System.out.println(sourceCode);
        }

        AccountPayload
            .builder()
                .AccountAlias("asdf")
                .City("asas")
                .Fax("123432134")
                .Location("asdfasdf")
            .build();
    }

}

package com.centurylink.sdk2.dsl.generator.java;

import com.centurylink.sdk2.api.server.create.CreateServerCommand;
import org.simart.writeonce.utils.FileUtils;

/**
 * @author ilya.drabenia
 */
public class Generator {

    public static void main(String... args) throws Exception {
        final String template = FileUtils.read("src\\main\\resources\\sdk2\\structure.java.jsp");

        final org.simart.writeonce.application.Generator generator =
                org.simart.writeonce.application.Generator.create(template);

        System.out.println(
            generator
                .bindValue("Types", new Types())
                .bindValue("structure", new CreateServerCommand().specification())
                .generate()
        );

//
//        for (Class<?> data : datas) {
//            final String sourceCode = generator.bindValue("cls", data).generate();
//            final String fileName = generator.generate("${cls.package.path}${cls.shortName}Builder.java");
//
//            // save generated file
////            final String filePath = "src\\main\\resources\\" + fileName;
////            FileUtils.write(filePath, sourceCode);
//
//            System.out.println(sourceCode);
//        }
    }


}

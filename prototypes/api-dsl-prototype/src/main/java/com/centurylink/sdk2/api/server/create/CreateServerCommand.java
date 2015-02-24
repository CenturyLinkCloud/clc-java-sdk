package com.centurylink.sdk2.api.server.create;

import com.centurylink.sdk2.dsl.language.structure.FieldBuilder;
import com.centurylink.sdk2.dsl.language.structure.StructureBuilder;
import com.centurylink.sdk2.dsl.language.structure.StructureSpecification;

import static com.centurylink.sdk2.dsl.language.PrimitiveType.INT;
import static com.centurylink.sdk2.dsl.language.PrimitiveType.STRING;
import static com.centurylink.sdk2.dsl.language.structure.StructureBuilder.structure;

/**
 * @author ilya.drabenia
 */
public class CreateServerCommand implements StructureSpecification {

    @Override
    public StructureBuilder specification() {
        return structure()
            .name("CreateServerCommand")

            .field(new FieldBuilder()
                .name("name")
                .desc(
                    "Name of the server to create. Alphanumeric characters and dashes only. Must be between 1-7 " +
                    "characters depending on the length of the account alias. (This name will be appended with a " +
                    "two digit number and prepended with the datacenter code and account alias to make up the " +
                    "final server name.)"
                )
                .type(STRING).required()
            )

            .field(new FieldBuilder()
                .name("groupId")
                .desc(
                    "ID of the parent group. Retrieved from query to parent group, or by looking at the URL on " +
                    "the UI pages in the Control Portal."
                )
                .type(STRING)
                .required()
            )

            .field(new FieldBuilder()
                .name("sourceServerId")
                .desc(
                    "ID of the server to use a source. May be the ID of a template, or when cloning, an existing " +
                    "server ID. The list of available templates for a given account in a data center can " +
                    "be retrieved from the Get Data Center Deployment Capabilities API operation."
                )
                .type(STRING)
                .required()
            )

            .field(new FieldBuilder()
                .name("cpu")
                .desc("Number of processors to configure the server with (1-16)")
                .type(INT)
                .required()
            )

            .field(new FieldBuilder()
                .name("memoryGB")
                .desc("Number of GB of memory to configure the server with (1-128)")
                .type(INT)
                .required()
            )

            .field(new FieldBuilder()
                .name("type")
                .desc("Whether to create standard or hyperscale server")
                .type(INT)
                .required()
            );
    }

}

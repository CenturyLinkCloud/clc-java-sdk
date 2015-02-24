package com.centurylink.sdk2.api.server.create;

import com.centurylink.sdk2.dsl.language.PrimitiveType;
import com.centurylink.sdk2.dsl.language.ResponseDescription;
import com.centurylink.sdk2.dsl.language.structure.FieldBuilder;
import com.centurylink.sdk2.dsl.language.structure.StructureBuilder;
import com.centurylink.sdk2.dsl.language.structure.StructureSpecification;

import static com.centurylink.sdk2.dsl.language.structure.StructureBuilder.structure;

/**
 * @author ilya.drabenia
 */
public class CreateServerResult  implements StructureSpecification {

    @Override
    public StructureBuilder specification() {
        return structure()
            .name("CreateServerResult")

            .field(new FieldBuilder()
                .name("result")
                .type(PrimitiveType.STRING)
                .desc("Operation Result")
                .required()
            );
    }

}

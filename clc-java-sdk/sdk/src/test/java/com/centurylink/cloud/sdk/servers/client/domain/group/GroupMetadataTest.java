package com.centurylink.cloud.sdk.servers.client.domain.group;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.services.function.Streams.map;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.testng.Assert.*;

public class GroupMetadataTest {

    @Test
    public void testGetAllGroups_DeepHierarchySuccess() {
        GroupMetadata metadata = new GroupMetadata() {{
            setId("root");
            setGroups(asList(
                new GroupMetadata() {{
                    setId("1");
                }},
                new GroupMetadata() {{
                    setId("2");
                    setGroups(asList(
                        new GroupMetadata() {{
                            setId("2.1");
                        }}
                    ));
                }},
                new GroupMetadata() {{
                    setId("3");
                }}
            ));
        }};

        List<GroupMetadata> groups = metadata.getAllGroups();

        assert groups.size() == 5;
        assertEquals(
            newHashSet(map(groups, GroupMetadata::getId)),
            newHashSet("1", "2", "2.1", "3", "root")
        );
    }

}
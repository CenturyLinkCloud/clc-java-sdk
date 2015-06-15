/*
 * (c) 2015 CenturyLink. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.centurylink.cloud.sdk.servers.client.domain.group;

import org.testng.annotations.Test;

import java.util.List;

import static com.centurylink.cloud.sdk.core.function.Streams.map;
import static com.google.common.collect.Sets.newHashSet;
import static java.util.Arrays.asList;
import static org.testng.Assert.assertEquals;

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
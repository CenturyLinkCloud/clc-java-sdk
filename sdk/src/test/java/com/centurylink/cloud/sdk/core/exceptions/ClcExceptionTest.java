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

package com.centurylink.cloud.sdk.core.exceptions;

import org.testng.annotations.Test;

public class ClcExceptionTest {

    @Test
    public void testConstructionWithFormatting() {
        Exception e = new ClcException("%s formatting works", "param");

        assert "param formatting works".equals(e.getMessage());
    }

    @Test
    public void testConstructorWithFormattingAndSuppressedException() {
        Exception e = new ClcException("%s formatting works", "parameters", new Exception("Cause"));

        assert "parameters formatting works".equals(e.getMessage());
        assert "Cause".equals(e.getCause().getMessage());
    }

}
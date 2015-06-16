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

package com.centurylink.cloud.sdk.sample.port.adapter.web;

import com.centurylink.cloud.sdk.core.client.ClcClientException;
import com.centurylink.cloud.sdk.core.client.errors.ClcHttpClientException;
import com.centurylink.cloud.sdk.core.exceptions.ClcException;
import com.centurylink.cloud.sdk.sample.domain.SdkCredentials;
import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.LoginForm;
import com.centurylink.cloud.sdk.sample.port.adapter.web.beans.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * @author Ilya Drabenia
 */
@RestController
@RequestMapping("/account")
public class LoginController {

    @Autowired
    SdkRegistry sdkRegistry;

    @RequestMapping(value = "/login", method = POST)
    public StatusResponse login(@RequestBody LoginForm loginForm) {
        sdkRegistry.setCredentials(new SdkCredentials(loginForm.getUsername(), loginForm.getPassword()));

        //check credentials
        try {
            sdkRegistry.getSdk().dataCenterService().findAll();
        } catch (ClcException | ClcHttpClientException e) {
            throw new ClcClientException(e);
        }

        return new StatusResponse("OK");
    }

    @RequestMapping(value = "/logout", method = POST)
    public StatusResponse logout(HttpSession session) {
        session.invalidate();

        return new StatusResponse("OK");
    }

}

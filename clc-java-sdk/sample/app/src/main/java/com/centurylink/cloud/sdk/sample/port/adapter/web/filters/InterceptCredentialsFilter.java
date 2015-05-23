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

package com.centurylink.cloud.sdk.sample.port.adapter.web.filters;

import com.centurylink.cloud.sdk.sample.domain.SdkCredentials;
import com.centurylink.cloud.sdk.sample.domain.SdkRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Ilya Drabenia
 */
@Component
public class InterceptCredentialsFilter implements Filter {

    @Autowired
    private SdkRegistry sdkRegistry;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {
        doFilter((HttpServletRequest) request, (HttpServletResponse) response, chain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response,
                          FilterChain chain) throws IOException, ServletException {
        String username = request.getHeader("X-Clc-Username");
        String password = request.getHeader("X-Clc-Password");

        if (username != null && password != null) {
            sdkRegistry.setCredentials(new SdkCredentials(
                username, password
            ));
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}

/*
 * spring-mvc-logger logs requests/responses
 *
 * Copyright (c) 2013. Israel Zalmanov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.weitaomi.systemconfig.filter;

import com.weitaomi.systemconfig.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.concurrent.atomic.AtomicLong;

public class LoggingFilter extends OncePerRequestFilter {

    protected static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    private static final String REQUEST_PREFIX = "Request: ";
    private static final String RESPONSE_PREFIX = "Response: ";
    private AtomicLong id = new AtomicLong(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, final FilterChain filterChain) throws ServletException, IOException {
        if(logger.isInfoEnabled()){
            long requestId = id.incrementAndGet();
            request = new RequestWrapper(requestId, request);
            //response = new ResponseWrapper(requestId, response);
        }
        try {
            filterChain.doFilter(request, response);
        }
        finally {
            if(logger.isInfoEnabled()){
                logRequest(request);
            }
        }

    }

    private void logRequest(final HttpServletRequest request) {
        StringBuilder msg = new StringBuilder();
        if(request instanceof RequestWrapper){
            msg.append("request_id:").append(((RequestWrapper)request).getId()).append(";");
        }

        msg.append("user_id:").append(request.getHeader("userId")).append(";");
        msg.append("from:").append(request.getHeader("from")).append(";");

        if(request.getContentType() != null && request.getContentType().contains("application/json")){
            RequestWrapper requestWrapper = (RequestWrapper) request;
            try {
                String charEncoding = requestWrapper.getCharacterEncoding() != null ? requestWrapper.getCharacterEncoding() :
                        "UTF-8";
                msg.append("params:").append(new String(requestWrapper.toByteArray(), charEncoding)).append(";");
            } catch (UnsupportedEncodingException e) {
                logger.warn("Failed to parse request payload", e);
            }
        }else{
            msg.append("params:").append(getPostParamsStr(request));
        }




//        logger.info(msg.toString());
    }

    /*private boolean isMultipart(final HttpServletRequest request) {
        return request.getContentType()!=null && request.getContentType().startsWith("multipart/form-data");
    }

    private void logResponse(final ResponseWrapper response) {
        StringBuilder msg = new StringBuilder();
        msg.append(RESPONSE_PREFIX);
        msg.append("request_id=").append((response.getId()));
        try {
            msg.append("; payload:").append(new String(response.toByteArray(), response.getCharacterEncoding()));
        } catch (UnsupportedEncodingException e) {
            logger.warn("Failed to parse response payload", e);
        }
        logger.info(msg.toString());
    }*/

    private String getPostParamsStr(HttpServletRequest request){
        StringBuffer sb = new StringBuffer();
        sb.append("{");

        Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()){
            String name = parameterNames.nextElement();
            sb.append("\"").append(name).append("\"").append(":").append(StringUtil.stringArrayToStringWithQuot(request.getParameterValues(name))).append(",");
        }
        if(sb.length() > 1){
            sb.deleteCharAt(sb.length() - 1);
        }
        sb.append("}");

        return sb.toString();
    }

}

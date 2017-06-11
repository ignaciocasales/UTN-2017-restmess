package com.utn.restmess.config;

import com.utn.restmess.config.util.AuthenticationData;
import com.utn.restmess.config.util.SessionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

@SuppressWarnings("unused")
@Service
public class AuthFilter extends OncePerRequestFilter {

    @Autowired
    private SessionData sessionData;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String sessionId = request.getHeader("sessionId");

        AuthenticationData data = sessionData.getSession(sessionId);

        if (null != data) {
            HeaderMapRequestWrapper requestWrapper = new HeaderMapRequestWrapper(request);
            requestWrapper.addHeader(data.getUser().getUsername());
            filterChain.doFilter(requestWrapper, response);
        } else {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
        }
    }

    public class HeaderMapRequestWrapper extends HttpServletRequestWrapper {
        private Map<String, String> headerMap = new HashMap<>();

        /**
         * construct a wrapper for this request
         *
         * @param request HttpServletRequest
         */
        HeaderMapRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        /**
         * add a header with given name and value
         *
         * @param value String
         */
        void addHeader(String value) {
            headerMap.put("user", value);
        }

        @Override
        public String getHeader(String name) {
            String headerValue = super.getHeader(name);

            if (headerMap.containsKey(name)) {
                headerValue = headerMap.get(name);
            }
            return headerValue;
        }

        /**
         * get the Header names
         */
        @Override
        public Enumeration<String> getHeaderNames() {
            List<String> names = Collections.list(super.getHeaderNames());

            names.addAll(headerMap.keySet());

            return Collections.enumeration(names);
        }

        @Override
        public Enumeration<String> getHeaders(String name) {
            List<String> values = Collections.list(super.getHeaders(name));

            if (headerMap.containsKey(name)) {
                values.add(headerMap.get(name));
            }
            return Collections.enumeration(values);
        }

    }
}
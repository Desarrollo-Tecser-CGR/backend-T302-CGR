package com.cgr.base.infrastructure.security.interceptor;

import com.cgr.base.domain.exception.customException.MessageException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class HeadersInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String contentType = request.getHeader("Content-Type");
        String acceptHeader = request.getHeader("Accept");
        String hostHeader = request.getHeader("Host");
        String userAgent = request.getHeader("User-Agent");

        // Validar Content-Type si no es GET
        if (!request.getMethod().equalsIgnoreCase("GET") &&
                (contentType == null || !contentType.equalsIgnoreCase(MediaType.APPLICATION_JSON_VALUE))) {
            throw new MessageException("Error 400: Invalid Content-Type, expected application/json");
        }

        // Validar Host para TODAS las peticiones
        if (hostHeader == null || hostHeader.isEmpty()) {
            throw new MessageException("Error 400: Host header is required");
        }

        // Validar Accept
        if (acceptHeader == null || acceptHeader.isEmpty()) {
            throw new MessageException("Error 406: Invalid Accept header, expected application/json or text/html");
        }

        // Validar User-Agent
        if (userAgent == null || userAgent.isEmpty()) {
            throw new MessageException("Error 400: User-Agent header is required");
        }

        return true;
    }
}
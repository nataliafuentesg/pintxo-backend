package com.tasca.tasca_backend.configurations;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AdminKeyInterceptor implements HandlerInterceptor {

    private final String adminKey;

    public AdminKeyInterceptor(@Value("${tasca.admin.key:}") String adminKey) {
        this.adminKey = adminKey == null ? "" : adminKey.trim();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (adminKey.isEmpty()) return true; // sin llave => no protegemos (solo dev)
        String provided = request.getHeader("X-Admin-Key");
        if (adminKey.equals(provided)) return true;

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setHeader("WWW-Authenticate", "AdminKey realm=\"Tasca\"");
        return false;
    }
}
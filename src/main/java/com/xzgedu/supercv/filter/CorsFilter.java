package com.xzgedu.supercv.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 跨域设置，允许跨域访问的URLs:
 * https://www.supercv.cn，http://www.supercv.cn，http://127.0.0.1:*，http://localhost:*
 */
@Slf4j
@Component
public class CorsFilter implements Filter {

    private static final List<String> ALLOWED_ORIGINS = new ArrayList<>();

    @Value("${spring.profiles.active}")
    private String active;

    static {
        ALLOWED_ORIGINS.add("https://www.supercv.cn");
        ALLOWED_ORIGINS.add("http://www.supercv.cn");
        ALLOWED_ORIGINS.add("http://localhost");
        ALLOWED_ORIGINS.add("http://127.0.0.1");
    }

    private boolean checkIfAllowedOrigin(String origin) {
        if (this.active.equals("dev") || this.active.equals("test") || this.active.equals("ut")) {
            return true;
        }

        if (origin == null) return false;
        for (String allowedOrigin : ALLOWED_ORIGINS) {
            if (origin.startsWith(allowedOrigin)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletResponse res = (HttpServletResponse) response;
        HttpServletRequest req = (HttpServletRequest) request;

        // Check the origin of the request
        String origin = req.getHeader("Origin");
        boolean allowedOrigin = checkIfAllowedOrigin(origin);

        if (allowedOrigin) {
            // Set CORS headers
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Credentials", "true");
            res.setHeader("Access-Control-Allow-Methods", "POST, GET, PATCH, DELETE, PUT, OPTIONS");
            res.setHeader("Access-Control-Max-Age", "86400");
            res.setHeader("Access-Control-Allow-Headers", "Content-Type, Authorization, X-Requested-With");

            // Handle preflight requests
            if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
                res.setStatus(HttpServletResponse.SC_OK);
            } else {
                chain.doFilter(request, response);
            }
        } else {
            // Reject request if origin is not allowed
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}

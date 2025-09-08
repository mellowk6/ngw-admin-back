package com.nhbank.ngw.common.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;

public class InactivityTimeoutFilter extends OncePerRequestFilter {
    private static final String ATTR_LAST_SEEN = "LAST_ACTIVITY_AT";
    private final long maxIdleMillis;

    public InactivityTimeoutFilter(Duration maxIdle) {
        this.maxIdleMillis = maxIdle.toMillis();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException {

        String path = req.getRequestURI();
        // 인증/헬스/콘솔 등은 제외
        if (path.startsWith("/api/auth/") || path.startsWith("/actuator/") || path.startsWith("/h2-console")) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = req.getSession(false);
        long now = System.currentTimeMillis();

        if (session != null) {
            Long last = (Long) session.getAttribute(ATTR_LAST_SEEN);
            if (last != null && now - last > maxIdleMillis) {
                session.invalidate();
                res.setCharacterEncoding("UTF-8");
                res.setContentType("application/json;charset=UTF-8");
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.getWriter().write("{\"message\":\"Your session has expired. Please log in again.\"}");
                return;
            }
            session.setAttribute(ATTR_LAST_SEEN, now);
        }

        chain.doFilter(req, res);
    }
}
package com.libsystem.libraryservice.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitingFilter implements Filter {

    private static final int MAX_REQUESTS_PER_TEN_SECONDS = 5;
    private final Map<String, AtomicInteger> requestCounts = new ConcurrentHashMap<>();

    private long lastResetTime = System.currentTimeMillis();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        long currentTime = System.currentTimeMillis();

        // Check if 10 seconds have passed since the last reset
        if (currentTime - lastResetTime > 10000) {
            requestCounts.clear();
            lastResetTime = currentTime;
        }

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String clientIp = httpRequest.getRemoteAddr();
        requestCounts.putIfAbsent(clientIp, new AtomicInteger(0));
        int currentCount = requestCounts.get(clientIp).incrementAndGet();


        if (currentCount >= MAX_REQUESTS_PER_TEN_SECONDS) {
            httpResponse.setStatus(429);
            httpResponse.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        filterChain.doFilter(httpRequest, httpResponse);

    }


}

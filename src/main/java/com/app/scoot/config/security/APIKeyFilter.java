package com.app.scoot.config.security;

import com.app.scoot.repository.APIKeyRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

import static com.app.scoot.consts.ApiConsts.PLATFORM_SUB_HEADER;

@Component
@AllArgsConstructor
public class APIKeyFilter extends OncePerRequestFilter {

    private APIKeyRepository apiKeyRepository;
    private static final String DISTANCE_URL_PATTERN = "/api/distance";

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {

        if (request.getRequestURI().startsWith(DISTANCE_URL_PATTERN)) {
            String apiKey = Optional.ofNullable(request.getHeader(PLATFORM_SUB_HEADER))
                    .orElseThrow(() -> new ServletException("Missing API key"));

            apiKeyRepository.findByApiKey(apiKey)
                    .orElseThrow(() -> new ServletException("Invalid API key"));
        }

        filterChain.doFilter(request, response);
    }
}
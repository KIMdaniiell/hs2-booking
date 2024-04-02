package com.example.hs2booking.config.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

@Component
public class CustomHeaderAdderFilter extends OncePerRequestFilter {
    public static final String ROLE_HEADER_NAME = "ROLE_NAME";

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Получаем заголовок
        var roleNames = request.getHeader(ROLE_HEADER_NAME);

        if (null == roleNames) {
            // Если запрос не содержит необходимый заголовок для авторизации по роли, то пускаем дальше по фильтрам
            filterChain.doFilter(request, response);
            return;
        }
        System.out.println("[Фильтр] ROLE_NAME Header = " + roleNames);

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String[] roleNameArray = roleNames.split(",");

            Collection<GrantedAuthority> roles = new HashSet<>();
            Arrays.stream(roleNameArray)
                    .map(roleName -> (GrantedAuthority) roleName::strip).forEach(roles::add);


            UsernamePasswordAuthenticationToken authentication
                    = new UsernamePasswordAuthenticationToken(
                    "user-name-demo-hardcoded",
                    null,
                    roles
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }
}

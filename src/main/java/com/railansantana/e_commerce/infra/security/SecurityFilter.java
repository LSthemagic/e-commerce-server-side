package com.railansantana.e_commerce.infra.security;

import com.railansantana.e_commerce.domain.user.User;
import com.railansantana.e_commerce.infra.security.execeptions.SecurityException;
import com.railansantana.e_commerce.repository.user.UserRepository;
import com.railansantana.e_commerce.resource.exceptions.StandardError;
import com.railansantana.e_commerce.services.user.DataUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Collections;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final DataUserService userService;

    public SecurityFilter(UserRepository userRepository, TokenService tokenService, DataUserService userService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        var token = this.recoverToken(request);
        var login = tokenService.validToken(token);

        System.err.println("DATA FROM SECURITY FILTER ");
        System.err.println("Token " + token);
        System.err.println("login " + login);
        System.err.println("-------------------------------------");

        User user = userService.findByEmail(login);
        var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
        var authentication = new UsernamePasswordAuthenticationToken(user, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String recoverToken(HttpServletRequest request) {
        var authHeader = request.getHeader("Authorization");
        if (authHeader == null) return null;
        return authHeader.replace("Bearer ", "");
    }
}

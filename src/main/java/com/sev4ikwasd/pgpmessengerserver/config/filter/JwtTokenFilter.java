package com.sev4ikwasd.pgpmessengerserver.config.filter;

import com.sev4ikwasd.pgpmessengerserver.config.service.JwtService;
import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private UserDAO userDAO;

    private JwtService jwtService;

    private String header = "Authorization";

    @Autowired
    public JwtTokenFilter(UserDAO userDAO, JwtService jwtService) {
        this.userDAO = userDAO;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        getTokenStringFromHeader(request.getHeader(header)).ifPresent(token -> authorize(request, token));
        getTokenStringFromParameter(request.getParameter("token")).ifPresent(token -> authorize(request, token));

        filterChain.doFilter(request, response);
    }

    private void authorize(HttpServletRequest request, String token) {
        jwtService.getSubFromToken(token).ifPresent(id -> {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                userDAO.findById(id).ifPresent(user -> {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user,
                            null,
                            Collections.emptyList()
                    );
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                });
            }
        });
    }

    private Optional<String> getTokenStringFromHeader(String header) {
        if (header == null) {
            return Optional.empty();
        }
        else {
            String[] split = header.split(" ");
            if (split.length < 2) {
                return Optional.empty();
            } else {
                return Optional.ofNullable(split[1]);
            }
        }
    }

    private Optional<String> getTokenStringFromParameter(String parameter) {
        if (parameter == null) {
            return Optional.empty();
        }
        else {
            return Optional.of(parameter);
        }
    }
}
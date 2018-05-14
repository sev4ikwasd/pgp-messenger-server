package com.sev4ikwasd.pgpmessengerserver.config.filter;

import com.sev4ikwasd.pgpmessengerserver.config.service.JwtService;
import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.Optional;

public class AuthChannelInterceptorAdapter extends ChannelInterceptorAdapter {
    private String header = "Authorization";
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private JwtService jwtService;

    public AuthChannelInterceptorAdapter(final JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Message<?> preSend(final Message<?> message, final MessageChannel channel) throws AuthenticationException {
        final StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT == accessor.getCommand()) {
            getTokenStringFromHeader(accessor.getFirstNativeHeader(header)).ifPresent(token -> {
                jwtService.getSubFromToken(token).ifPresent(id -> {
                    if (SecurityContextHolder.getContext().getAuthentication() == null) {
                        userDAO.findById(id).ifPresent(user -> {
                            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                    user,
                                    null,
                                    Collections.emptyList()
                            );
                            //authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            accessor.setUser(authenticationToken);
                        });
                    }
                });
            });
        }
        return message;
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
}
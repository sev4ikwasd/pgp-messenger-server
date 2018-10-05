package com.sev4ikwasd.pgpmessengerserver.config.service.user;

import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import static java.util.Collections.emptyList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private DatabaseUserService userService;

    @Autowired
    public UserDetailsServiceImpl(DatabaseUserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserApp userApp = userService.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
        return new User(userApp.getUsername(), userApp.getPassword(), emptyList());
    }
}
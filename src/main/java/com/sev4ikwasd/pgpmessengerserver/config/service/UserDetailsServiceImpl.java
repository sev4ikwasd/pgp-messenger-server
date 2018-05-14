package com.sev4ikwasd.pgpmessengerserver.config.service;

import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
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
    private UserDAO userDAO;

    @Autowired
    public UserDetailsServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserApp userApp = userDAO.findUserByEmail(email);
        if (userApp == null) {
            throw new UsernameNotFoundException(email);
        }
        return new User(userApp.getEmail(), userApp.getPassword(), emptyList());
    }
}
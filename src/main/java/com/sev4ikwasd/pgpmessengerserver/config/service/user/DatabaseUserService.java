package com.sev4ikwasd.pgpmessengerserver.config.service.user;

import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DatabaseUserService implements UserService {
    private UserDAO userDAO;

    @Autowired
    public DatabaseUserService(UserDAO userDAO){
        this.userDAO = userDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

    @Override
    public Optional<UserApp> findUserByUsername(String username) {
        return userDAO.findUserByUsername(username);
    }

    @Override
    public Boolean existsByUsername(String username) {
        return userDAO.existsByUsername(username);
    }

    @Override
    public Optional<UserApp> findUserByEmail(String email) {
        return userDAO.findUserByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }
}

package com.sev4ikwasd.pgpmessengerserver.config.service.user;

import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;

import java.util.Optional;

public interface UserService {
    Optional<UserApp> findUserByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<UserApp> findUserByEmail(String email);
    Boolean existsByEmail(String email);
}

package com.sev4ikwasd.pgpmessengerserver.database.dao;

import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface UserDAO extends JpaRepository<UserApp, String> {
    UserApp findUserByUsername(String username);
    Boolean existsByUsername(String username);
    UserApp findUserByEmail(String email);
    Boolean existsByEmail(String email);
}

package com.sev4ikwasd.pgpmessengerserver.database.dao;

import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
@Transactional
public interface UserDAO extends JpaRepository<UserApp, String> {
    Optional<UserApp> findUserByUsername(String username);
    Boolean existsByUsername(String username);
    Optional<UserApp> findUserByEmail(String email);
    Boolean existsByEmail(String email);
}

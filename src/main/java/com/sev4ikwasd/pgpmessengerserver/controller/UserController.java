package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.config.service.JwtService;
import com.sev4ikwasd.pgpmessengerserver.controller.model.RegisterParam;
import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import com.sev4ikwasd.pgpmessengerserver.controller.exception.InvalidRequestException;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import com.sev4ikwasd.pgpmessengerserver.controller.model.LoginParam;
import lombok.Data;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
public class UserController {
    private UserDAO userDAO;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private JwtService jwtService;
    private Logger logger;

    @Autowired
    public UserController(UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder, JwtService jwtService, Logger logger) {
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
        this.logger = logger;
    }

    @PostMapping("/users/register")
    public ResponseEntity register(@RequestBody @Valid RegisterParam registerParam, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        if (userDAO.existsByUsername(registerParam.getUsername())) {
            bindingResult.rejectValue("username", "DUPLICATED", "Username is duplicated");
        }
        if (userDAO.existsByEmail(registerParam.getEmail())) {
            bindingResult.rejectValue("email", "DUPLICATED", "Email is duplicated");
        }
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        UserApp user = new UserApp(registerParam.getUsername(), registerParam.getEmail(), registerParam.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/users/login")
    public ResponseEntity login(@RequestBody @Valid LoginParam loginParam, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new InvalidRequestException(bindingResult);
        }
        UserApp user = userDAO.findUserByEmail(loginParam.getEmail());
        if (user != null && bCryptPasswordEncoder.matches(loginParam.getPassword(), user.getPassword())){
            TokenString token = new TokenString();
            token.setToken(jwtService.toToken(user));
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else {
            bindingResult.rejectValue("password", "INVALID", "Email or password is invalid");
            throw new InvalidRequestException(bindingResult);
        }
    }

    @Data
    private class TokenString {
        private String token;
    }
}

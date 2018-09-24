package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.config.service.JwtService;
import com.sev4ikwasd.pgpmessengerserver.controller.exception.InvalidRequestException;
import com.sev4ikwasd.pgpmessengerserver.controller.model.LoginParam;
import com.sev4ikwasd.pgpmessengerserver.controller.model.RegisterParam;
import com.sev4ikwasd.pgpmessengerserver.controller.model.UserInfoParam;
import com.sev4ikwasd.pgpmessengerserver.controller.model.UserWithToken;
import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        UserApp user = null;
        if(userDAO.existsByEmail(loginParam.getLogin())){
            user = userDAO.findUserByEmail(loginParam.getLogin());
        }
        else if(userDAO.existsByUsername(loginParam.getLogin())){
            user = userDAO.findUserByUsername(loginParam.getLogin());
        }
        if (user != null && bCryptPasswordEncoder.matches(loginParam.getPassword(), user.getPassword())){
            UserWithToken token = new UserWithToken(jwtService.toToken(user), user.getEmail(), user.getUsername());
            //token.setToken(jwtService.toToken(user));
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        else {
            bindingResult.rejectValue("password", "INVALID", "Login or password is invalid");
            throw new InvalidRequestException(bindingResult);
        }
    }

    @GetMapping("users/info")
    public ResponseEntity info(@AuthenticationPrincipal String username){
        UserApp user = userDAO.findUserByUsername(username);
        UserInfoParam userInfo = new UserInfoParam(user.getUsername(), user.getEmail());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}

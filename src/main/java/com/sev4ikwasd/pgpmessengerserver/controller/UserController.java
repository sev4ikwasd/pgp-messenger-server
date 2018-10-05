package com.sev4ikwasd.pgpmessengerserver.controller;

import com.sev4ikwasd.pgpmessengerserver.config.service.user.UserService;
import com.sev4ikwasd.pgpmessengerserver.controller.model.RegisterParam;
import com.sev4ikwasd.pgpmessengerserver.controller.model.UserInfoParam;
import com.sev4ikwasd.pgpmessengerserver.database.dao.UserDAO;
import com.sev4ikwasd.pgpmessengerserver.database.model.UserApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class UserController {
    private UserService userService;
    private UserDAO userDAO;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    //private JwtService jwtService;

    @Autowired
    public UserController(UserService userService, UserDAO userDAO, BCryptPasswordEncoder bCryptPasswordEncoder/*, JwtService jwtService*/) {
        this.userService = userService;
        this.userDAO = userDAO;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        //this.jwtService = jwtService;
    }

    @PostMapping("/auth/register")
    public ResponseEntity register(@RequestBody @Valid RegisterParam registerParam, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            throw new BadCredentialsException("Invalid data");
        }
        if (userService.existsByUsername(registerParam.getUsername())) {
            throw new BadCredentialsException("Username is already used");
        }
        if (userService.existsByEmail(registerParam.getEmail())) {
            throw new BadCredentialsException("Email is already used");
        }
        //if (bindingResult.hasErrors()) {
        //    throw new InvalidRequestException(bindingResult);
        //}
        UserApp user = new UserApp(registerParam.getUsername(), registerParam.getEmail(), registerParam.getPassword());
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /*@PostMapping("/users/login")
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
    }*/

    @GetMapping("users/info")
    public ResponseEntity info(@AuthenticationPrincipal String username){
        UserApp user = userService.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username not found"));
        UserInfoParam userInfo = new UserInfoParam(user.getUsername(), user.getEmail());
        return new ResponseEntity<>(userInfo, HttpStatus.OK);
    }
}

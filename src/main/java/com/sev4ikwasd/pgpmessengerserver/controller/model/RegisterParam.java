package com.sev4ikwasd.pgpmessengerserver.controller.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeName(value = "user")
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
public class RegisterParam {
    @NotBlank
    private String username;
    @NotBlank(message = "Email can't be empty")
    @Email(message = "It isn't email")
    private String email;
    @NotBlank(message = "Password can't be empty")
    private String password;
}

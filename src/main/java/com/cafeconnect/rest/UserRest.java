package com.cafeconnect.rest;

import com.cafeconnect.Model.*;
import com.cafeconnect.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@RequestMapping(path = "/user")
public interface UserRest {

    @PostMapping(path = "/signup")
    ResponseEntity<String> signUp(@RequestBody(required = true) UserSignupRequest userSignupRequest);

    @PostMapping(path = "/login")
    public ResponseEntity<String> login(@RequestBody UserLoginRequest userLoginRequest);

    @GetMapping(path = "/get")
    public ResponseEntity<List<UserWrapper>> getAllUser();

    @PostMapping(path = "/update")
    public ResponseEntity<String> update(@RequestBody UpdateUserStatusRequest updateUserStatusRequest);

    @GetMapping(path = "/checkToken")
    ResponseEntity<String> checkToken();

    @PostMapping(path = "/changePassword")
    ResponseEntity<String> changePassword(@RequestBody ChangePasswordRequest changePasswordRequest);

    @PostMapping(path = "/forgotPassword")
    ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest forgotPasswordRequest);
}

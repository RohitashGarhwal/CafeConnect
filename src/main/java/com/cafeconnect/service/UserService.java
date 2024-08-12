package com.cafeconnect.service;

import com.cafeconnect.Model.*;
import com.cafeconnect.wrapper.UserWrapper;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface UserService {
    ResponseEntity<String> signUp(UserSignupRequest userSignupRequest);

    ResponseEntity<String> login(UserLoginRequest userLoginRequest);

    ResponseEntity<List<UserWrapper>> getAllUser();

    ResponseEntity<String> update(UpdateUserStatusRequest updateUserStatusRequest);

    ResponseEntity<String> checkToken();

    ResponseEntity<String> changePassword(ChangePasswordRequest changePasswordRequest);

    ResponseEntity<String> forgotPassword(ForgotPasswordRequest forgotPasswordRequest);
}

package com.cafeconnect.serviceImpl;

import com.cafeconnect.JWT.CustomerUsersDetailsService;
import com.cafeconnect.JWT.JwtFilter;
import com.cafeconnect.JWT.JwtUtil;
import com.cafeconnect.Model.*;
import com.cafeconnect.POJO.User;
import com.cafeconnect.constents.CafeConstants;
import com.cafeconnect.dao.UserDao;
import com.cafeconnect.service.UserService;
import com.cafeconnect.utils.CafeUtils;
import com.cafeconnect.utils.EmailUtils;
import com.cafeconnect.wrapper.UserWrapper;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CustomerUsersDetailsService customerUsersDetailsService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    EmailUtils emailUtils;


    @Override
    public ResponseEntity<String> signUp(UserSignupRequest userSignupRequest) {
        log.info("Inside signup {}", userSignupRequest);
        try {
            if (validateSignUpRequest(userSignupRequest)) {
                User user = userDao.findByEmailId(userSignupRequest.getEmail());
                if (Objects.isNull(user)) {
                    userDao.save(getUserFromRequest(userSignupRequest));
                    return CafeUtils.getResponseEntity("Successfully Registered.", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("Email already exists", HttpStatus.BAD_REQUEST);
                }
            }
            else  {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_DATA, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private boolean validateSignUpRequest(UserSignupRequest userSignupRequest) {
        if (userSignupRequest.getName() != null && userSignupRequest.getContactNumber() != null
                && userSignupRequest.getEmail() != null && userSignupRequest.getPassword() != null) {
            return true;
        }
        return false;
    }

    private User getUserFromRequest(UserSignupRequest userSignupRequest) {
        User user = new User();
        user.setName(userSignupRequest.getName());
        user.setContactNumber(userSignupRequest.getContactNumber());
        user.setEmail(userSignupRequest.getEmail());
        user.setPassword(userSignupRequest.getPassword());
        user.setStatus("false");
        user.setRole("user");
        return user;
    }

    @Override
    public ResponseEntity<String> login(UserLoginRequest userLoginRequest) {
        log.info("Inside login");
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getEmail(), userLoginRequest.getPassword())
            );
            if (auth.isAuthenticated()) {
                if (customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")) {
                    return new ResponseEntity<String>("{\"token\":\""+
                            jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                                    customerUsersDetailsService.getUserDetail().getRole()) + "\"}",
                    HttpStatus.OK);
                }
                else {
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for admin approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        } catch (Exception ex) {
            log.error("{}",ex);
        }
        return new ResponseEntity<String>("{\"message\":\""+"Check Credentials."+"\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try {
            if (jwtFilter.isAdmin()) {
                return new ResponseEntity<>(userDao.getAllUser(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new ArrayList<>(), HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(UpdateUserStatusRequest updateUserStatusRequest) {
        try {
            if (jwtFilter.isAdmin()) {
                Optional<User> optional = userDao.findById(updateUserStatusRequest.getId());
                if (!optional.isEmpty()) {
                    userDao.updateStatus(updateUserStatusRequest.getStatus(), updateUserStatusRequest.getId());
                    sendMailToAllAdmin(updateUserStatusRequest.getStatus(), optional.get().getEmail(), userDao.getAllAdmin());
                    return CafeUtils.getResponseEntity("User status updated successfully", HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity("User id doesn't exist", HttpStatus.OK);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
        allAdmin.remove(jwtFilter.getCurrentUser());
        if (status != null && status.equalsIgnoreCase("true")) {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Approved", "USER:- " + user + " \n is approved by \nADMIN:- " + jwtFilter.getCurrentUser(), allAdmin);
        } else {
            emailUtils.sendSimpleMessage(jwtFilter.getCurrentUser(), "Account Disabled", "USER:- " + user + " \n is disabled by \nADMIN:- " + jwtFilter.getCurrentUser(), allAdmin);
        }
    }


    @Override
    public ResponseEntity<String> checkToken() {
        return CafeUtils.getResponseEntity("true", HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(ChangePasswordRequest changePasswordRequest) {
        try {
            User userObj = userDao.findByEmail(jwtFilter.getCurrentUser());
            if (userObj != null) {
                if (userObj.getPassword().equals(changePasswordRequest.getOldPassword())) {
                    userObj.setPassword(changePasswordRequest.getNewPassword());
                    userDao.save(userObj);
                    return CafeUtils.getResponseEntity("Password Updated Successfully", HttpStatus.OK);
                }
                return CafeUtils.getResponseEntity("Incorrect Old Password", HttpStatus.BAD_REQUEST);
            }
            return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            User user = userDao.findByEmail(forgotPasswordRequest.getEmail());
            if (!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail()))
                emailUtils.forgotMail(user.getEmail(), "Credentials by Cafe Management System", user.getPassword());
            return CafeUtils.getResponseEntity("Check your mail for Credentials.", HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
















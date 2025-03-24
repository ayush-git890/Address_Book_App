package com.example.addressbook.controller;

import com.example.addressbook.interfaces.IUserAuthenticationService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.addressbook.dto.*;
import com.example.addressbook.exception.UserException;
import com.example.addressbook.dto.UserAuthDTO;

@Slf4j
@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    IUserAuthenticationService userAuthenticationService;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO<?>> register(@Valid @RequestBody UserAuthDTO userDTO) {
        log.info("Registering user with email: {}", userDTO.getEmail());
        try {
            UserAuthDTO user = userAuthenticationService.register(userDTO);
            ResponseDTO<UserAuthDTO> responseUserDTO = new ResponseDTO<UserAuthDTO>("User details is submitted!", user);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error registering user: {}", e.getMessage());
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("User details is not submitted!", e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO<?>> login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("Logging in user with email: {}", loginDTO.getEmail());
        try {
            String result = userAuthenticationService.login(loginDTO);
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Login successfully!!", result);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        } catch (UserException e) {
            log.error("Error logging in user: {}", e.getMessage());
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Login failed!!", e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponseDTO<?>> logout(@RequestHeader String sessionToken) {
        log.info("Logging out user with sessionToken: {}", sessionToken);
        try {
            String result = userAuthenticationService.logout(sessionToken);
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Logout successfully!!", result);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        } catch (UserException e) {
            log.error("Error logging out user: {}", e.getMessage());
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Logout failed!!", e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/reset-password")
    public ResponseEntity<ResponseDTO<?>> resetPassword(@RequestHeader String resetToken, @Valid @RequestBody ResetPasswordDTO resetPasswordDTO) {
        log.info("Resetting password for user with resetToken: {}", resetToken);
        try {
            if(!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
                log.error("New password and confirm password do not match");
                ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Password reset failed!!", "New password and confirm password do not match");
                return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
            }

            String result = userAuthenticationService.resetPassword(resetToken, resetPasswordDTO);
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Password reset successfully!!", result);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        } catch (UserException e) {
            log.error("Error resetting password: {}", e.getMessage());
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Password reset failed!!", e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ResponseDTO<?>> forgotPassword(@Valid @RequestBody ForgotPasswordDTO forgotPasswordDTO) {
        log.info("Processing forgot password for email: {}", forgotPasswordDTO.getEmail());
        try {
            String result = userAuthenticationService.forgotPassword(forgotPasswordDTO);
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Forgot password successfully!!", result);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        } catch (UserException e) {
            log.error("Error processing forgot password: {}", e.getMessage());
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Forgot password failed!!", e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/change-password")
    public ResponseEntity<ResponseDTO<?>> changePassword(@RequestHeader String sessionToken, @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        log.info("Changing password for user with sessionToken: {}", sessionToken);
        try {
            if(!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
                log.error("New password and confirm password do not match");
                ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Change password failed!!", "New password and confirm password do not match");
                return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
            }
            String result = userAuthenticationService.changePassword(sessionToken, changePasswordDTO);
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Password changed successfully!!", result);
            return new ResponseEntity<>(responseUserDTO, HttpStatus.OK);
        } catch (UserException e) {
            log.error("Error changing password: {}", e.getMessage());
            ResponseDTO<String> responseUserDTO = new ResponseDTO<String>("Change password failed!!", e.getMessage());
            return new ResponseEntity<>(responseUserDTO, HttpStatus.BAD_REQUEST);
        }
    }
}
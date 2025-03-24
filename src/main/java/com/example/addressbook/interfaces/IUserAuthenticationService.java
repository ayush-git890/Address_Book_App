package com.example.addressbook.interfaces;

import com.example.addressbook.dto.*;
import com.example.addressbook.exception.UserException;

public interface IUserAuthenticationService {
    UserAuthDTO register(UserAuthDTO userDTO) throws Exception;
    String login(LoginDTO loginDTO) throws UserException;
    String logout(String token) throws UserException;
    String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws UserException;
    String resetPassword(String token, ResetPasswordDTO resetPasswordDTO) throws UserException;
    String changePassword(String token, ChangePasswordDTO changePasswordDTO) throws UserException;


}
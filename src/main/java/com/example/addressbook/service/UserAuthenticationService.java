package com.example.addressbook.service;

import com.example.addressbook.dto.*;
import com.example.addressbook.exception.UserException;
import com.example.addressbook.interfaces.IUserAuthenticationService;
import com.example.addressbook.model.UserAuth;
import com.example.addressbook.repository.UserAuthRepository;
import com.example.addressbook.util.JwtToken;
import com.example.addressbook.util.ResetToken;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
public class UserAuthenticationService implements IUserAuthenticationService {

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    JwtToken tokenUtil;     // JwtToken is used to generate JWT tokens.

    @Autowired
    ResetToken resetTokenUtil;  // ResetToken is used to generate reset tokens.

    @Autowired
    PasswordEncoder passwordEncoder;    // PasswordEncoder is used to encode passwords.

    @Autowired
    ModelMapper modelMapper;    // ModelMapper is used to map DTOs to entities and vice versa.

    @Autowired
    UserAuthRepository userAuthRepository;      // UserAuthenticationRepository is used to perform CRUD operations on UserAuthentication entity.

    @Autowired
    RedisTemplate<String, Object> redisTemplate;        // RedisTemplate is used to interact with Redis database.

    @Autowired
    EventSender eventSender;        // EventPublisherService is used to publish events.

    @Override
    @Transactional
    public UserAuthDTO register(UserAuthDTO userDTO) throws Exception {
        if (existsByEmail(userDTO.getEmail()) != null) {
            throw new UserException("Email '" + userDTO.getEmail() + "' is already registered!");
        }

        UserAuth user = modelMapper.map(userDTO, UserAuth.class);
        user.setRole("User");
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword());

        user.setPassword(encodedPassword);

        UserAuth savedUser = userAuthRepository.save(user);

        String customMessage = "REGISTER|" + savedUser.getEmail() + "|" + savedUser.getFirstName() + " " + savedUser.getLastName();
        eventSender.sendMessage(customMessage);

        emailSenderService.sendEmail(user.getEmail(),"Registration Successful!",
                "Hii "+ user.getFirstName() + "..."
                        + "\n\n\n\n You have successfully registered into MyAddressBook App!"
                        + "Your Profile is given below: \n\n"
                        + "First Name: " + user.getFirstName() + "\n"
                        + "Last Name: " + user.getLastName() + "\n"
                        + "Email: " + user.getEmail() + "\n"
        );

        return modelMapper.map(user, UserAuthDTO.class);
    }

    public UserAuth existsByEmail(String email) {
        return userAuthRepository.findByEmail(email);
    }

    public UserAuth existsById(long id) {
        return userAuthRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public String login(LoginDTO loginDTO) throws UserException {
        UserAuth user = existsByEmail(loginDTO.getEmail());
        if (user != null && passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            String sessionToken = tokenUtil.createToken(user.getUserId(), user.getRole());

            redisTemplate.opsForValue().set("session:" + sessionToken, user, 10, TimeUnit.MINUTES);
            emailSenderService.sendEmail(user.getEmail(),"Logged in Successfully!", "Hii...."+user.getFirstName()+"\n\n You have successfully logged in into MyAddressBook App!");

            String customMessage = "LOGIN|" + user.getEmail() + "|" + user.getFirstName() + " " + user.getLastName();
            eventSender.sendMessage(customMessage);

            return "Congratulations!! You have logged in successfully!\n\n Your JWT token is: " + sessionToken;
        } else if (user == null) {
            throw new UserException("Sorry! User not Found!");
        } else if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
            throw new UserException("Sorry! Password is incorrect!");
        } else {
            throw new UserException("Sorry! Email or Password is incorrect!");
        }
    }

    @Override
    public String logout(String sessionToken) throws UserException {
        if (tokenUtil.isTokenExpired(sessionToken))
            throw new UserException("Session expired");

        long userId = Long.parseLong(tokenUtil.decodeToken(sessionToken));
        UserAuth user = existsById(userId);
        if (user != null) {
            redisTemplate.delete(tokenUtil.decodeToken(sessionToken));
            return "Logout successfully!!";
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public String resetPassword(String resetToken, ResetPasswordDTO resetPasswordDTO) throws UserException {
        if (tokenUtil.isTokenExpired(resetToken))
            throw new UserException("Token is expired");

        String email = tokenUtil.decodeToken(resetToken);
        UserAuth user = existsByEmail(email);
        if (user != null) {
            String password = resetPasswordDTO.getNewPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            user.setResetToken(null);
            userAuthRepository.save(user);
            emailSenderService.sendEmail(user.getEmail(),"Password Reset Successfully!", "Hii...."+user.getFirstName()+"\n\n Your password has been reset successfully!");
            return "Password reset successfully!!";
        } else {
            throw new UserException("User not found" + " " + email + " " + resetToken);
        }
    }

    @Override
    @Transactional
    public String forgotPassword(ForgotPasswordDTO forgotPasswordDTO) throws UserException {
        String email = forgotPasswordDTO.getEmail();
        UserAuth user = existsByEmail(email);
        if (user != null) {
            String resetToken = tokenUtil.createToken(email, user.getRole());
            user.setResetToken(resetToken);
            userAuthRepository.save(user);
            String resetLink = "http://localhost:8080/reset-password";
            emailSenderService.sendEmail(user.getEmail(), "Password Reset Request",
                    "Hi " + user.getFirstName()
                            + "\nIt is came to our attention that you request us for reseting your account password since you forgot it."
                            + "\nIf this request isn't made by you, just don't worry. Just don't share the below credentials with anyone else\n\n"
                            + "Click the link to reset your password: " + resetLink + "\n\nUse the following token in the header: " + resetToken);
            return "Reset token sent to your email!";
        } else {
            throw new UserException("User not found");
        }
    }

    @Override
    public String changePassword(String sessionToken, ChangePasswordDTO changePasswordDTO) throws UserException {
        if (tokenUtil.isTokenExpired(sessionToken))
            throw new UserException("Session expired!");

        long userId = Long.parseLong(tokenUtil.decodeToken(sessionToken));
        UserAuth user = existsById(userId);
        if (user != null) {
            String password = changePasswordDTO.getNewPassword();
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
            user.setResetToken(null);
            userAuthRepository.save(user);
            emailSenderService.sendEmail(user.getEmail(),"Password Changed Successfully!", "Hii...."+user.getFirstName()+"\n\n Your password has been changed successfully!");
            return "Password changed successfully!!";
        } else {
            throw new UserException("User not found");
        }
    }
}
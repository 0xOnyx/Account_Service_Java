package account.controller;

import account.DTO.PasswordUpdateDTO;
import account.database.User;
import account.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Auth {
    UserService userService;

    Auth(@Autowired UserService userService)
    {
        this.userService = userService;
    }

    @PostMapping("/api/auth/signup")
    public User   postSignup(@Valid @RequestBody User user) {
        return userService.userSignup(user);
    }


    @PostMapping("/api/auth/changepass")
    public PasswordUpdateDTO   postChangepass(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PasswordUpdateDTO passwordUpdateDTO) {
        return userService.userUpdatePassword(userDetails, passwordUpdateDTO);
    }
}

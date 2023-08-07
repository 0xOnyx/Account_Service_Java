package account.service;


import account.exception.UserException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordService {
    private final PasswordEncoder passwordEncoder;

    private final static List<String> leekPasswords = List.of("PasswordForJanuary", "PasswordForFebruary", "PasswordForMarch", "PasswordForApril",
            "PasswordForMay", "PasswordForJune", "PasswordForJuly", "PasswordForAugust",
            "PasswordForSeptember", "PasswordForOctober", "PasswordForNovember", "PasswordForDecember");

    PasswordService(@Autowired PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Boolean checkPasswordLength(String password)
    {
        return password.matches("^.{12,}$");
    }

    public Boolean isLeekPassword(String password)
    {
        return leekPasswords.contains(password);
    }

    public void isTheSamePassword(String password, String encodedPassword) throws UserException
    {
        if (this.passwordEncoder.matches(password, encodedPassword))
            throw new UserException("The passwords must be different!");
    }

    public void checkPassword(String password) throws UserException
    {
        if (password == null || password.isEmpty())
            throw new UserException("The password is empty!");
        if (!this.checkPasswordLength(password))
            throw new UserException("Password length must be 12 chars minimum!");
        if (this.isLeekPassword(password))
            throw new UserException("The password is in the hacker's database!");
    }
}

package account.service;

import account.DTO.PasswordUpdateDTO;
import account.database.Event;
import account.database.Role;
import account.database.User;
import account.exception.UserException;
import account.repository.EventRepository;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    UserRepository  userRepository;
    RoleRepository  roleRepository;
    PasswordEncoder passwordEncoder;
    PasswordService passwordService;
    EventRepository eventRepository;

    @Autowired
    UserService(UserRepository userRepository,
                EventRepository eventRepository,
                PasswordEncoder passwordEncoder,
                PasswordService passwordService,
                RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.passwordEncoder = passwordEncoder;
        this.passwordService = passwordService;
        this.roleRepository = roleRepository;
    }

    public User userSignup(User user) throws UserException
    {
        Role role;

        if (userRepository.findByEmailIgnoreCase(user.getEmail()) != null)
            throw new UserException("User exist!");
        user.setEmail(user.getEmail().toLowerCase());
        passwordService.checkPassword(user.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        if (userRepository.count() == 0)
            role = roleRepository.findByName("ADMINISTRATOR");
        else
            role = roleRepository.findByName("USER");
        if (role != null)
            user.addRole(role);
        eventRepository.save(
                new Event(Event.ACTION.CREATE_USER, user.getEmail())
        );
        return userRepository.save(user);
    }

    public PasswordUpdateDTO    userUpdatePassword(UserDetails userDetails, PasswordUpdateDTO passwordUpdateDTO) throws UserException
    {
        User user = userRepository.findByEmailIgnoreCase(userDetails.getUsername());
        if (user == null)
            throw new UserException("User not found!");
        passwordService.checkPassword(passwordUpdateDTO.getNew_password());
        passwordService.isTheSamePassword(passwordUpdateDTO.getNew_password(), user.getPassword());
        user.setPassword(passwordEncoder.encode(passwordUpdateDTO.getNew_password()));
        userRepository.save(user);
        passwordUpdateDTO.setStatus("The password has been updated successfully");
        passwordUpdateDTO.setEmail(user.getEmail());
        eventRepository.save(
                new Event(Event.ACTION.CHANGE_PASSWORD, user.getEmail(), userDetails.getUsername())
        );
        return passwordUpdateDTO;
    }
}

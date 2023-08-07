package account.service;

import account.DTO.LockUpdateDTO;
import account.DTO.RoleUpdateDTO;
import account.DTO.StatusMessage;
import account.DTO.StatusMessageUser;
import account.database.Event;
import account.database.Role;
import account.database.User;
import account.exception.UserException;
import account.exception.UserNotFoundException;
import account.repository.EventRepository;
import account.repository.RoleRepository;
import account.repository.UserRepository;
import account.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    UserRepository userRepository;
    RoleRepository roleRepository;
    EventRepository eventRepository;

    LoginAttemptService loginAttemptService;

    @Autowired
    public AdminService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            EventRepository eventRepository,
            LoginAttemptService loginAttemptService
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.eventRepository = eventRepository;
        this.loginAttemptService = loginAttemptService;
    }

    public Iterable<User> getUser() {
        return userRepository.findAll();
    }

    public StatusMessageUser deleteUser(String email) {
        User user = userRepository.findByEmailIgnoreCase(email);
        if (user == null) {
            throw new UserNotFoundException("User not found!");
        }
        if (user.getRoles().stream().anyMatch(role -> role.getName().equals("ADMINISTRATOR"))) {
            throw new UserException("Can't remove ADMINISTRATOR role!");
        }
        userRepository.delete(user);
        eventRepository.save(
                new Event(Event.ACTION.DELETE_USER, email, SecurityContextHolder.getContext().getAuthentication().getName())
        );
        return new StatusMessageUser(user.getEmail(), "Deleted successfully!");
    }

    public User putChangeRole(RoleUpdateDTO roleUpdateDTO)
    {
        User user = userRepository.findByEmailIgnoreCase(roleUpdateDTO.getUser());
        Role role = roleRepository.findByName(roleUpdateDTO.getRole());

        if (user == null)
            throw new UserNotFoundException("User not found!");
        else if (role == null)
            throw new UserNotFoundException("Role not found!");
        else if ((user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMINISTRATOR")) && (role.getName().equals("ACCOUNTANT") || role.getName().equals("USER") || role.getName().equals("AUDITOR")) )
                || (user.getRoles().stream().anyMatch(r -> r.getName().equals("ACCOUNTANT") || r.getName().equals("USER") || r.getName().equals("AUDITOR")) && role.getName().equals("ADMINISTRATOR")))
            throw new UserException("The user cannot combine administrative and business roles!");
        else if (role.getName().equals("ADMINISTRATOR"))
            throw new UserException("Can't remove ADMINISTRATOR role!");
        else if (roleUpdateDTO.getOperation() == RoleUpdateDTO.OPERATION.REMOVE && user.getRoles().stream().noneMatch(r -> r.getName().equals(role.getName())))
            throw new UserException("The user does not have a role!");
        else if (roleUpdateDTO.getOperation() == RoleUpdateDTO.OPERATION.REMOVE && user.getRoles().size() == 1)
            throw new UserException("The user must have at least one role!");

//        else if (user.getRoles().stream().anyMatch(r -> r.getName().equals(role.getName())))
//            throw new UserException("User already has this role!");

        if (roleUpdateDTO.getOperation() == RoleUpdateDTO.OPERATION.GRANT) {
            user.addRole(role);
            eventRepository.save(
                    new Event(
                            Event.ACTION.GRANT_ROLE,
                            String.format("Grant role %s to %s", role.getName(), user.getEmail()),
                            SecurityContextHolder.getContext().getAuthentication().getName()
                    )
            );
        }
        else if (roleUpdateDTO.getOperation() == RoleUpdateDTO.OPERATION.REMOVE) {
            user.removeRole(role);
            eventRepository.save(
                    new Event(
                            Event.ACTION.REMOVE_ROLE,
                            String.format("Remove role %s from %s", role.getName(), user.getEmail()),
                            SecurityContextHolder.getContext().getAuthentication().getName()
                    )
            );
        }
        userRepository.save(user);
        return user;
    }

    public StatusMessage    putChangeAccess(LockUpdateDTO lockUpdateDTO)
    {
        User user = userRepository.findByEmailIgnoreCase(lockUpdateDTO.getUser());
        if (user == null)
            throw new UserNotFoundException("User not found!");
        else if (user.getRoles().stream().anyMatch(r -> r.getName().equals("ADMINISTRATOR")))
            throw new UserException("Can't lock the ADMINISTRATOR!");

        if (lockUpdateDTO.getOperation() == LockUpdateDTO.OPERATION.LOCK) {
            user.setLocked(true);
            eventRepository.save(
                    new Event(
                            Event.ACTION.LOCK_USER,
                            String.format("Lock user %s", user.getEmail()),
                            SecurityContextHolder.getContext().getAuthentication().getName()
                    )
            );
        }
        else if (lockUpdateDTO.getOperation() == LockUpdateDTO.OPERATION.UNLOCK) {
            user.setLocked(false);
            loginAttemptService.loginSucceeded(user.getEmail());
            eventRepository.save(
                    new Event(
                            Event.ACTION.UNLOCK_USER,
                            String.format("Unlock user %s", user.getEmail()),
                            SecurityContextHolder.getContext().getAuthentication().getName()
                    )
            );
        }
        userRepository.save(user);
        return new StatusMessage(String.format("User %s %s!",
                user.getEmail(),
                lockUpdateDTO.getOperation() == LockUpdateDTO.OPERATION.LOCK ? "locked" : "unlocked"
        ));
    }
}

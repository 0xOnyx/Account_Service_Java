package account.security;

import account.database.Event;
import account.database.User;
import account.repository.EventRepository;
import account.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFailureListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {

    private final LoginAttemptService   loginAttemptService;
    private final EventRepository       eventRepository;

    private final UserRepository        userRepository;
    private final HttpServletRequest    request;

    @Autowired
    AuthenticationFailureListener(
            LoginAttemptService loginAttemptService,
            EventRepository eventRepository,
            UserRepository userRepository,
            HttpServletRequest request){
        this.loginAttemptService = loginAttemptService;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.request = request;
    }

    @Override
    public void onApplicationEvent(@NotNull AuthenticationFailureBadCredentialsEvent event) {
        Authentication authentication = event.getAuthentication();
        String key = authentication.getName();


        eventRepository.save(new Event(Event.ACTION.LOGIN_FAILED, request.getRequestURI(), authentication.getName()));
//        Object principal = authentication.getPrincipal();

//        if (principal instanceof UserDetails && !((UserDetails)principal).isAccountNonLocked())
//            eventRepository.save(new Event(Event.ACTION.LOCK_USER, request.getRequestURI(), event.getAuthentication().getName()));
        loginAttemptService.loginFailed(key);
        if (loginAttemptService.isBlocked(key))
        {

            User user = userRepository.findByEmailIgnoreCase(authentication.getName());
            if (user != null && !user.isLocked() && user.getRoles().stream().noneMatch(role -> role.getName().equals("ADMINISTRATOR"))) {
                user.setLocked(true);
                eventRepository.save(new Event(Event.ACTION.BRUTE_FORCE, request.getRequestURI(), event.getAuthentication().getName()));
                eventRepository.save(new Event(Event.ACTION.LOCK_USER, String.format("Lock user %s", user.getEmail()), event.getAuthentication().getName()));
                userRepository.save(user);
            }
        }
    }
}

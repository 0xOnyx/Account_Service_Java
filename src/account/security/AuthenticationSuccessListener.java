package account.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationSuccessListener implements
        ApplicationListener<AuthenticationSuccessEvent> {
    private final LoginAttemptService   loginAttemptService;

    @Autowired
    AuthenticationSuccessListener(LoginAttemptService loginAttemptService)
    {
        this.loginAttemptService = loginAttemptService;
    }

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        loginAttemptService.loginSucceeded(event.getAuthentication().getName());

    }
}

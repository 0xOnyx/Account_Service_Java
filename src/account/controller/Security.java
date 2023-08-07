package account.controller;

import account.database.Event;
import account.service.SecurityService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Security {

    SecurityService securityService;

    Security(SecurityService securityService)
    {
        this.securityService = securityService;
    }

    @GetMapping("/api/security/events/")
    public Iterable<Event> getLoginAttempts() {
        return securityService.getLoginAttempts();
    }
}

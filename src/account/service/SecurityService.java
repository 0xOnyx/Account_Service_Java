package account.service;

import account.database.Event;
import account.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {

        EventRepository eventRepository;

        @Autowired
        SecurityService(EventRepository eventRepository) {
                this.eventRepository = eventRepository;
        }

        public Iterable<Event> getLoginAttempts() {
                return eventRepository.findAll();
        }
}

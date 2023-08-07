package account.database;

import jakarta.persistence.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;

@Entity
@Table(name = "events")
public class Event {
    public enum ACTION {
        CREATE_USER,
        CHANGE_PASSWORD,
        ACCESS_DENIED,
        LOGIN_FAILED,
        GRANT_ROLE,
        REMOVE_ROLE,
        LOCK_USER,
        UNLOCK_USER,
        DELETE_USER,
        BRUTE_FORCE,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private ACTION action;

    @NotEmpty
    String subject;

    @NotEmpty
    String object;

    @NotEmpty
    String path;

    public Event() {
    }

    public Event(ACTION action, String object, String subject) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = attr.getRequest();

        this.date = new Date();
        this.action = action;
        this.subject = subject;
        this.object = object;
        this.path = req.getRequestURI();
    }

    public Event(ACTION action, String object) {
        this(action, object, "Anonymous");
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public ACTION getAction() {
        return action;
    }

    public void setAction(ACTION action) {
        this.action = action;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getObject() {
        return object;
    }

    public void setObject(String object) {
        this.object = object;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

}

package account.DTO;

public class StatusMessageUser {
    private String user;
    private String status;

    public StatusMessageUser(String user, String status) {
        this.user = user;
        this.status = status;
    }

    public StatusMessageUser() {
        this("user", "OK");
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

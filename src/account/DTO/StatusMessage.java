package account.DTO;

public class StatusMessage {
    private String status;

    public StatusMessage(String status) {
        this.status = status;
    }

    public StatusMessage() {
        this("OK");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

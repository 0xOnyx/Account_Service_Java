package account.DTO;


import account.database.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;

public class PasswordUpdateDTO {
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String email;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    String status;

    @NotEmpty
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    String new_password;

    PasswordUpdateDTO() {
    }

    PasswordUpdateDTO(User user, String status) {
        this.email = user.getEmail();
        this.status = status;
    }


    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getNew_password() {
        return new_password;
    }

    public void setNew_password(String new_password) {
        this.new_password = new_password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

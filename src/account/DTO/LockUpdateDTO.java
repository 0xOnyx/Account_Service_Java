package account.DTO;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

public class LockUpdateDTO {
    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public void setOperation(OPERATION operation) {
        this.operation = operation;
    }

    public enum OPERATION {
        LOCK,  UNLOCK
    }

    @NotEmpty
    @Email
    @Pattern(regexp = "\\w+(@acme.com)$")
    private String user;

    @NotEmpty
    OPERATION operation;

    public LockUpdateDTO() {
    }

    public LockUpdateDTO(String user, OPERATION operation) {
        this.user = user;
        this.operation = operation;
    }


}

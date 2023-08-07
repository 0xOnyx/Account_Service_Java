package account.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;



public class RoleUpdateDTO {
    public enum OPERATION {
        GRANT,  REMOVE
    }

    @NotEmpty
    @Email
    @Pattern(regexp = "\\w+(@acme.com)$")
    String  user;

    @NotEmpty
    String  role;

    @NotEmpty
    OPERATION operation;

    public RoleUpdateDTO() {
    }

    public RoleUpdateDTO(String user, String role, OPERATION operation) {
        this.user = user;
        this.role = role;
        this.operation = operation;
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public OPERATION getOperation() {
        return operation;
    }

    public void setOperation(OPERATION operation) {
        this.operation = operation;
    }
}

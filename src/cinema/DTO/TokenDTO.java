package cinema.DTO;

import java.util.UUID;

public class TokenDTO {
    UUID token;

    public TokenDTO() {
    }

    public TokenDTO(UUID token) {
        this.token = token;
    }

    public UUID getToken() {
        return token;
    }

    void setToken(UUID token) {
        this.token = token;
    }
}

package cinema.DTO;

import cinema.utils.Seat;

import java.util.UUID;

public class PurchaseSeatDTO {
    UUID    token;
    Seat ticket;

    public PurchaseSeatDTO() {
    }

    public PurchaseSeatDTO(UUID token, Seat seat) {
        this.token = token;
        this.ticket = seat;
    }

    public UUID getToken() {
        return token;
    }

    public Seat getTicket() {
        return ticket;
    }

    static public PurchaseSeatDTO convertSeatToDTO(UUID token, Seat seat) {
        return new PurchaseSeatDTO(token, seat);
    }
}

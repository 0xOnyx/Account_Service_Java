package cinema.DTO;

import cinema.utils.Seat;

public class ReturnTicketDTO {
    Seat returned_ticket;

    public ReturnTicketDTO() {
    }

    public ReturnTicketDTO(Seat returned_ticket) {
        this.returned_ticket = returned_ticket;
    }

    public Seat getReturned_ticket() {
        return returned_ticket;
    }

}

package cinema.controller;

import cinema.DTO.PurchaseSeatDTO;
import cinema.DTO.ReturnTicketDTO;
import cinema.DTO.SeatDTO;
import cinema.DTO.TokenDTO;
import cinema.exception.AuthentificationException;
import cinema.exception.SeatException;
import cinema.service.RoomService;
import cinema.utils.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class Cinema {
    RoomService roomService;

    Cinema(@Autowired RoomService roomService) {
        this.roomService = roomService;
    }

    @GetMapping("/seats")
    public Room getSeats() {
        return roomService.getAllSeat();
    }

    @PostMapping("/purchase")
    public PurchaseSeatDTO purchaseSeat(@RequestBody SeatDTO seatDTO){
        Seat seat = SeatDTO.convertDTOToSeat(seatDTO);
        PurchaseSeatDTO purchaseSeatDTO;

        if (seat.getColumn() > roomService.getAllSeat().getTotalColumns()
            || seat.getRow() > roomService.getAllSeat().getTotalRows()
            || seat.getColumn() < 1 || seat.getRow() < 1)
            throw new SeatException("The number of a row or a column is out of bounds!");
         purchaseSeatDTO = roomService.purchaseSeat(seat);
        if (purchaseSeatDTO == null)
            throw new SeatException("The ticket has been already purchased!");
        return purchaseSeatDTO;
    }

    @PostMapping("/return")
    public ReturnTicketDTO  returnTicket(@RequestBody TokenDTO token) {
        Seat seat = roomService.returnTicket(token.getToken());

        if (seat == null)
            throw new SeatException("Wrong token!");
        return new ReturnTicketDTO(seat);
    }

    @ExceptionHandler(SeatException.class)
    public ResponseEntity<ErrorMessage> handleSeatException(SeatException ex) {
        ErrorMessage error = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthentificationException.class)
    public ResponseEntity<ErrorMessage> handleAuthentificationException(AuthentificationException ex) {
        ErrorMessage error = new ErrorMessage(ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @GetMapping("/stats")
    public Statistic getStats(@RequestParam (required = false) String password) {
        if (password == null || !password.equals("super_secret"))
            throw new AuthentificationException("The password is wrong!");
        return roomService.getStatistic();

    }
}

package cinema.DTO;

import cinema.utils.Seat;

public class SeatDTO {
    private int row;
    private int column;

    public SeatDTO() {
    }

    public SeatDTO(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public static SeatDTO convertSeatToDTO(Seat seat) {
        return new SeatDTO(seat.getRow(), seat.getColumn());
    }

    public static Seat convertDTOToSeat(SeatDTO seatDTO) {
        return new Seat(seatDTO.getRow(), seatDTO.getColumn());
    }

}

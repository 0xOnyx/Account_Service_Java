package cinema.service;

import cinema.DTO.PurchaseSeatDTO;
import cinema.utils.Room;
import cinema.utils.Seat;
import cinema.utils.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
public class RoomService {
    Room room;

    RoomService(@Autowired Room room) {
        this.room = room;
    }

    public Room getAllSeat()
    {
        return room;
    }

    public PurchaseSeatDTO purchaseSeat(Seat seat)
    {
        List<Seat> available_seats = room.getAvailableSeats();
        UUID uuidv4 = UUID.randomUUID();

        int index = IntStream.range(0, available_seats.size())
                .filter(i -> available_seats.get(i).getRow() == seat.getRow() && available_seats.get(i).getColumn() == seat.getColumn())
                .findFirst()
                .orElse(-1);
        if (index < 0)
            return null;
        available_seats.remove(index);
        room.getPurchaseSeat().put(uuidv4, seat);
        return new PurchaseSeatDTO(uuidv4, seat);
    }

    public Seat returnTicket(UUID token)
    {
        Seat seat = room.getPurchaseSeat().get(token);

        if (seat == null)
            return null;
        room.getPurchaseSeat().remove(token);
        room.getAvailableSeats().add((seat.getRow() - 1) * room.getTotalRows() + (seat.getColumn() - 1), seat);
        return seat;
    }

    public Statistic getStatistic()
    {
        int current_income;

        current_income = room.getPurchaseSeat().values().stream().mapToInt(Seat::getPrice).sum();
        return new Statistic(current_income, room.getAvailableSeats().size(), room.getPurchaseSeat().size());
    }
}

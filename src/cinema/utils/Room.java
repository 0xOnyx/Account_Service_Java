package cinema.utils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@JsonIgnoreProperties({"purchase_seat"})
public class Room {

    @JsonProperty("total_rows")
    private final int totalRows;

    @JsonProperty("total_columns")
    private final int totalColumns;

    @JsonProperty("available_seats")
    private final List<Seat> availableSeats;

    @JsonProperty("purchase_seat")
    private final Map<UUID, Seat> purchaseSeat;

    Room() {
        this(9, 9);
    }

    Room(int total_columns, int total_rows) {
        this.totalRows = total_rows;
        this.totalColumns = total_columns;
        this.availableSeats = Collections.synchronizedList(new ArrayList<>());
        for (int row = 1; row <= total_rows; row++) {
            for (int column = 1; column <= total_columns; column++) {
                this.availableSeats.add(new Seat(row, column));
            }
        }
         this.purchaseSeat = new ConcurrentHashMap<>();
    }

    @JsonProperty("total_rows")
    public int getTotalRows() {
        return totalRows;
    }

    @JsonProperty("total_columns")
    public int getTotalColumns() {
        return totalColumns;
    }

    @JsonProperty("available_seats")
    public List<Seat> getAvailableSeats() {
        return availableSeats;
    }

    @JsonProperty("purchase_seat")
    public Map<UUID, Seat> getPurchaseSeat() {
        return purchaseSeat;
    }
}

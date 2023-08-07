package cinema.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SeatException extends RuntimeException {
    public SeatException(String message) {
        super(message);
    }
}

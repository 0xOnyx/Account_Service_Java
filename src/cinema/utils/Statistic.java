package cinema.utils;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Statistic {

    @JsonProperty("current_income")
    int currentIncome;

    @JsonProperty("number_of_available_seats")
    int numberOfAvailableSeats;

    @JsonProperty("number_of_purchased_tickets")
    int numberOfPurchasedTickets;

    public Statistic() {
    }

    public Statistic(int currentIncome, int numberOfAvailableSeats, int numberOfPurchasedTickets) {
        this.currentIncome = currentIncome;
        this.numberOfAvailableSeats = numberOfAvailableSeats;
        this.numberOfPurchasedTickets = numberOfPurchasedTickets;
    }

    @JsonProperty("current_income")
    public int getCurrentIncome() {
        return currentIncome;
    }

    @JsonProperty("number_of_available_seats")
    public int getNumberOfAvailableSeats() {
        return numberOfAvailableSeats;
    }

    @JsonProperty("number_of_purchased_tickets")
    public int getNumberOfPurchasedTickets() {
        return numberOfPurchasedTickets;
    }
}

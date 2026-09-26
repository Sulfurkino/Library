package BookingOffice;

import java.util.*;

public class BookingDesk {
    private final Map<Integer, Seat> seats = new HashMap<>();

    public BookingDesk(List<Seat> initialSeats) {
        for (Seat seat : initialSeats) {
            seats.put(seat.getNumber(), seat);
        }
    }

    public boolean reserveMistake(List<Integer> seatNumbers) {
        boolean success = true;
        for (Integer number : seatNumbers) {
            Seat seat = seats.get(number);
            if (seat != null) {
                if (!seat.isReserved()) {
                    seat.reserve(); // mistake
                } else {
                    success = false;
                    break;
                }
            } else {
                success = false;
                break;
            }
        }
        return success;
    }

    public boolean reserve(List<Integer> seatNumbers){
        Set<Integer> numberSet = new HashSet<>();

        if (seatNumbers == null||seatNumbers.isEmpty()){
            return false;
        }
        for (Integer number : seatNumbers){
            Seat seat = seats.get(number);
            if (number == null || !numberSet.add(number)){
                return false;
            }
            if (seat == null || seat.isReserved()){
                return false;
            }
        }
        for (Integer number : seatNumbers){
            seats.get(number).reserve();
        }
        return true;
    }

    public boolean isReserved(int number) {
        Seat seat = seats.get(number);
        return seat != null && seat.isReserved();
    }
}
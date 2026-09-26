package BookingOffice;

public class Seat {
    private final int number;
    private boolean reserved;

    public Seat(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }

    public boolean isReserved() {
        return reserved;
    }

    public void reserve() {
        reserved = true;
    }
}
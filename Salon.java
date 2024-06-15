import java.util.Arrays;

public class Salon {
    private int salonID;
    private TypeOfSaloon typeofSaloon;
    private int numberOfSeats;
    private boolean[] seats;

    public Salon (int salonID , TypeOfSaloon typeofSaloon ,  int numberOfSeats) {
        this.salonID = salonID;
        this.numberOfSeats = numberOfSeats;
        this.typeofSaloon = typeofSaloon;
        this.seats = new boolean[numberOfSeats];
        Arrays.fill(seats, false);
    }

    public boolean isFullSeat(int seatnum) {
        return seats[seatnum];
    } //Koltuğun dolu/boş durumu

    public void fillSeat(int seatnum) { //Seçilen koltuğu doldur
        if (seatnum >= 0 && seatnum < seats.length) {
            seats[seatnum] = true;
        } else {
            System.out.println("Geçersiz koltuk numarası.");
        }
    }

    public void vacateSeat(int seatnum) { // Koltuğu boşalt
        if (seatnum >= 0 && seatnum < seats.length) {
            seats[seatnum] = false;
        } else {
            System.out.println("Geçersiz koltuk numarası.");
        }
    }
    public int getSalonID() {
        return salonID;
    }
    public void setSalonID(int salonID) {
        this.salonID = salonID;
    }
    public int getSeatCounter() {
        return numberOfSeats;
    }
    public void setSeatCounter(int seatCounter) {
        this.numberOfSeats = seatCounter;
    }
    public TypeOfSaloon getTypeofSaloon() {
        return typeofSaloon;
    }
    public void setTypeofSaloon(TypeOfSaloon typeofSaloon) {
        this.typeofSaloon = typeofSaloon;
    }
}

public class Ticket {

    private User user;
    private Seans seans;
    private int seatNumber;
    private TypeOfTicket typeOfTicket;

    public Ticket (User user , Seans seans , int seatNumber , TypeOfTicket typeOfTicket) {
        this.user = user;
        this.seans = seans;
        this.seatNumber = seatNumber;
        this.typeOfTicket = typeOfTicket;
    }

    public User getUser() {
        return user;
    }

    public TypeOfTicket getTypeOfTicket() {
        return typeOfTicket;
    }

    public int getSeatNumber() {
        return seatNumber;
    }

    public Seans getSeans() {
        return seans;
    }

    public void setTypeOfTicket(TypeOfTicket typeOfTicket) {
        this.typeOfTicket = typeOfTicket;
    }

    public void setSeans(Seans seans) {
        this.seans = seans;
    }

    public void setSeatNumber(int seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
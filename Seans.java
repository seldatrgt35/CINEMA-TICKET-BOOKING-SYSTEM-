import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Seans {
    private Film movieName;
    private Salon salonID;
    private TypeOfSaloon typeOfSaloon;
    private LocalDateTime date;
    private Map<TypeOfSaloon, Map<TypeOfTicket, Double>> ticketPrices;



    public Seans() {
        this.ticketPrices = new HashMap<>();

    }



    public Seans (Film movieName , Salon salonID , LocalDateTime date , TypeOfSaloon typeOfSaloon) {
        this.movieName = movieName;
        this.salonID = salonID;
        this.date = date;
        this.typeOfSaloon = typeOfSaloon;

    }

    public Film getMovieName() {
        return movieName;
    }

    public void setMovieName(Film movieName) {
        this.movieName = movieName;
    }

    public Salon getSalonID() {
        return salonID;
    }

    public void setSalonID(Salon salonID) {
        this.salonID = salonID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public TypeOfSaloon getTypeOfSaloon() {
        return typeOfSaloon;
    }

    public void setTypeOfSaloon(Salon typeOfSaloon) {
        this.typeOfSaloon = typeOfSaloon.getTypeofSaloon();
    }

    public Map<TypeOfSaloon, Map<TypeOfTicket, Double>> getTicketPrices() {
        return ticketPrices;
    }

    public double getTicketPrice(TypeOfSaloon typeOfSaloon, TypeOfTicket typeOfTicket) {
        if (ticketPrices.containsKey(typeOfSaloon)) {
            Map<TypeOfTicket, Double> salonPrices = ticketPrices.get(typeOfSaloon);

            if (salonPrices.containsKey(typeOfTicket)) {
                return salonPrices.get(typeOfTicket);
            }
        }
        return 0.0; // Varsayılan olarak 0 fiyatını döndür
    }

    public void setTicketPrices(TypeOfSaloon typeOfSaloon , TypeOfTicket typeOfTicket , double cost) {
        ticketPrices.get(typeOfSaloon).put(typeOfTicket , cost);
    }


}

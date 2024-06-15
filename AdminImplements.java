import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class AdminImplements implements Admin {

    public void saveMovies(List<Film> films) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:/movies.txt"));
        for (Film film : films) {
            writer.write(film.getMovieName() + "," + film.getDirector() + "," + film.getMovieTime() + "," + film.getMovieType());
            writer.newLine();
        }
        writer.close();
    }

    public void saveSessions(List<Seans> sessions) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("C:/seanslar.txt"));
        for (Seans seans : sessions) {
            writer.write(seans.getMovieName().getMovieName() + "," + seans.getSalonID().getSalonID() + "," + (seans.getDate().getYear() + "-" + seans.getDate().getMonthValue() + "-" + seans.getDate().getDayOfMonth() + "-" + seans.getDate().getHour() + "-" + seans.getDate().getMinute()) + "," + seans.getTypeOfSaloon());
            writer.newLine();
        }
        writer.close();
    }

    public void addNewFilm() throws IOException {
        List<Film> films = databaseMovies();
        String name = JOptionPane.showInputDialog("Movie Name:");
        String directorName = JOptionPane.showInputDialog("Director Name:");
        int duration = Integer.parseInt(JOptionPane.showInputDialog("Movie Duration (minute):"));
        String genre = JOptionPane.showInputDialog("Movie Type:");

        Film newFilm = new Film(name, directorName , duration, genre);
        films.add(newFilm);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/movies.txt", true))) {
            writer.write(newFilm.getMovieName() + "," + newFilm.getDirector() + "," + newFilm.getMovieTime() + "," + newFilm.getMovieType());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "The movie was successfully added!");
    }


    public void deleteFilm() throws IOException {
        List<Film> films = databaseMovies();
        String name = JOptionPane.showInputDialog("The name of the movie you want to delete:");
        Film filmToRemove = null;
        for (Film film : films) {
            if (film.getMovieName().equals(name)) {
                filmToRemove = film;
                break;
            }
        }
        if (filmToRemove != null) {
            films.remove(filmToRemove);
            saveMovies(films);

            JOptionPane.showMessageDialog(null, "The movie was successfully deleted!");
        } else {
            JOptionPane.showMessageDialog(null, "The movie was not found!");
        }
    }


    public void addNewMovieSession() throws IOException {
        List<Seans> seanslar = creatingSessions();
        List<Salon> salons = databaseSaloons();
        List<Film> films = databaseMovies();
        String movieName = JOptionPane.showInputDialog("Movie Name:");
        Film filmToRemove = null;
        for (Film film : films) {
            if (film.getMovieName().equals(movieName)) {
                filmToRemove = film;
                break;
            }
        }
        int IDSalon = Integer.parseInt(JOptionPane.showInputDialog("Saloon ID:"));
        String movieDate = JOptionPane.showInputDialog("Sessions Date(DD/MM/YYYY):");
        String[] date1 = movieDate.split("/");
        int day = Integer.parseInt(date1[0]);
        int month = Integer.parseInt(date1[1]);
        int year = Integer.parseInt(date1[2]);
        String time = JOptionPane.showInputDialog("Sessions Time (HH:MM):");
        String[] date2 = time.split(":");
        int hour = Integer.parseInt(date2[0]);
        int minute = Integer.parseInt(date2[1]);
        LocalDateTime seanstime = LocalDateTime.of(year,month,day,hour,minute);
        Seans seans = new Seans(filmToRemove , salons.get(IDSalon) , seanstime , salons.get(IDSalon).getTypeofSaloon());
        seanslar.add(seans);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("C:/seanslar.txt", true))) {
            writer.write(seans.getMovieName().getMovieName() + "," + seans.getSalonID().getSalonID() + "," +  year + "-" + month +"-" + day + "-" + hour + "-" +minute + "," + seans.getTypeOfSaloon());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        JOptionPane.showMessageDialog(null, "The session was successfully added!");
    }



    public void deleteSession() throws IOException {
        List<Seans> sessions = creatingSessions();
        String moviename = JOptionPane.showInputDialog("The movie name of the session you want to delete::");
        int salonID = Integer.parseInt(JOptionPane.showInputDialog(("The saloon ID of the session you want to delete:")));
        String movieDate = JOptionPane.showInputDialog("Sessions Date (DD/MM/YYYY):");
        String[] date1 = movieDate.split("/");
        int day = Integer.parseInt(date1[0]);
        int month = Integer.parseInt(date1[1]);
        int year = Integer.parseInt(date1[2]);
        String time = JOptionPane.showInputDialog("Sessions Time (HH:MM):");
        String[] date2 = time.split(":");
        int hour = Integer.parseInt(date2[0]);
        int minute = Integer.parseInt(date2[1]);
        LocalDateTime seanstime = LocalDateTime.of(year,month,day,hour,minute);


        Seans seansToRemove = null;
        for (Seans seans : sessions) {
            if (seans.getMovieName().getMovieName().equals(moviename) && (seans.getSalonID().getSalonID() == salonID) && seans.getDate().equals(seanstime)) {
                seansToRemove = seans;
                break;
            }
        }
        if (seansToRemove != null) {
            sessions.remove(seansToRemove);
            saveSessions(sessions);

            JOptionPane.showMessageDialog(null, "The session was successfully deleted!");
        } else {
            JOptionPane.showMessageDialog(null, "The session was not found!");
        }
    }

    public void reportsofticketsold() throws IOException {
        File file = new File("past_purchases.txt");
        int AdultCounter = 0;
        int StudentCounter = 0;
        if (file.exists()) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            String[] counterparts;
            while ((line = reader.readLine()) != null) {
                counterparts = line.split(",");
                if (counterparts[6].equals("ADULT"))
                    AdultCounter++;
                else if (counterparts[6].equals("STUDENT")) {
                    StudentCounter++;
                }
            }

        }
        JOptionPane.showMessageDialog(null , "Number Of Adult Ticket:" + AdultCounter + "\n" + "Number of Student Ticket:" + StudentCounter);
    }



    private static List<Film> databaseMovies() throws IOException {
        List<Film> films = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader("C:/movies.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] movieparts = line.split(",");
            String moviename = movieparts[0];
            String directorname = movieparts[1];
            int movietime = Integer.parseInt(movieparts[2]);
            String movietype = movieparts[3];
            Film film = new Film(moviename, directorname, movietime, movietype);

            films.add(film);
        }

        return films;
    }
    private static List<Salon> databaseSaloons() throws IOException {
        List<Salon> saloons = new ArrayList<>();
        Salon salon1 = new Salon(1, TypeOfSaloon.StandartSaloon, 40);
        Salon salon2 = new Salon(2, TypeOfSaloon.StandartSaloon, 40);
        Salon salon3 = new Salon(3, TypeOfSaloon.IMAXSaloon, 60);
        Salon salon4 = new Salon(4, TypeOfSaloon.GoldClassSaloon, 30);
        saloons.add(salon1);
        saloons.add(salon2);
        saloons.add(salon3);
        saloons.add(salon4);

        return saloons;
    }
    private static List<Seans> creatingSessions() throws IOException {
        List<Seans> seanslar = new ArrayList<>();
        Map<TypeOfSaloon, Map<TypeOfTicket, Double>> ticketPrices = new HashMap<>();
        ticketPrices.put(TypeOfSaloon.StandartSaloon, new HashMap<>());
        ticketPrices.get(TypeOfSaloon.StandartSaloon).put(TypeOfTicket.ADULT, 80.0);
        ticketPrices.get(TypeOfSaloon.StandartSaloon).put(TypeOfTicket.STUDENT, 60.0);

        ticketPrices.put(TypeOfSaloon.IMAXSaloon, new HashMap<>());
        ticketPrices.get(TypeOfSaloon.IMAXSaloon).put(TypeOfTicket.ADULT, 120.0);
        ticketPrices.get(TypeOfSaloon.IMAXSaloon).put(TypeOfTicket.STUDENT, 90.0);

        ticketPrices.put(TypeOfSaloon.GoldClassSaloon, new HashMap<>());
        ticketPrices.get(TypeOfSaloon.GoldClassSaloon).put(TypeOfTicket.ADULT, 180.0);
        ticketPrices.get(TypeOfSaloon.GoldClassSaloon).put(TypeOfTicket.STUDENT, 150.0);

        List<Film> films = databaseMovies();
        List<Salon> saloons = databaseSaloons();
        Scanner scanner = new Scanner(new File("C:/seanslar.txt"));

        while (scanner.hasNextLine()) {
            String[] sessionspart = scanner.nextLine().split(",");
            String filmNameStr = sessionspart[0];
            Film film = null;
            for (Film f : films) {
                if (f.getMovieName().equals(filmNameStr)) {
                    film = f;
                    break;
                }
            }
            if (film == null) {
                System.out.println("Film not found: " + filmNameStr);
                continue;
            }

            String salonIdStr = sessionspart[1];
            int salonId = Integer.parseInt(salonIdStr);
            Salon seansalon = null;
            for (Salon salon : saloons) {
                if (salon.getSalonID() == salonId) {
                    seansalon = salon;
                    break;
                }
            }
            if (seansalon == null) {
                System.out.println("Salon not found: " + salonIdStr);
                continue;
            }

            String dateStr = sessionspart[2];
            String[] datelocaltime = dateStr.split("-");
            int year = Integer.parseInt(datelocaltime[0]);
            int month = Integer.parseInt(datelocaltime[1]);
            int day = Integer.parseInt(datelocaltime[2]);
            int hour = Integer.parseInt(datelocaltime[3]);
            int minute = Integer.parseInt(datelocaltime[4]);
            LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);
            TypeOfSaloon typesaloon = TypeOfSaloon.valueOf(sessionspart[3]);
            Seans seans = new Seans(film, seansalon, dateTime , typesaloon);
            seanslar.add(seans);
        }

        return seanslar;

    }
}
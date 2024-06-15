import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

public class MainFrame extends JFrame implements ActionListener {
    private JButton registerButton;
    private JButton loginButton;
    private List<Film> films;
    private List<Salon> saloons;
    private LocalDateTime dateTime;
    private List<User> users;

    public MainFrame(List<Film> films, List<Salon> saloons, LocalDateTime dateTime) {
        this.films = films;
        this.saloons = saloons;
        this.dateTime = dateTime;

        setTitle("Cinema Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(2, 1));

        registerButton = new JButton("SIGN UP");
        registerButton.addActionListener(this);
        add(registerButton);

        loginButton = new JButton("LOGIN");
        loginButton.addActionListener(this);
        add(loginButton);
        updateButtonBackground(loginButton, true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == registerButton) {
            RegisterFrame registerFrame = new RegisterFrame();
            registerFrame.setVisible(true);

        } else if (e.getSource() == loginButton) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);

        }
    }

    private void updateButtonBackground(JButton button, boolean isOccupied) {
        if (isOccupied) {
            button.setBackground(Color.PINK);
        } else {
            button.setBackground(Color.MAGENTA);
        }
    }

    public static void main(String[] args) throws IOException {
        List<Film> films = databaseMovies();
        List<Salon> saloons = databaseSaloons();
        List<Seans> seans = creatingSessions();
        List<User> users = databaseUsers();
        LocalDateTime dateTime = LocalDateTime.of(2024, 10, 12, 15, 42);

        SwingUtilities.invokeLater(() -> {
            MainFrame mainFrame = new MainFrame(films, saloons, dateTime);
            mainFrame.setVisible(true);
        });
    }

    private static List<Film> databaseMovies() throws IOException {
        List<Film> films = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("C:/Users/LENOVO/Desktop/movies.txt"));
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

    private static List<User> databaseUsers() throws IOException {
        List<User> users = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader("C:/Users/LENOVO/Desktop/users.txt"));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] usersparts = line.split(",");
            String eposta = usersparts[0];
            String nickname = usersparts[1];
            String password = usersparts[2];
            String type = usersparts[3];
            TypeOfTicket typeOfTicket;
            if (type.equals("ADULT")) {
                typeOfTicket = TypeOfTicket.ADULT;
            } else {
                typeOfTicket = TypeOfTicket.STUDENT;
            }

            User user = new User(eposta, nickname, password, typeOfTicket);
            users.add(user);
        }
        return users;
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


    public class RegisterFrame extends JFrame implements ActionListener {
        private JTextField emailField;
        private JTextField nicknameField;
        private JPasswordField passwordField;
        private JPasswordField passwordVerifyField;
        private JRadioButton adultbutton;
        private JRadioButton studentbutton;
        private TypeOfTicket selectedType;

        public RegisterFrame() {
            setTitle("REGISTRATION FORM");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 200);
            setLayout(new GridLayout(8, 4)); // Dikey sıralama için 5 satır 2 sütun

            add(new JLabel("E-mail:"));
            emailField = new JTextField();
            add(emailField);

            add(new JLabel("Nickname:"));
            nicknameField = new JTextField();
            add(nicknameField);

            add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            add(passwordField);

            add(new JLabel("Password Repetition:"));
            passwordVerifyField = new JPasswordField();
            add(passwordVerifyField);

            JRadioButton ogrenciRadioButton = new JRadioButton(String.valueOf(TypeOfTicket.STUDENT));
            JRadioButton yetiskinRadioButton = new JRadioButton(String.valueOf(TypeOfTicket.ADULT));

            ButtonGroup group = new ButtonGroup();
            group.add(ogrenciRadioButton);
            group.add(yetiskinRadioButton);

            add(ogrenciRadioButton);
            add(yetiskinRadioButton);
            ogrenciRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedType = TypeOfTicket.STUDENT;
                }
            });

            yetiskinRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedType = TypeOfTicket.ADULT;
                }
            });
            JButton registerButton = new JButton("SIGN UP");
            registerButton.addActionListener(this);
            add(registerButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<User> users = null;
            try {
                users = databaseUsers();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String email = emailField.getText();
            String nickname = nicknameField.getText();
            String password = new String(passwordField.getPassword());
            String passwordVerify = new String(passwordVerifyField.getPassword());


            if (password.equals(passwordVerify)) {
                User user = new User(email, nickname, password, selectedType);
                users.add(user);
                UserServiceImplements userService = new UserServiceImplements();
                if (!userService.userExists(user.getEmail())) {
                    userService.writeToFile(user);
                    JOptionPane.showMessageDialog(this, "Your membership process has been completed!");
                    UserInformationService.setCurrentUser(user);

                    try {
                        openMainMenu();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    dispose(); // Pencereyi kapat
                } else {
                    JOptionPane.showMessageDialog(this, "There is already an account belonging to this e-mail address.");
                }
            } else {
                JOptionPane.showMessageDialog(this, "The passwords you entered do not match. Please try again.");
            }
        }

        private void openMainMenu() throws IOException {
            List<Film> films = databaseMovies();
            List<Salon> saloons = databaseSaloons();
            Scanner scannerInput = new Scanner(System.in);

            SwingUtilities.invokeLater(() -> {
                MainMenuFrame mainMenuFrame = new MainMenuFrame(films, saloons, scannerInput);
                mainMenuFrame.setVisible(true);
            });
        }
    }


    public class LoginFrame extends JFrame implements ActionListener {
        private JTextField emailField;
        private JPasswordField passwordField;

        public LoginFrame() {
            setTitle("LOG-IN");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(300, 150);
            setLayout(new GridLayout(3, 2)); // Dikey sıralama için 3 satır 2 sütun

            add(new JLabel("E-mail:"));
            emailField = new JTextField();
            add(emailField);

            add(new JLabel("Password:"));
            passwordField = new JPasswordField();
            add(passwordField);

            JButton loginButton = new JButton("LOGIN");
            loginButton.addActionListener(this);
            add(loginButton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            List<User> users = null;
            try {
                users = databaseUsers();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            String email = emailField.getText();
            String password = new String(passwordField.getPassword());
            Scanner scanner = new Scanner(System.in);


            UserServiceImplements userService = new UserServiceImplements();
            try {
                if (userService.userExists(email)) {
                    if (userService.loginUser(email, password)) {
                        if (email.equals("Admin") && password.equals("admin123")) {
                            JOptionPane.showMessageDialog(this , "The administrator login is successful!");
                            AdminFrame adminFrame = new AdminFrame(films , saloons , creatingSessions() ,scanner);
                            adminFrame.setVisible(true);
                        }
                        else {
                            JOptionPane.showMessageDialog(this, "The entry is successful!");
                            for (int i = 0; i < users.size(); i++) {
                                if (users.get(i).getEmail().equals(email)) {
                                    UserInformationService.setCurrentUser(users.get(i));
                                    break;
                                } else
                                    continue;
                            }
                            openMainMenu();
                            dispose();
                        } } else {
                        JOptionPane.showMessageDialog(this, "Wrong email or password! Please try again.");
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "An account belonging to this e-mail address could not be found. Please register first.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        private void openMainMenu() throws IOException {
            List<Film> films = databaseMovies();
            List<Salon> saloons = databaseSaloons();
            Scanner scannerInput = new Scanner(System.in);

            SwingUtilities.invokeLater(() -> {
                MainMenuFrame mainMenuFrame = new MainMenuFrame(films, saloons, scannerInput);
                mainMenuFrame.setVisible(true);
            });
        }
    }
    public class AdminFrame extends JFrame implements ActionListener {
        private List<Film> films;
        private List<Salon> saloons;
        private Scanner scanner;
        private List<Seans> seans;

        public AdminFrame(List<Film> films,List<Salon> saloons,List<Seans> seans,Scanner scanner) {
            this.films = films;
            this.saloons = saloons;
            this.seans = seans;
            this.scanner = scanner;
            setTitle("ADMIN PANEL");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400,300);
            setLayout(new GridLayout(3,1));

            JButton addMovieButton = new JButton("Add New Movie");
            addMovieButton.addActionListener(this);
            add(addMovieButton);

            JButton deleteMovieButton = new JButton("Delete Movie");
            deleteMovieButton.addActionListener(this);
            add(deleteMovieButton);
            JButton addSeansButton = new JButton("Add New Session");
            addSeansButton.addActionListener(this);
            add(addSeansButton);
            JButton deleteSeansButton = new JButton("Delete Session");
            deleteSeansButton.addActionListener(this);
            add(deleteSeansButton);
            JButton reportsofTicketSold = new JButton("Reports Of Tickets Sold");
            reportsofTicketSold.addActionListener(this);
            add(reportsofTicketSold);

        }


        public void actionPerformed(ActionEvent e) {
            AdminImplements adminImplements = new AdminImplements();
            if (e.getActionCommand().equals("Add New Movie")) {
                try {
                    adminImplements.addNewFilm();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if (e.getActionCommand().equals("Delete Movie")) {
                try {
                    adminImplements.deleteFilm();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if(e.getActionCommand().equals("Add New Session")) {
                try {
                    adminImplements.addNewMovieSession();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if(e.getActionCommand().equals("Delete Session")) {
                try {
                    adminImplements.deleteSession();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

            if(e.getActionCommand().equals("Reports Of Tickets Sold")) {
                try {
                    adminImplements.reportsofticketsold();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }

        }
    }


    public class MainMenuFrame extends JFrame implements ActionListener {
        private List<Film> films;
        private List<Salon> saloons;
        private Scanner scannerInput;


        public MainMenuFrame(List<Film> films, List<Salon> saloons, Scanner scannerInput) {
            this.films = films;
            this.saloons = saloons;
            this.scannerInput = scannerInput;

            setTitle("Ana Menü");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLayout(new GridLayout(3, 1));

            JButton moviesButton = new JButton("Movies in Theaters");
            moviesButton.addActionListener(this);
            add(moviesButton);

            JButton sessionsButton = new JButton("Sessions and Ticket Purchase");
            sessionsButton.addActionListener(this);
            add(sessionsButton);

            JButton previousticketbutton = new JButton("Past Purchases");
            previousticketbutton.addActionListener(this);
            add(previousticketbutton);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("Movies in Theaters")) {
                try {
                    MovieDetailsFrame movieDetailsFrame = new MovieDetailsFrame();
                    movieDetailsFrame.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            } else if (e.getActionCommand().equals("Sessions and Ticket Purchase")) {

                try {
                    SessionsFrame sessionsFrame = new SessionsFrame();
                    sessionsFrame.setVisible(true);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

            } else if (e.getActionCommand().equals("Past Purchases")) {
                PastPurchasesFrame pastPurchasesFrame = new PastPurchasesFrame();
                pastPurchasesFrame.setVisible(true);

            }
        }


    }

    public class PastPurchasesFrame extends JFrame {
        private void openMainMenu() throws IOException {
            List<Film> films = databaseMovies();
            List<Salon> saloons = databaseSaloons();
            Scanner scannerInput = new Scanner(System.in);

            SwingUtilities.invokeLater(() -> {
                MainMenuFrame mainMenuFrame = new MainMenuFrame(films, saloons, scannerInput);
                mainMenuFrame.setVisible(true);
            });
        }

        public PastPurchasesFrame() {
            setTitle("Past Purchases");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLayout(new BorderLayout());

            JTextArea pastPurchasesArea = new JTextArea();
            pastPurchasesArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(pastPurchasesArea);
            add(scrollPane, BorderLayout.CENTER);

            JButton backButton = new JButton("BACK");
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        openMainMenu();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            backButton.setBackground(Color.RED);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(new Font("Arial", Font.BOLD, 16));

            add(backButton, BorderLayout.SOUTH);

            loadPastPurchases(pastPurchasesArea);

            setVisible(true);
        }

        private void loadPastPurchases(JTextArea textArea) {
            UserServiceImplements userService = new UserServiceImplements();

            try {
                File file = new File("past_purchases.txt");
                if (file.exists()) {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;
                    String[] purchaseparts;
                    while ((line = reader.readLine()) != null) {
                        purchaseparts = line.split(",");
                        if (purchaseparts[0].equals("Kullanıcı: " + UserInformationService.getCurrentUser().getNickname())) {
                            textArea.append(line + "\n"); }
                    }
                    reader.close();
                } else {
                    textArea.setText("You do not have any past ticket purchases.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class MovieDetailsFrame extends JFrame implements ActionListener {
        private void openMainMenu() throws IOException {
            List<Film> films = databaseMovies();
            List<Salon> saloons = databaseSaloons();
            Scanner scannerInput = new Scanner(System.in);

            SwingUtilities.invokeLater(() -> {
                MainMenuFrame mainMenuFrame = new MainMenuFrame(films, saloons, scannerInput);
                mainMenuFrame.setVisible(true);
            });
        }
        public MovieDetailsFrame() throws IOException {
            setTitle("Movies");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLayout(new GridLayout(0, 1));

            Scanner scanner = new Scanner(new File("C:/Users/LENOVO/Desktop/movies.txt"));
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                String movieName = parts[0];
                JButton movieButton = new JButton(movieName);
                movieButton.addActionListener(this);
                add(movieButton);
            }
            scanner.close();
            JButton backButton = new JButton("BACK");
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        openMainMenu();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });

            backButton.setBackground(Color.RED);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(new Font("Arial", Font.BOLD, 16));

            add(backButton);

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String movieName = e.getActionCommand();
            try {
                showMovieDetails(movieName);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }


        private void showMovieDetails(String movieName) throws IOException {
            JFrame detailsFrame = new JFrame(movieName + " Details");
            detailsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            detailsFrame.setSize(300, 200);
            detailsFrame.setLayout(new BorderLayout());

            JTextArea textArea = new JTextArea();
            textArea.setEditable(false);

            Scanner scanner = new Scanner(new File("C:/Users/LENOVO/Desktop/movies.txt"));
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts[0].equals(movieName)) {
                    textArea.append("Movie Name: " + parts[0] + "\n");
                    textArea.append("Director: " + parts[1] + "\n");
                    textArea.append("Movie Duration: " + parts[2] + " minutes\n");
                    textArea.append("Movie Type: " + parts[3] + "\n");
                    break;
                }
            }
            scanner.close();

            JScrollPane scrollPane = new JScrollPane(textArea);
            detailsFrame.add(scrollPane, BorderLayout.CENTER);

            detailsFrame.setVisible(true);
        }


    }

    public class SessionsFrame extends JFrame implements ActionListener {
        private JComboBox<String> movieComboBox;

        public SessionsFrame() throws IOException {
            setTitle("Sessions");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(400, 300);
            setLayout(new BorderLayout());

            List<String> movieNames = new ArrayList<>();
            Scanner scanner = new Scanner(new File("C:/Users/LENOVO/Desktop/movies.txt"));
            while (scanner.hasNextLine()) {
                String[] parts = scanner.nextLine().split(",");
                String movieName = parts[0];
                movieNames.add(movieName);
            }
            scanner.close();
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            movieComboBox = new JComboBox<>(movieNames.toArray(new String[0]));
            movieComboBox.addActionListener(this);
            add(movieComboBox, BorderLayout.NORTH);
            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.PLAIN, 16));
            backButton.setPreferredSize(new Dimension(100, 40));
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        openMainMenu();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            buttonPanel.add(backButton);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String selectedMovieName = (String) movieComboBox.getSelectedItem();
            if (selectedMovieName != null) {
                try {
                    showSessions(selectedMovieName);

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        private void showSessions(String selectedMovieName) throws IOException {
            List<Seans> selectedMovieSessions = new ArrayList<>();
            List<Seans> seanslar = creatingSessions();
            for (Seans seans : seanslar) {
                if (seans.getMovieName().getMovieName().equals(selectedMovieName)) {
                    selectedMovieSessions.add(seans);
                }
            }

            JFrame sessionsFrame = new JFrame(selectedMovieName + " Sessions");
            sessionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sessionsFrame.setSize(400, 300);
            sessionsFrame.setLayout(new BorderLayout());

            JPanel sessionsPanel = new JPanel();
            sessionsPanel.setLayout(new GridLayout(0, 1));

            for (Seans seans : selectedMovieSessions) {
                int salonID = seans.getSalonID().getSalonID();
                String salonTuru = String.valueOf(seans.getSalonID().getTypeofSaloon());
                JButton sessionButton = new JButton("Saloon " + salonID + " Type: " + salonTuru + " - " + seans.getDate());
                sessionButton.addActionListener(e -> {
                    BiletSatisEkrani biletSatisEkrani = null;
                    try {
                        biletSatisEkrani = new BiletSatisEkrani(seans);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    biletSatisEkrani.setVisible(true);
                });
                sessionsPanel.add(sessionButton);
            }

            JButton backButton = new JButton("BACK");
            backButton.setBackground(Color.RED);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(new Font("Arial", Font.BOLD, 16));
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    sessionsFrame.dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(backButton);

            sessionsFrame.add(sessionsPanel, BorderLayout.CENTER);
            sessionsFrame.add(buttonPanel, BorderLayout.SOUTH);

            sessionsFrame.setVisible(true);
        }
        private void openMainMenu() throws IOException {
            List<Film> films = databaseMovies();
            List<Salon> saloons = databaseSaloons();
            Scanner scannerInput = new Scanner(System.in);

            SwingUtilities.invokeLater(() -> {
                MainMenuFrame mainMenuFrame = new MainMenuFrame(films, saloons, scannerInput);
                mainMenuFrame.setVisible(true);
            });
        }

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
        Scanner scanner = new Scanner(new File("C:/Users/LENOVO/Desktop/seanslar.txt"));
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


    public class SeanslarArasiKoltukSecimi extends JFrame {

        private Map<Seans, Map<Integer, Boolean>> koltukDurumu;
        private JLabel selectedSeatsLabel;
        private JLabel totalPriceLabel;
        private Seans seans;

        private double totalRevenue;
        private double totalPrice;
        private int selectedSeatsCount;
        private int selectedTicketNumber;
        private List<Integer> selectedSeatNumbers;

        public SeanslarArasiKoltukSecimi(Seans seans, int selectedTicketNumber) {
            this.seans = seans;
            this.totalRevenue = 0.0;
            this.totalPrice = 0.0;
            this.selectedSeatsCount = 0;
            this.selectedTicketNumber = selectedTicketNumber;
            this.selectedSeatNumbers = new ArrayList<>();

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

            setTitle("Session " + seans.getDate() + " Seat Selection");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(800, 600);
            setLayout(new BorderLayout());

            JLabel screenLabel = new JLabel("SCREEN", SwingConstants.CENTER);
            screenLabel.setFont(new Font("Arial", Font.BOLD, 24));
            screenLabel.setOpaque(true);
            screenLabel.setBackground(Color.LIGHT_GRAY);
            add(screenLabel, BorderLayout.NORTH);

            JPanel seatPanel = new JPanel(new GridLayout(0, 5));

            koltukDurumu = loadSeatStatus(seans);

            for (final int koltukNumarasi : koltukDurumu.get(seans).keySet()) {
                JButton seatButton = new JButton("Seat: " + koltukNumarasi);
                updateButtonBackground(seatButton, koltukDurumu.get(seans).get(koltukNumarasi));

                seatButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        JButton clickedButton = (JButton) e.getSource();
                        int koltukNumarasi = Integer.parseInt(clickedButton.getText().substring(6));
                        boolean isOccupied = koltukDurumu.get(seans).get(koltukNumarasi);

                        if (isOccupied && !selectedSeatNumbers.contains(koltukNumarasi)) {
                            JOptionPane.showMessageDialog(SeanslarArasiKoltukSecimi.this, "This seat has already been selected.", "ERROR", JOptionPane.ERROR_MESSAGE); //KOLTUK ÖNCEDEN SEÇİLİ
                        } else if (isOccupied) { //O AN SEÇİLMİŞ VE İPTAL EDİLİYOR
                            koltukDurumu.get(seans).put(koltukNumarasi, false);
                            updateButtonBackground(clickedButton, false);
                            selectedSeatNumbers.remove(Integer.valueOf(koltukNumarasi));
                            selectedSeatsCount--;
                            totalPrice -= ticketPrices.get(seans.getTypeOfSaloon()).get(getUserInformation().getTypeOfTicket());
                        } else { //SEÇİLME ANI
                            koltukDurumu.get(seans).put(koltukNumarasi, true);
                            updateButtonBackground(clickedButton, true);
                            selectedSeatNumbers.add(koltukNumarasi);
                            selectedSeatsCount++;
                            totalPrice += ticketPrices.get(seans.getTypeOfSaloon()).get(getUserInformation().getTypeOfTicket());
                        }

                        selectedSeatsLabel.setText("Number of Selected Seats: " + selectedSeatsCount);
                        totalPriceLabel.setText("Total Price: " + totalPrice + "₺");

                        saveSeatStatus(seans, koltukDurumu.get(seans));
                    }
                });

                seatPanel.add(seatButton);
            }

            JButton confirmButton = new JButton("Confirm");
            confirmButton.setFont(new Font("Arial", Font.PLAIN, 24));
            confirmButton.setPreferredSize(new Dimension(200, 80));
            confirmButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (selectedSeatsCount != selectedTicketNumber) {
                        JOptionPane.showMessageDialog(SeanslarArasiKoltukSecimi.this, "Please select " + selectedTicketNumber + " seats.", "ERROR", JOptionPane.ERROR_MESSAGE);
                    } else {
                        User user = getUserInformation();
                        totalRevenue += totalPrice;
                        JOptionPane.showMessageDialog(SeanslarArasiKoltukSecimi.this, "Your seat selections have been confirmed.\n", "Confirmation", JOptionPane.INFORMATION_MESSAGE);
                        dispose();
                        new OdemePenceresi(user, seans, selectedSeatNumbers, totalPrice , selectedTicketNumber).setVisible(true);
                    }
                }
            });

            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.PLAIN, 24));
            backButton.setPreferredSize(new Dimension(200, 80));
            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        new BiletSatisEkrani(seans).setVisible(true);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });


            selectedSeatsLabel = new JLabel("Number of Selected Seats: " + selectedSeatsCount);
            totalPriceLabel = new JLabel("Total Price: " + totalPrice + "₺");

            add(seatPanel, BorderLayout.CENTER);
            JPanel infoPanel = new JPanel(new GridLayout(2, 1));
            infoPanel.add(selectedSeatsLabel);
            infoPanel.add(totalPriceLabel);
            add(infoPanel, BorderLayout.EAST);
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(confirmButton);
            buttonPanel.add(backButton);
            add(buttonPanel, BorderLayout.SOUTH);
        }

        private void updateButtonBackground(JButton button, boolean isOccupied) {
            if (isOccupied) {
                button.setBackground(Color.RED);
            } else {
                button.setBackground(Color.GREEN);
            }
        }

        private void saveSeatStatus(Seans seans, Map<Integer, Boolean> seatStatus) {
            try {
                File file = new File("seat_status_" + seans.getDate() + ".txt");
                FileWriter writer = new FileWriter(file);
                for (Map.Entry<Integer, Boolean> entry : seatStatus.entrySet()) {
                    writer.write(entry.getKey() + "," + entry.getValue() + "\n");
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private Map<Seans, Map<Integer, Boolean>> loadSeatStatus(Seans seans) {
            Map<Seans, Map<Integer, Boolean>> seatStatus = new HashMap<>();
            try {
                File file = new File("seat_status_" + seans.getDate() + ".txt");
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line;
                Map<Integer, Boolean> seansSeatStatus = new HashMap<>();
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(",");
                    int seatNumber = Integer.parseInt(parts[0]);
                    boolean isOccupied = Boolean.parseBoolean(parts[1]);
                    seansSeatStatus.put(seatNumber, isOccupied);
                }
                reader.close();
                seatStatus.put(seans, seansSeatStatus);
            } catch (IOException e) {
                Map<Integer, Boolean> defaultSeatStatus = new HashMap<>();
                for (int j = 1; j <= seans.getSalonID().getSeatCounter(); j++) {
                    defaultSeatStatus.put(j, false);
                }
                seatStatus.put(seans, defaultSeatStatus);
            }
            return seatStatus;
        }
        private User getUserInformation() {
            return UserInformationService.getCurrentUser();
        }
    }



    public class BiletSatisEkrani extends JFrame {

        private TypeOfTicket selectedTypeOfTicket;
        private Seans seans;

        public BiletSatisEkrani(Seans seans) throws IOException {
            this.seans = seans;
            List<User> users = databaseUsers();
            Map<TypeOfSaloon, Map<TypeOfTicket, Double>> ticketPrices = new HashMap<>();
            ticketPrices.put(TypeOfSaloon.StandartSaloon, new HashMap<>());
            ticketPrices.get(TypeOfSaloon.StandartSaloon).put(TypeOfTicket.ADULT, 80.0);
            double AdultStandart = ticketPrices.get(TypeOfSaloon.StandartSaloon).get(TypeOfTicket.ADULT);
            ticketPrices.get(TypeOfSaloon.StandartSaloon).put(TypeOfTicket.STUDENT, 60.0);
            double StudentStandart = ticketPrices.get(TypeOfSaloon.StandartSaloon).get(TypeOfTicket.STUDENT);
            ticketPrices.put(TypeOfSaloon.IMAXSaloon, new HashMap<>());
            ticketPrices.get(TypeOfSaloon.IMAXSaloon).put(TypeOfTicket.ADULT, 120.0);
            double AdultImax = ticketPrices.get(TypeOfSaloon.IMAXSaloon).get(TypeOfTicket.ADULT);
            ticketPrices.get(TypeOfSaloon.IMAXSaloon).put(TypeOfTicket.STUDENT, 90.0);
            double StudentImax = ticketPrices.get(TypeOfSaloon.IMAXSaloon).get(TypeOfTicket.STUDENT);
            ticketPrices.put(TypeOfSaloon.GoldClassSaloon, new HashMap<>());
            ticketPrices.get(TypeOfSaloon.GoldClassSaloon).put(TypeOfTicket.ADULT, 180.0);
            double AdultGoldClass = ticketPrices.get(TypeOfSaloon.GoldClassSaloon).get(TypeOfTicket.ADULT);
            ticketPrices.get(TypeOfSaloon.GoldClassSaloon).put(TypeOfTicket.STUDENT, 150.0);
            double StudentGoldClass = ticketPrices.get(TypeOfSaloon.GoldClassSaloon).get(TypeOfTicket.STUDENT);

            setTitle("Ticket Sales Screen");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 300);
            setLayout(new GridLayout(0, 1));

            UserInformationService.setCurrentUser(getUserInformation());
            Integer[] sayilar = {1, 2, 3, 4, 5, 6, 7, 8, 9};
            JComboBox<Integer> ticketNumberComboBox = new JComboBox<>(sayilar);
            final int[] selectedTicketNumber = new int[1];
            ticketNumberComboBox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    selectedTicketNumber[0] = (int) ticketNumberComboBox.getSelectedItem();
                }
            });
            add(ticketNumberComboBox);
            JRadioButton ogrenciRadioButton = new JRadioButton("Student:  " + ticketPrices.get(seans.getTypeOfSaloon()).get(TypeOfTicket.STUDENT) + "₺");
            JRadioButton yetiskinRadioButton = new JRadioButton("Adult:  " + ticketPrices.get(seans.getTypeOfSaloon()).get(TypeOfTicket.ADULT) + "₺");

            ButtonGroup group = new ButtonGroup();
            group.add(ogrenciRadioButton);
            group.add(yetiskinRadioButton);

            add(ogrenciRadioButton);
            add(yetiskinRadioButton);
            ogrenciRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {

                    if (UserInformationService.getCurrentUser().getTypeOfTicket().equals(TypeOfTicket.STUDENT)) {
                        selectedTypeOfTicket = TypeOfTicket.STUDENT;
                    } else {
                        JOptionPane.showMessageDialog(BiletSatisEkrani.this, "SELECT THE TICKET TYPE YOU ENTERED ON THE REGISTRATION SCREEN");
                    }
                }
            });

            yetiskinRadioButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (UserInformationService.getCurrentUser().getTypeOfTicket().equals(TypeOfTicket.ADULT)) {
                        selectedTypeOfTicket = TypeOfTicket.ADULT;
                    } else {
                        JOptionPane.showMessageDialog(BiletSatisEkrani.this, "SELECT THE TICKET TYPE YOU ENTERED ON THE REGISTRATION SCREEN");
                    }
                }
            });

            JButton satinAlButton = new JButton("SELL");
            satinAlButton.setFont(new Font("Arial", Font.PLAIN, 18));
            satinAlButton.setPreferredSize(new Dimension(200, 80));
            satinAlButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    boolean ogrenciSecildi = ogrenciRadioButton.isSelected();
                    boolean yetiskinSecildi = yetiskinRadioButton.isSelected();
                    if (ogrenciSecildi || yetiskinSecildi) {

                        SeanslarArasiKoltukSecimi seanslarArasiKoltukSecimi = new SeanslarArasiKoltukSecimi(seans , selectedTicketNumber[0]  );
                        seanslarArasiKoltukSecimi.setVisible(true); }
                    else
                        JOptionPane.showMessageDialog(BiletSatisEkrani.this , "PLEASE SELECT ONE OF THE OPTIONS.");
                }
            });
            add(satinAlButton);

            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.BOLD, 16));
            backButton.setBackground(Color.RED);
            backButton.setForeground(Color.WHITE);
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    try {
                        showSessions(seans.getMovieName().getMovieName());
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            });
            add(backButton);

            setVisible(true);
        }

        private User getUserInformation() {
            return UserInformationService.getCurrentUser();
        }

        private void showSessions(String selectedMovieName) throws IOException {
            List<Seans> selectedMovieSessions = new ArrayList<>();
            List<Seans> seanslar = creatingSessions();
            for (Seans seans : seanslar) {
                if (seans.getMovieName().getMovieName().equals(selectedMovieName)) {
                    selectedMovieSessions.add(seans);
                }
            }

            JFrame sessionsFrame = new JFrame(selectedMovieName + " Sessions");
            sessionsFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            sessionsFrame.setSize(400, 300);
            sessionsFrame.setLayout(new BorderLayout());

            JPanel sessionsPanel = new JPanel();
            sessionsPanel.setLayout(new GridLayout(0, 1));

            for (Seans seans : selectedMovieSessions) {
                int salonID = seans.getSalonID().getSalonID();
                String salonTuru = String.valueOf(seans.getSalonID().getTypeofSaloon());
                JButton sessionButton = new JButton("Saloon " + salonID + " Type: " + salonTuru + " - " + seans.getDate());
                sessionButton.addActionListener(e -> {
                    BiletSatisEkrani biletSatisEkrani = null;
                    try {
                        biletSatisEkrani = new BiletSatisEkrani(seans);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    biletSatisEkrani.setVisible(true);
                });
                sessionsPanel.add(sessionButton);
            }

            JButton backButton = new JButton("BACK");
            backButton.setBackground(Color.RED);
            backButton.setForeground(Color.WHITE);
            backButton.setFont(new Font("Arial", Font.BOLD, 16));
            backButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    sessionsFrame.dispose();
                }
            });

            JPanel buttonPanel = new JPanel();
            buttonPanel.add(backButton);

            sessionsFrame.add(sessionsPanel, BorderLayout.CENTER);
            sessionsFrame.add(buttonPanel, BorderLayout.SOUTH);

            sessionsFrame.setVisible(true);
        }
    }
    public class OdemePenceresi extends JFrame {
        private List<Integer> selectedSeatNumbers;
        private int selectedTicketNumber;

        private List<Ticket> tickets;
        private double totalPrice;
        private User user;
        private Seans seans;

        public OdemePenceresi(User user, Seans seans, List<Integer> selectedSeatNumbers, double totalPrice, int selectedTicketNumber) {
            this.user = user;
            this.seans = seans;
            this.selectedSeatNumbers = selectedSeatNumbers;
            this.totalPrice = totalPrice;
            this.selectedTicketNumber = selectedTicketNumber;

            setTitle("Ödeme Ekranı");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(500, 400);
            setLayout(new BorderLayout());

            JTextArea ticketDetailsArea = new JTextArea();
            ticketDetailsArea.setEditable(false);
            ticketDetailsArea.setFont(new Font("Arial", Font.PLAIN, 16));
            ticketDetailsArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            String movieName = seans.getMovieName().getMovieName();
            String seansDate = seans.getDate().toString();
            String salonInfo = seans.getSalonID().getSalonID() + " - " + seans.getTypeOfSaloon();

            StringBuilder details = new StringBuilder("Tickets in your cart:\n");
            details.append("Film: ").append(movieName).append("\n");
            details.append("Session: ").append(seansDate).append("\n");
            details.append("Saloon: ").append(salonInfo).append("\n\n");
            for (int seatNumber : selectedSeatNumbers) {
                details.append("Seat: ").append(seatNumber).append("\n");
            }
            details.append("\nTotal Price: ").append(totalPrice).append("₺");

            ticketDetailsArea.setText(details.toString());

            JScrollPane scrollPane = new JScrollPane(ticketDetailsArea);
            scrollPane.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK, 2), "Ticket Details"));
            add(scrollPane, BorderLayout.CENTER);

            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            JButton completePurchaseButton = new JButton("SELL");
            completePurchaseButton.setFont(new Font("Arial", Font.BOLD, 20));
            completePurchaseButton.setPreferredSize(new Dimension(200, 80));
            completePurchaseButton.setBackground(new Color(34, 139, 34));
            completePurchaseButton.setForeground(Color.WHITE);
            completePurchaseButton.setFocusPainted(false);
            completePurchaseButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            completePurchaseButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    createTickets(user, selectedSeatNumbers);
                    JOptionPane.showMessageDialog(OdemePenceresi.this, "Your purchase has been completed.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                }
            });

            JButton backButton = new JButton("BACK");
            backButton.setFont(new Font("Arial", Font.BOLD, 20));
            backButton.setPreferredSize(new Dimension(200, 80));
            backButton.setBackground(new Color(255, 69, 0)); // Kırmızı renk
            backButton.setForeground(Color.WHITE);
            backButton.setFocusPainted(false);
            backButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2));

            backButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dispose();
                    new SeanslarArasiKoltukSecimi(seans , selectedTicketNumber ).setVisible(true);
                }
            });

            buttonPanel.add(completePurchaseButton);
            buttonPanel.add(backButton);
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
            add(buttonPanel, BorderLayout.SOUTH);

            setVisible(true);
        }

        private void createTickets(User user, List<Integer> seatNumbers) {
            List<Ticket> tickets = new ArrayList<>();
            try {
                FileWriter writer = new FileWriter("past_purchases.txt", true);
                for (int seatNumber : seatNumbers) {
                    TypeOfTicket typeOfTicket = user.getTypeOfTicket();
                    Ticket ticket = new Ticket(user, seans, seatNumber, typeOfTicket);
                    tickets.add(ticket);
                    String ticketInfo = "Kullanıcı: " + user.getNickname() +
                            ", Film: " + seans.getMovieName().getMovieName() +
                            ", Salon ID: " + seans.getSalonID().getSalonID() +
                            ", Tür: " + seans.getTypeOfSaloon() +
                            ", Tarih: " + seans.getDate() +
                            ", Koltuk Numarası: " + seatNumber +
                            "," + typeOfTicket;
                    writer.write(ticketInfo + "\n");
                    System.out.println("Yeni bilet oluşturuldu: " + ticketInfo);
                }
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




}
import java.io.*;

public class UserServiceImplements implements UserServices {

    public boolean loginUser(String eposta, String password) { // EPOSTA İLE ŞİFREYİ KARŞILAŞTIRIYOR.ŞİFRE DOĞRUYSA GİRİŞ YAPILIYOR.
        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/LENOVO/Desktop/users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 0 && parts[0].equals(eposta)) {
                    if (parts.length >= 3 && parts[2].equals(password)) {
                        reader.close();
                        return true;
                    } else {
                        reader.close();
                        return false;
                    }
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return false;
    }



    public void writeToFile(User user) { //Oluşturulan user'ı dosyaya yazdırır
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("C:/Users/LENOVO/Desktop/users.txt", true));
            writer.write(user.getEmail() + "," + user.getNickname() + "," + user.getPassword()+","+ user.getTypeOfTicket());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }
    }


    public boolean userExists(String email) { //Girilen emaile ait bir hesap var mı yok mu kontrol eder


        try {
            BufferedReader reader = new BufferedReader(new FileReader("C:/Users/LENOVO/Desktop/users.txt"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(email)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return false;

    }

}

public interface UserServices {

    boolean userExists(String email);
    boolean loginUser(String email , String password);
    void writeToFile(User user);
}

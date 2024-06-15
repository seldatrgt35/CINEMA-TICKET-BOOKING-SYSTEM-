public class UserInformationService {
    private static User currentUser; // Mevcut kullanıcıyı saklamak için bir alan

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}

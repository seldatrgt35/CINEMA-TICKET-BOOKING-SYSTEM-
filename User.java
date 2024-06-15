class User {
    private String email;
    private String nickname;
    private String password;
    private TypeOfTicket typeOfTicket;

    public User(String email, String nickname, String password,TypeOfTicket typeOfTicket) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
        this.typeOfTicket = typeOfTicket;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public TypeOfTicket getTypeOfTicket() {
        return typeOfTicket;
    }
}

package DTO;

public class Utente {
    private String Username;
    private String email;
    private char[] pw;

    public Utente(String Username, String email, char[] pw) {
        this.Username = Username;
        this.email = email;
        this.pw = pw;
    }

    public String getUsername() {

        return this.Username;
    }

    public void setUsername(String username) {

        this.Username = username;
    }

    public String getEmail() {

        return this.email;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public char[] getPw() {

        return this.pw;
    }

    public void setPw(char[] pw) {

        this.pw = pw;
    }

}


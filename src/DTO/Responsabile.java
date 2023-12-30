package DTO;

public class Responsabile {
    private String matricola;
    private String nome;
    private String cognome;
    private String codfiscale;
    private String telefono;
    private String email;
    private char[] pw;
    private Sede codS_fk;

    public Responsabile(String matricola, String nome, String cognome, String codfis, String tel, String email, char[] pw,  Sede s) {

        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.codfiscale = codfis;
        this.telefono = tel;
        this.email = email;
        this.pw = pw;
        this.codS_fk = s;

    }

    public String getMatricola() {
        return this.matricola;
    }

    public void setMatricola(String matricola) {
        this.matricola = matricola;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return this.cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public String getCodfiscale() {
        return this.codfiscale;
    }

    public void setCodfiscale(String codfiscale) {
        this.codfiscale = codfiscale;
    }

    public String getTelefono() {
        return this.telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
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

    public Sede getSede() {
        return this.codS_fk;
    }

    public void setSede(Sede codS_fk) {
        this.codS_fk = codS_fk;
    }

}
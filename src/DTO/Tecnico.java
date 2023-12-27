package DTO;

public class Tecnico {

    private String matricola;
    private String nome;
    private String cognome;
    private String email;
    private String codfiscale;
    private String telefono;
    private Laboratorio codl_fk;
    private Team codTeam_fk;

    public Tecnico(String matricola, String nome, String cognome, String codfis, String tel, String email, Laboratorio lab, Team t) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.codfiscale = codfis;
        this.telefono = tel;
        this.email = email;
        this.codl_fk = lab;
        this.codTeam_fk = t;
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

    public Laboratorio getLaboratorio() {
        return this.codl_fk;
    }

    public void setLaboratorio(Laboratorio laboratorio) {
        this.codl_fk = laboratorio;
    }

    public Team getTeam() {
        return this.codTeam_fk;
    }

    public void setTeam(Team team) {
        this.codTeam_fk = team;
    }

    public String toString() {
        return matricola + " " + nome + " " + cognome;
    }
}



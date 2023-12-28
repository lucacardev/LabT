package DTO;

import java.util.Objects;

public class Tecnico {

    private String matricola;
    private String nome;
    private String cognome;
    private String codfiscale;
    private String telefono;

    private String email;
    private Laboratorio codl_fk;
    private Team codTeam_fk;

    public Tecnico(String matricola, String nome, String cognome, String codfis,  String tel, String email, Laboratorio lab, Team t) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.codfiscale = codfis;
        this.email = email;
        this.telefono = tel;
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

    public Laboratorio getLaboratorio() {

        return this.codl_fk;
    }

    public void setLaboratorio(Laboratorio laboratorio) {

        this.codl_fk = laboratorio;
    }

    public String getEmail() {

        return this.email;
    }

    public void setEmail(Laboratorio laboratorio) {

        this.email = email;
    }

    public Team getCodTeam() {
        return this.codTeam_fk;
    }

    public void setTeam(Team Team) {
        this.codTeam_fk = Team;
    }

    public String toString() {
        return matricola + " " + nome + " " + cognome;
    }

    //Questi due metodi ci permettono di confrontare l'uguaglianza tra tecnici in base alla matricola


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tecnico tecnico = (Tecnico) o;
        return Objects.equals(matricola, tecnico.matricola);
    }

    @Override
    public int hashCode() {
        return Objects.hash(matricola);
    }


}

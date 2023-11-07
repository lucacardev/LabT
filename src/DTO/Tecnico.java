package DTO;

public class Tecnico {

    private String matricola;
    private String nome;
    private String cognome;
    private String codfiscale;
    private String telefono;
    private Laboratorio codl_fk;
    private DTO.Laboratorio Laboratorio;
    private  Team codTeam_fk;
    private DTO.Team Team;

    public Tecnico (String matricola,String nome,String cognome,String codfis,String tel,Laboratorio lab,Team t) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.codfiscale = codfis;
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

        return this.Laboratorio;
    }

    public void setLaboratorio(Laboratorio laboratorio) {

        this.codl_fk = laboratorio;
    }

    public Team getCodTeam () {
        return this.Team;
    }

    public void setTeam(Team Team) {
        this.codTeam_fk = Team;
    }
}
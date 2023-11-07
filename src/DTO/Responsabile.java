package DTO;

public class Responsabile {
    private String matricola;
    private String nome;
    private String cognome;
    private String codfiscale;
    private String telefono;
    private String email;
    private Sede codS_fk;
    private DTO.Sede Sede;

    public Responsabile(String matricola,String nome,String cognome,String codfis,String tel,String email,Sede s) {
        this.matricola = matricola;
        this.nome = nome;
        this.cognome = cognome;
        this.codfiscale = codfis;
        this.telefono = tel;
        this.email = email;
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

    public Sede getSede() {
        return this.Sede;
    }

    public void setSede(Sede sede) {
        this.codS_fk = sede;
    }
}

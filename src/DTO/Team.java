package DTO;

public class Team {
    private String codTeam;
    private String nome;
    private String descrizione;
    private String matricolaL;
    private Integer n_tecnici;
    private Responsabile codR_fk;

    public Team(String codTeam,String nome,String des,String matricolaLeader,Integer n_tecnici, Responsabile res) {

        this.codTeam = codTeam;
        this.nome = nome;
        this.descrizione = des;
        this.matricolaL = matricolaLeader;
        this.n_tecnici = n_tecnici;
        this.codR_fk = res;

    }

    public String getCodTeam() {
        return this.codTeam;
    }

    public void setCodTeam(String codTeam) {
        this.codTeam = codTeam;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getMatricolaL() {
        return this.matricolaL;
    }

    public void setMatricolaL(String matricolaL) {
        this.matricolaL = matricolaL;
    }

    public Integer getN_tecnici() { return this.n_tecnici;}

    public void setN_tecnici(Integer n_tecnici) { this.n_tecnici = n_tecnici; }

    public Responsabile getResponsabile() {
        return this.codR_fk;
    }

    public void setResponsabile(Responsabile responsabile) {
        this.codR_fk = responsabile;
    }
}

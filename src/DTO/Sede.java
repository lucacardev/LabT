package DTO;

public class Sede {

    private Integer CodS;
    private String nome;
    private String indirizzo;


    public Sede (Integer codS,String nome,String indirizzo) {
        this.CodS = codS;
        this.nome = nome;
        this.indirizzo = indirizzo;
    }

    public Sede (String nome,String indirizzo) {
        this.nome = nome;
        this.indirizzo = indirizzo;
    }

//GETTERS E SETTERS

    public Integer getCodS() {

        return this.CodS;
    }

    public void setCodS(Integer codS) {

        this.CodS = codS;
    }

    public String getNome() {

        return this.nome;
    }

    public void setNome(String nome) {

        this.nome = nome;
    }

    public String getIndirizzo() {

        return this.indirizzo;
    }

    public void setIndirizzo(String indirizzo) {

        this.indirizzo = indirizzo;
    }

}
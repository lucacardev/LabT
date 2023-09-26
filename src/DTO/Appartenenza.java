package DTO;

public class Appartenenza {
    private Laboratorio CodL_fk;
    private Sede CodS_fk;

    public Appartenenza(Laboratorio CodL_fk, Sede CodS_fk ) {
        this.CodL_fk = CodL_fk;
        this.CodS_fk = CodS_fk;
    }

    //GETTERS E SETTERS


    public Laboratorio getCodL_fk() {

        return this.CodL_fk;
    }

    public void setCodL_fk(Laboratorio codL_fk) {


        this.CodL_fk = codL_fk;
    }

    public Sede getCodS_fk() {

        return this.CodS_fk;
    }

    public void setCodS_fk(Sede codS_fk) {


        this.CodS_fk = codS_fk;
    }
}
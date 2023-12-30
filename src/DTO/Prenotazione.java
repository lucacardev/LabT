package DTO;

import java.util.Date;

public class Prenotazione {
    private Date data_prenotazioneS;
    private Date ora_prenotazioneS;
    private Date tempo_utilizzoS;
    private Utente Username_fk;
    private Strumento CodStrumento_fk;

    private int cod_prenotazione;

    public Prenotazione(Date data_prenotazioneS, Date ora_prenotazioneS, Date tempo_utilizzoS, Utente Username_fk, Strumento CodStrumento_fk) {

        this.data_prenotazioneS = data_prenotazioneS;
        this.ora_prenotazioneS = ora_prenotazioneS;
        this.tempo_utilizzoS = tempo_utilizzoS;
        this.Username_fk = Username_fk;
        this.CodStrumento_fk = CodStrumento_fk;

    }

    public Prenotazione(int cod_prenotazione, Date data_prenotazioneS, Date ora_prenotazioneS, Date tempo_utilizzoS, Utente Username_fk, Strumento CodStrumento_fk) {


        this.cod_prenotazione = cod_prenotazione;
        this.data_prenotazioneS = data_prenotazioneS;
        this.ora_prenotazioneS = ora_prenotazioneS;
        this.tempo_utilizzoS = tempo_utilizzoS;
        this.Username_fk = Username_fk;
        this.CodStrumento_fk = CodStrumento_fk;

    }

    public Prenotazione(Date data_prenotazioneS, Date ora_prenotazioneS, Date tempo_utilizzoS, Strumento CodStrumento_fk) {

        this.data_prenotazioneS = data_prenotazioneS;
        this.ora_prenotazioneS = ora_prenotazioneS;
        this.tempo_utilizzoS = tempo_utilizzoS;
        this.CodStrumento_fk = CodStrumento_fk;

    }



    public int getCod_prenotazione() {

        return cod_prenotazione;

    }

    public void setCod_prenotazione(int cod_prenotazione) {

        this.cod_prenotazione = cod_prenotazione;

    }

    public Date getData_prenotazioneS() {

        return this.data_prenotazioneS;

    }

    public void setData_prenotazioneS(Date data_prenotazioneS) {

        this.data_prenotazioneS = data_prenotazioneS;

    }

    public Date getOra_prenotazioneS() {

        return this.ora_prenotazioneS;

    }

    public void setOra_prenotazioneS(Date ora_prenotazioneS) {

        this.ora_prenotazioneS = ora_prenotazioneS;

    }

    public Date getTempo_utilizzoS() {

        return this.tempo_utilizzoS;

    }

    public void setTempo_utilizzoS(Date tempo_utilizzoS) {

        this.tempo_utilizzoS = tempo_utilizzoS;

    }

    public Utente getUsername_fk() {

        return this.Username_fk;

    }

    public void setUsername_fk(Utente username_fk) {

        this.Username_fk = username_fk;

    }

    public Strumento getCodStrumento_fk() {

        return this.CodStrumento_fk;

    }

    public void setCodStrumento_fk(Strumento codStrumento_fk) {

        this.CodStrumento_fk = codStrumento_fk;

    }
}
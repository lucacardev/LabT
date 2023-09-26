package DTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class Utilizzato {

    private Utente Username_fk;
    private Strumento CodStrumento_fk;
    private LocalDate datautilizzo;
    private LocalTime oraInizio;
    private LocalTime oraFine;

    public Utilizzato(Utente Username_fk, Strumento CodStrumento_fk, LocalDate datautilizzo,
                      LocalTime oraInizio, LocalTime oraFine) {

        this.Username_fk = Username_fk;
        this.CodStrumento_fk = CodStrumento_fk;
        this.datautilizzo = datautilizzo;
        this.oraInizio = oraInizio;
        this.oraFine = oraFine;

    }

}
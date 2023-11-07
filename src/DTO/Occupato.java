package DTO;

import java.security.PrivateKey;

public class Occupato {
    private Utente username_fk;
    private Postazione codPostazione_fk;

    public Occupato(Utente username_fk, Postazione codP_fk) {
        this.username_fk = username_fk;
        this.codPostazione_fk = codP_fk;
    }

    public Utente getUsername_fk() {
        return this.username_fk;
    }

    public void setUsername_fk(Utente username_fk) {
        this.username_fk = username_fk;
    }

    public Postazione getCodPostazione_fk() {
        return this.codPostazione_fk;
    }

    public void setCodPostazione_fk(Postazione codPostazione_fk) {
        this.codPostazione_fk = codPostazione_fk;
    }
}

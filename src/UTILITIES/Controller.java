package UTILITIES;

import DAO.*;
import DTO.*;
import DAO.SedeDAO;
import GUI.RightLoginAccess;

import java.sql.Timestamp;
import java.util.List;

public class Controller {

    private RightLoginAccess loginPage;
    private UtenteDAO utenteDAO;
    private DB_Connection connection;
    private Utente utenteDTO;
    private SedeDAO sedeDAO;
    private StrumentoDAO strumentoDAO;
    private PrenotazioneDAO prenotazioneDAO;

    private AppartenenzaDAO appartenenzaDAO;

    private PostazioneDAO postazioneDAO;

    private ResponsabileDAO responsabileDAO;




    public static void main(String[] args) {


    }

    public Controller() {

        /*Creiamo oggetti delle classi di cui occorriamo e passiamo come parametro
        * il controller*/

        loginPage = new RightLoginAccess(this);
        utenteDAO = new UtenteDAO(this);
        sedeDAO = new SedeDAO(this);
        strumentoDAO = new StrumentoDAO(this);
        prenotazioneDAO = new PrenotazioneDAO(this);
        responsabileDAO = new ResponsabileDAO(this);


    }

    //////////////////////////////METODI UTENTE DAO///////////////////////////////


    /*Verifica utente presente nel database*/


    public boolean verificaUtente(Utente utente) {

        boolean utenteTrovato;

        utenteTrovato = utenteDAO.verificaCredenzialiDAO(utente.getEmail(), utente.getPw());

        return utenteTrovato;
    }

    public boolean verificaMailUtente (String email) {

        boolean emailTrovata;

        emailTrovata = utenteDAO.verificaMailUtente(email);

        return emailTrovata;

    }

    public String recuperoPasswordUtenteC (String email) {

        String PasswordC;

        PasswordC = utenteDAO.recuperaPasswordUtenteDAO(email);

        return PasswordC;

    }

    public boolean aggiornaPasswordUtenteC(String email, String nuovaPassword) {

       boolean update;

       update = utenteDAO.aggiornaPasswordUtenteDAO(email, nuovaPassword);

       return update;

    }

    public boolean newUserRegister(Utente utente) {


        return utenteDAO.newUserRegister(utente.getUsername(), utente.getEmail(), utente.getPw());

    }

    public boolean verifyMailUsernameC (Utente utente) {

        return utenteDAO.verifyMailUsername(utente.getUsername(), utente.getEmail());

    }


    public String usernameRecovery(Utente utente) {

        return utenteDAO.usernameRecovery(utente.getEmail());

    }

    public List<Strumento> recuperoListaStrumenti(String nomeSede, String descrizioneStrumento) {

        return strumentoDAO.recuperoStrumenti(nomeSede, descrizioneStrumento);

    }

    public List<Strumento> recuperoTuttaListaStrumenti() {

        return strumentoDAO.recuperoTuttiStrumenti();

    }

    public boolean newUserBookingC(Prenotazione prenotazione) {
        java.util.Date dataPrenotazione = prenotazione.getData_prenotazioneS();
        java.util.Date oraPrenotazione = prenotazione.getOra_prenotazioneS();
        java.util.Date tempoUtilizzo = prenotazione.getTempo_utilizzoS();

        Timestamp tsDataPrenotazione = new Timestamp(dataPrenotazione.getTime());
        Timestamp tsOraPrenotazione = new Timestamp(oraPrenotazione.getTime());
        Timestamp tsTempoUtilizzo = new Timestamp(tempoUtilizzo.getTime());

        return prenotazioneDAO.newUserBooking(tsDataPrenotazione, tsOraPrenotazione, tsTempoUtilizzo, prenotazione.getUsername_fk(), prenotazione.getCodStrumento_fk());
    }


    public int recuperoCodlConPostazioneC(Postazione postazione) {
        int codL;
        codL =  postazioneDAO.recuperoCodlConPostazione(postazione.getCodPostazione());

        System.out.println(codL);

        return codL;

    }



    /*
    public Integer codificaSede(String nomeSede) {

        return sedeDAO.codificaSedeDAO(nomeSede);


    }
    */

    //////////////////////////////METODI RESPONSABILE DAO///////////////////////////////


    /*Verifica responsabile presente nel database*/


    public boolean verificaResponsabile(Responsabile responsabile) {

        boolean responsabileTrovato;

        responsabileTrovato = responsabileDAO.verificaCredenzialiDAO(responsabile.getMatricola(), responsabile.getPw());

        return responsabileTrovato;
    }

    public boolean verificaMailResponsabile (String email) {

        boolean emailTrovata;

        emailTrovata = responsabileDAO.verificaMailResponsabile(email);

        return emailTrovata;

    }


    public boolean verificaMatricolaResponsabile (String matricola) {

        boolean matricolaTrovata;

        matricolaTrovata = responsabileDAO.verificaMatricolaResponsabile(matricola);

        return matricolaTrovata;

    }

    public String recuperoPasswordResponsabileC(String email) {

        String PasswordC;

        PasswordC = responsabileDAO.recuperaPasswordResponsabile(email);

        return PasswordC;

    }

    public boolean aggiornaPasswordResponsabileC(String email, String nuovaPassword) {

        boolean update;

        update = responsabileDAO.aggiornaPasswordResponsabile(email, nuovaPassword);

        return update;

    }

    public boolean newRespRegister(Responsabile responsabile) {


        return responsabileDAO.newResponsableRegister(responsabile.getMatricola(),responsabile.getNome(),responsabile.getCognome(),responsabile.getEmail(),responsabile.getPw());

    }

    public boolean verifyMatricolaResponsabileC (Responsabile responsabile) {

        return responsabileDAO.verificaMatricolaResponsabile(responsabile.getMatricola());

    }

    public String MatricolaRecovery(Responsabile responsabile) {

        return responsabileDAO.matricolaRecovery(responsabile.getPw());

    }

}

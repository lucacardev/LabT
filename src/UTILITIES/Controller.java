package UTILITIES;

import DAO.*;
import DTO.*;
import DAO.SedeDAO;
import GUI.RightLoginAccess;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
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

    private TeamDAO teamDAO;
    private TecnicoDAO tecnicoDAO;




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
        teamDAO = new TeamDAO(this);
        tecnicoDAO = new TecnicoDAO(this);


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

    public List<Prenotazione> recuperoMiePrenotazioniC(Utente utenteC) {

        return prenotazioneDAO.recuperoMiePrenotazioni(utenteC);

    }

    public List<Prenotazione> recuperoPrenStrumenti(Strumento codStrumento) {

        return prenotazioneDAO.recuperoPrenStrumenti(codStrumento);

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

    //////////////////////////////RESPONSABILE///////////////////////////////


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

        return responsabileDAO.newResponsableRegister(responsabile.getMatricola(),responsabile.getNome(),responsabile.getCognome(),responsabile.getEmail(),responsabile.getPw(),responsabile.getSede());

    }

    public boolean verifyMatricolaMailResponsabileC (Responsabile responsabile) {

        return responsabileDAO.verifyMatricolaMailR(responsabile.getMatricola(),responsabile.getEmail());

    }

    public String MatricolaRecovery(Responsabile responsabile) {

        return responsabileDAO.matricolaRecovery(responsabile.getPw());

    }

    //////////////////////////////////////////////////TEAM/////////////////////////////////////////////////////////

    public boolean recuperoTeamdaCodC(String codT) {
        boolean teamTrovato;
        teamTrovato = teamDAO.recuperoTeamDaCodice(codT);
        return teamTrovato;
    }

    public Team recuperoTeamC(String codT) {

        return teamDAO.recuperoTeam(codT);
    }

    public boolean newTeamInsert(Team team) {

        return teamDAO.newTeamInsert(team.getCodTeam(), team.getNome(), team.getDescrizione(), team.getMatricolaL(), team.getN_tecnici(), team.getResponsabile());
    }

    public void deleteTeamC(Team team) {

        teamDAO.deleteTeam(team.getCodTeam());
    }

    /////////////////////////////TECNICO/////////////////////////////////////////

    public List<Tecnico> recuperoTecniciC (Team team) {
        return tecnicoDAO.recuperoTecniciDalDB(team);
    }
    public boolean updateTecniciC(Tecnico tecnico,String nuovoCodTeam) {

        return tecnicoDAO.updateTecnici(tecnico,nuovoCodTeam);
    }

    public Tecnico recuperoTecnicoDallaMatricola (String matricola) {

        return tecnicoDAO.recuperoTecnicoMatricola(matricola);
    }

    //Metodo per recuperare il tempo massimo dello strumento
    public Time getStrumentHourC(Strumento strumento) {

        return strumentoDAO.getStrumentHour(strumento);
    }

    //Metodo per eliminare una prneotazione

    public void eliminaPrenotazioneC(Prenotazione prenotazione) {

        prenotazioneDAO.eliminaPrenotazioneDAO(prenotazione.getCod_prenotazione());

    }

    public boolean modificaMiaPrenotazioneC(Prenotazione prenotazione) {

        //Chiamiamo il metodo removeMills per togliere i millisecondi che potrebbero causare errpro
        Timestamp tsDataPrenotazione = removeMillis(prenotazione.getData_prenotazioneS());
        Timestamp tsOraPrenotazione = removeMillis(prenotazione.getOra_prenotazioneS());
        Timestamp tsTempoUtilizzo = removeMillis(prenotazione.getTempo_utilizzoS());

        System.out.println(prenotazione.getCod_prenotazione());

        return prenotazioneDAO.modificaMiaPrenotazioneDAO(
                prenotazione.getCod_prenotazione(),
                tsDataPrenotazione,
                tsOraPrenotazione,
                tsTempoUtilizzo
        );
    }

    private Timestamp removeMillis(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTimeInMillis());
    }

    public Strumento recuperoStrumentoC (int codStrumento) {

        return strumentoDAO.recuperoStrumento(codStrumento);

    }

    public String riepilogoStrumentoC (Strumento strumento, int mese, int anno) {

        return strumentoDAO.riepilogoStrumentoDAO(strumento.getCodStrumento(), mese, anno);

    }

    public String riepilogoStrumentoNoMeseC (Strumento strumento, int anno) {

        return strumentoDAO.riepilogoStrumentoNoMeseDAO(strumento.getCodStrumento(), anno);

    }

    public List<Team> recuperoTeamsDalDBC(Responsabile responsabileCorrente) {

        return teamDAO.recuperoTeamsDalDB(responsabileCorrente);
    }
}

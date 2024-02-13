package UTILITIES;

import DAO.*;
import DTO.*;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Controller {
    private final UtenteDAO utenteDAO;
    private final StrumentoDAO strumentoDAO;
    private final PrenotazioneDAO prenotazioneDAO;
    private final LaboratorioDAO laboratorioDAO;
    private final ResponsabileDAO responsabileDAO;
    private final TeamDAO teamDAO;
    private final TecnicoDAO tecnicoDAO;

    public Controller() {

        /*We create objects of the classes we need and pass as parameters
         *the controller*/

        utenteDAO = new UtenteDAO(this);
        strumentoDAO = new StrumentoDAO(this);
        prenotazioneDAO = new PrenotazioneDAO(this);
        responsabileDAO = new ResponsabileDAO(this);
        teamDAO = new TeamDAO(this);
        tecnicoDAO = new TecnicoDAO(this);
        laboratorioDAO = new LaboratorioDAO(this);

    }

    public Laboratorio labRecoveryWithCodeC(String codL) {

        return laboratorioDAO.labRecoveryDAO(codL);

    }

    public boolean userVerification(Utente utente) {
        
        return utenteDAO.credentialsVerificationDAO(utente.getEmail(), utente.getPw());
        
    }

    public boolean userMailVerifyC (String email) {

        return utenteDAO.userMailVerifyC(email);
        
    }

    public String userPasswordRecoveryC (String email) {
        
        return utenteDAO.userPasswordRecoveryDAO(email);
        
    }

    public boolean userPasswordUpdateC(String email, String newPassword) {
        
       return utenteDAO.userPasswordUpdateDAO(email, newPassword);
       
    }

    public boolean newUserRegisterC(Utente user) {

        return utenteDAO.newUserRegisterDAO(user.getUsername(), user.getEmail(), user.getPw());

    }

    public boolean verifyMailUsernameC (Utente user) {

        return utenteDAO.verifyMailUsername(user.getUsername(), user.getEmail());

    }


    public String usernameRecovery(Utente user) {

        return utenteDAO.usernameRecovery(user.getEmail());

    }

    public List<Strumento> toolsListRecovery(String headQuarters, String toolDescription) {

        return strumentoDAO.toolsRecovery(headQuarters, toolDescription);

    }

    public List<Strumento> allToolsListRecovery() {

        return strumentoDAO.allToolsRecoveryDAO();

    }

    public List<Prenotazione> myBookingRecoveryC(Utente user) {

        return prenotazioneDAO.myBookingRecoveryDAO(user);

    }

    public List<Prenotazione> toolsBookingsRecoveryC(Strumento codStrumento) {

        return prenotazioneDAO.toolsBookingsRecoveryC(codStrumento);

    }

    public boolean newUserBookingC(Prenotazione prenotazione) {
        java.util.Date bookDate = prenotazione.getData_prenotazioneS();
        java.util.Date bookHours = prenotazione.getOra_prenotazioneS();
        java.util.Date usageTime = prenotazione.getTempo_utilizzoS();

        Timestamp tsBookDate = new Timestamp(bookDate.getTime());
        Timestamp tsBookHours = new Timestamp(bookHours.getTime());
        Timestamp tsUsageTime = new Timestamp(usageTime.getTime());

        return prenotazioneDAO.newUserBooking(tsBookDate, tsBookHours, tsUsageTime, prenotazione.getUsername_fk(), prenotazione.getCodStrumento_fk());

    }

    public boolean managerVerificationC(Responsabile manager) {

        return responsabileDAO.credentialsVerificationDAO(manager.getMatricola(), manager.getPw());

    }

    public boolean managerMailCheckDAO (String email) {

         return responsabileDAO.managerMailCheckDAO(email);
         
    }

    public String managerPasswordRecoveryC(String email) {
        
        return responsabileDAO.managerPasswordRecoveryDAO(email);

    }

    public boolean managerPasswordUpdateC(String email, String newPassword) {
        
         return responsabileDAO.managerPasswordUpdateDAO(email, newPassword);

    }

    public boolean newRespRegister(Responsabile manager) {

        return responsabileDAO.newManagerSignIn(manager.getMatricola(), manager.getNome(), manager.getCognome(), manager.getEmail(), manager.getPw(), manager.getSede());

    }

    public boolean mailManagerMatriculationVerifyC (Responsabile manager) {

        return responsabileDAO.verifyManagerMailMatriculationDAO(manager.getMatricola(),manager.getEmail());

    }

    public boolean teamRecoveryWithCodeC(String codT) {
        
        return teamDAO.teamRecoveryWithCode(codT);

    }

    public Team teamRecoveryC(String codT) {

        return teamDAO.teamRecoveryDAO(codT);

    }

    public boolean newTeamInsert(Team team) {

        return teamDAO.newTeamInsertDAO(team.getCodTeam(), team.getNome(), team.getDescrizione(), team.getMatricolaL(), team.getN_tecnici(), team.getResponsabile());
    }

    public void deleteTeamC(Team team) {

        teamDAO.deleteTeamDAO(team.getCodTeam());

    }

    public List<Tecnico> technicianRecoveryC (Team team) {
        return tecnicoDAO.recuperoTecniciDalDB(team);
    }
    public boolean technicianUpdateC(Tecnico technician,String newCodTeam) {

        return tecnicoDAO.techniciansUpdateDAO(technician,newCodTeam);

    }

    public void deleteBookingsC(Prenotazione booking) {

        prenotazioneDAO.bookingDeleteDAO(booking.getCod_prenotazione());

    }

    public boolean myBookingsUpdateC(Prenotazione booking) {

        //We call the removeMills method to remove milliseconds that could cause error

        Timestamp tsBookDate = removeMillis(booking.getData_prenotazioneS());
        Timestamp tsBookHours = removeMillis(booking.getOra_prenotazioneS());
        Timestamp tsUsageTime = removeMillis(booking.getTempo_utilizzoS());

        return prenotazioneDAO.changeMyBookingDAO(
                booking.getCod_prenotazione(),
                tsBookDate,
                tsBookHours,
                tsUsageTime
        );

    }

    private Timestamp removeMillis(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MILLISECOND, 0);

        return new Timestamp(calendar.getTimeInMillis());

    }

    public Strumento toolsRecoveryC (int toolCode) {

        return strumentoDAO.toolRecovery(toolCode);

    }

    public String toolSummaryC (Strumento strumento, int month, int anno) {

        return strumentoDAO.toolSummaryDAO(strumento.getCodStrumento(), month, anno);

    }

    public String noMonthToolSummaryC (Strumento tool, int year) {

        return strumentoDAO.noMonthToolSummaryDAO(tool.getCodStrumento(), year);

    }

    public List<Team> dBTeamsRecovery(Responsabile currentManager) {

        return teamDAO.teamRecoverysDalDB(currentManager);

    }

    public void updateTeamLeaderC(Team team, Tecnico leader) {

        teamDAO.updateTeamLeaderDAO(team.getCodTeam(), leader.getMatricola());

    }

    public void technicianUpdate1To1C(Tecnico tecToBeReplaced, Tecnico tecReplaced, Team team) {

        tecnicoDAO.technicianUpdate1To1DAO(tecToBeReplaced, tecReplaced, team);

    }

    public void technicianUpdateSameTeamC(Tecnico tecToBeReplaced, Tecnico tecReplaced, Team team) {

        tecnicoDAO.technicianUpdateSameTeamDAO(tecToBeReplaced, tecReplaced, team);

    }


}

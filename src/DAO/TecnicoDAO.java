package DAO;

import DTO.*;
import UTILITIES.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TecnicoDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public TecnicoDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();
    }


    public void eliminaTecniciTeam(String codTeam) {

        try {
            String query = "DELETE FROM tecnico WHERE codteam_fk = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);

            preparedStatement.setString(1, codTeam);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione dei tecnici con cod Team");
            e.printStackTrace();
        }


    }


    public Tecnico recuperoTecnicoMatricola(String matricola) {

        Tecnico tecnicoRecuperato = null;

        try {
            String query = "SELECT matricola,nome,cognome FROM tecnico WHERE matricola = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String matricolaRecuperata = resultSet.getString("matricola");
                String nomeRecuperato = resultSet.getString("nome");
                String cognomeRecuperato = resultSet.getString("cognome");
                String codfiscale = resultSet.getString("codfiscale");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String codL_fk = resultSet.getString("codl_fk");
                String codT_fk = resultSet.getString("codteam_fk");

                Laboratorio laboraotorioRec = currController.recuperoLaboratorioConCodC(codL_fk);
                Team teamRec = currController.recuperoTeamC(codT_fk);

                tecnicoRecuperato = new Tecnico(matricolaRecuperata, nomeRecuperato, cognomeRecuperato, codfiscale, telefono, email,laboraotorioRec,teamRec);
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero tecnico tramite matricola");
            e.printStackTrace();
        }

        return tecnicoRecuperato;


    }


    //Recupero tecnici che lavorano in un team
    public List<Tecnico> recuperoTecniciDalDB(Team team) {
        List<Tecnico> teamMembers = new ArrayList<>();

        try {
            String query = "SELECT * FROM tecnico WHERE codteam_fk = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, team.getCodTeam());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String matricola = resultSet.getString("matricola");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String codfiscale = resultSet.getString("codfiscale");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String codL_fk = resultSet.getString("codl_fk");
                String codT_fk = resultSet.getString("codteam_fk");

                TeamDAO teamDAO = new TeamDAO(currController);
                Team team1 = teamDAO.recuperoTeam(codT_fk);
                Laboratorio laboratorioRec = currController.recuperoLaboratorioConCodC(codL_fk);

                Tecnico tecnico = new Tecnico(matricola, nome, cognome,codfiscale,telefono,email, laboratorioRec, team1);
                teamMembers.add(tecnico);
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dei tecnici dal DB");
            e.printStackTrace();
        }

        return teamMembers;
    }

    //Recupero tecnici che non lavorano in un team
    public List<Tecnico> recuperoTecniciNoTeam( ) {
        List<Tecnico> tecnici = new ArrayList<>();

        try {
            String query = "SELECT * FROM tecnico WHERE codteam_fk IS NULL";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String matricola = resultSet.getString("matricola");
                String nome = resultSet.getString("nome");
                String cognome = resultSet.getString("cognome");
                String codfiscale = resultSet.getString("codfiscale");
                String telefono = resultSet.getString("telefono");
                String email = resultSet.getString("email");
                String codL_fk = resultSet.getString("codl_fk");
                String codT_fk = resultSet.getString("codteam_fk");

                Laboratorio laboraotorioRec = currController.recuperoLaboratorioConCodC(codL_fk);
                Team teamRec = currController.recuperoTeamC(codT_fk);

                Tecnico tecnico = new Tecnico(matricola, nome, cognome,codfiscale,telefono,email, laboraotorioRec, teamRec);

                tecnici.add(tecnico);
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero dei tecnici che non lavorano in un team dal DB");
            e.printStackTrace();
        }

        return tecnici;
    }

    //Update dei tecnici
    public boolean updateTecnici (Tecnico tecnico, String nuovoCodiceTeam) {
        try {

            String query = "UPDATE tecnico SET codteam_fk = ? WHERE matricola = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);

            preparedStatement.setString(1, nuovoCodiceTeam);
            preparedStatement.setString(2, tecnico.getMatricola());

            int updateCount = preparedStatement.executeUpdate();

            return updateCount > 0; // Ritorna true se l'aggiornamento ha avuto successo, altrimenti false
        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento del codice del team del tecnico");
            e.printStackTrace();
            return false;
        }

    }

    /*Metodo per aggiornare i tecnici durante la modifica dell'organigramma, la modifica avviene 1 to 1 altrimentri
     * avremmo problemi con la posizione del tecnico nel organigramma.*/

    public boolean updateTecnico1to1C(Tecnico tecnicoDaSostituire, Tecnico tecnicoSostituto, Team team) {

        List<Tecnico> listaTecnici;

        try {

            //Recupero della lista dei tecnici nel team
            listaTecnici = recuperoTecniciDalDB(team);

            // Trova l'indice del tecnico da sostituire nella lista del team
            int index = listaTecnici.indexOf(tecnicoDaSostituire);

            // Rimuovi il tecnico da sostituire dalla lista del team
            listaTecnici.remove(tecnicoDaSostituire);

            // Inserisci il tecnico sostituto nella posizione trovata
            listaTecnici.add(index, tecnicoSostituto);

            // Aggiorna il valore di codteam_fk del tecnico da sostituire a null nel database
            aggiornaCodTeam(tecnicoDaSostituire.getMatricola(), null);

            aggiornaCodTeam(tecnicoSostituto.getMatricola(), team.getCodTeam());

            //Eliminiamo tutti i tecnici dal DB
            eliminaTecniciTeam(team.getCodTeam());

            // Aggiorna il team nel database o in memoria, a seconda della tua implementazione
           insertTecniciTeam(team, listaTecnici);

            return true;

        } catch (Exception e) {
            System.out.println("Errore durante lo scambio dei tecnici");
            e.printStackTrace();
            return false;
        }
    }

    // Aggiorna il valore di codteam_fk del tecnico a null nel database
    private void aggiornaCodTeam(String matricola, String codTeam) {
        try {
            String query = "UPDATE tecnico SET codteam_fk = ? WHERE matricola = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, codTeam);
            preparedStatement.setString(2, matricola);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Errore durante l'aggiornamento del codteam_fk");
            e.printStackTrace();
        }
    }

    public void insertTecniciTeam(Team team, List<Tecnico> listaTecnici) {

        try {

            String query = "INSERT INTO tecnico(matricola, nome, cognome, codfiscale, telefono, email, codl_fk, codteam_fk)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);

            for (Tecnico tecnico : listaTecnici) {
                preparedStatement.setString(1, tecnico.getMatricola());
                preparedStatement.setString(2, tecnico.getNome());
                preparedStatement.setString(3, tecnico.getCognome());
                preparedStatement.setString(4, tecnico.getCodfiscale());
                preparedStatement.setString(5, tecnico.getTelefono());
                preparedStatement.setString(6, tecnico.getEmail());
                preparedStatement.setString(7, tecnico.getLaboratorio().getCodL());
                preparedStatement.setString(8, team.getCodTeam());
                preparedStatement.executeUpdate();
            }

        } catch (SQLException e) {
            System.out.println("Errore durante l'inserimento del team nel database");
            e.printStackTrace();
        }
    }

}
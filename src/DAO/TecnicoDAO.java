package DAO;

import DTO.*;
import UTILITIES.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TecnicoDAO {
    private final DB_Connection DBConnection;
    Controller currController;


    public TecnicoDAO(Controller controller) {

        currController = controller;

        DBConnection = DB_Connection.getConnessione();

    }


    public void TecTeamDelete(String codTeam) {

        try {

            String query = "DELETE FROM tecnico WHERE codteam_fk = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);

            preparedStatement.setString(1, codTeam);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {

            System.out.println("Errore durante l'eliminazione dei tecnici con cod Team");
            e.printStackTrace();

        }

    }

    //Recupero tecnici che lavorano in un team
    public List<Tecnico> recuperoTecniciDalDB(Team team) {
        List<Tecnico> teamMembers = new ArrayList<>();

        try {

            String query = "SELECT * FROM tecnico WHERE codteam_fk = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
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
                Team team1 = teamDAO.teamRecoveryDAO(codT_fk);
                Laboratorio laboratorioRec = currController.labRecoveryWithCodeC(codL_fk);

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
    public List<Tecnico> recuperoTecniciNoTeam() {

        List<Tecnico> tecnici = new ArrayList<>();

        try {

            String query = "SELECT * FROM tecnico WHERE codteam_fk IS NULL";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
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

                Laboratorio laboraotorioRec = currController.labRecoveryWithCodeC(codL_fk);
                Team teamRec = currController.teamRecoveryC(codT_fk);

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
    public boolean techniciansUpdateDAO (Tecnico tecnico, String nuovoteamCode) {

        try {

            String query = "UPDATE tecnico SET codteam_fk = ? WHERE matricola = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);

            preparedStatement.setString(1, nuovoteamCode);
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

    public void technicianUpdate1To1DAO(Tecnico tecToBeReplaced, Tecnico tecReplaced, Team team) {

        List<Tecnico> tecList;

        try {

            //Recupero della lista dei tecnici nel team
            tecList = recuperoTecniciDalDB(team);

            // Trova l'indice del tecnico da sostituire nella lista del team
            int index = tecList.indexOf(tecToBeReplaced);

            // Rimuovi il tecnico da sostituire dalla lista del team
            tecList.remove(tecToBeReplaced);

            // Inserisci il tecnico sostituto nella posizione trovata
            tecList.add(index, tecReplaced);

            // Aggiorna il valore di codteam_fk del tecnico da sostituire a null nel database
            aggiornaCodTeam(tecToBeReplaced.getMatricola(), null);

            aggiornaCodTeam(tecReplaced.getMatricola(), team.getCodTeam());

            //Eliminiamo tutti i tecnici dal DB
            TecTeamDelete(team.getCodTeam());

            // Aggiorna il team nel database o in memoria, a seconda della tua implementazione
           insertTecniciTeam(team, tecList);

        } catch (Exception e) {

            System.out.println("Errore durante lo scambio dei tecnici");
            e.printStackTrace();

        }

    }

    public void technicianUpdateSameTeamDAO(Tecnico tecToBeReplaced, Tecnico tecReplaced, Team team) {

        List<Tecnico> tecList;

        try {

            //Recupero della lista dei tecnici nel team
            tecList = recuperoTecniciDalDB(team);

            // Trova l'indice del tecnico da sostituire nella lista del team
            int index1 = tecList.indexOf(tecToBeReplaced);
            int index2 = tecList.indexOf(tecReplaced);

            // Rimuovi il tecnico da sostituire dalla lista del team
            tecList.remove(tecToBeReplaced);
            tecList.remove(tecReplaced);

            // Inserisci il tecnico sostituto nella posizione trovata
            tecList.add(index1, tecReplaced);
            tecList.add(index2, tecToBeReplaced);

            //Eliminiamo tutti i tecnici dal DB
            TecTeamDelete(team.getCodTeam());

            // Aggiorna il team nel database o in memoria, a seconda della tua implementazione
            insertTecniciTeam(team, tecList);

        } catch (Exception e) {

            System.out.println("Errore durante lo scambio dei tecnici");
            e.printStackTrace();

        }

    }

    // Aggiorna il valore di codteam_fk del tecnico a null nel database
    private void aggiornaCodTeam(String matricola, String codTeam) {

        try {

            String query = "UPDATE tecnico SET codteam_fk = ? WHERE matricola = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, codTeam);
            preparedStatement.setString(2, matricola);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {

            System.out.println("Errore durante l'aggiornamento del codteam_fk");
            e.printStackTrace();

        }

    }

    public void insertTecniciTeam(Team team, List<Tecnico> tecList) {

        try {

            String query = "INSERT INTO tecnico(matricola, nome, cognome, codfiscale, telefono, email, codl_fk, codteam_fk)" +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);

            for (Tecnico tecnico : tecList) {

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
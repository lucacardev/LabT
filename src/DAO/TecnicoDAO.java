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

                tecnicoRecuperato = new Tecnico(matricolaRecuperata, nomeRecuperato, cognomeRecuperato, null, null,null,null);
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
                //Integer codL_fk = resultSet.getInt("codL_FK");
                String codT_fk = resultSet.getString("codteam_fk");

                TeamDAO teamDAO = new TeamDAO(currController);
                Team team1 = teamDAO.recuperoTeam(codT_fk);

                Tecnico tecnico = new Tecnico(matricola, nome, cognome,codfiscale,telefono,null,team1);
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
                //Integer codL_fk = resultSet.getInt("codL_fk");
                //String codT_fk = resultSet.getString("codT_fk");

                Tecnico tecnico = new Tecnico(matricola, nome, cognome,codfiscale,telefono,null,null);
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

            System.out.println("Matricola Tecnico: " + tecnico.getMatricola());
            System.out.println("Nuovo Codice Team: " + nuovoCodiceTeam);

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

}
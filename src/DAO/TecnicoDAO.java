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


    public boolean eliminaTecnicoMatricola(String matricola) {

        try {
            String query = "DELETE FROM tecnico WHERE matricola = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);

            preparedStatement.setString(1, matricola);

            int updateCount = preparedStatement.executeUpdate();

            return updateCount > 0;

        } catch (SQLException e) {
            System.out.println("Errore durante l'eliminazione del tecnico");
            e.printStackTrace();
            return false;
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

    public boolean updateTecnico1to1 (Tecnico tecnicoDaSostituire, Tecnico tecnicoSostituto, String nuovoCodiceTeam) {

        String matricola1 = tecnicoDaSostituire.getMatricola();
        String nome1 = tecnicoDaSostituire.getNome();
        String cognome1 = tecnicoDaSostituire.getCognome();
        String codF1 = tecnicoDaSostituire.getCodfiscale();
        String telefono1 = tecnicoDaSostituire.getTelefono();
        String email1 = tecnicoDaSostituire.getEmail();
        String codlFk1 = tecnicoDaSostituire.getLaboratorio().getCodL();

        String matricola2 = tecnicoSostituto.getMatricola();
        String nome2 = tecnicoSostituto.getNome();
        String cognome2 = tecnicoSostituto.getCognome();
        String codF = tecnicoSostituto.getCodfiscale();
        String telefono2 = tecnicoSostituto.getTelefono();
        String email2 = tecnicoSostituto.getEmail();
        String codlFk = tecnicoSostituto.getLaboratorio().getCodL();

        eliminaTecnicoMatricola(tecnicoSostituto.getMatricola());

        try {

            String query = "UPDATE tecnico " +
                    "SET matricola = ?, " +
                    "nome = ?, " +
                    "cognome = ?, " +
                    "codfiscale = ?, " +
                    "telefono = ?, " +
                    "email = ?, " +
                    "codl_fk = ?, " +
                    "codteam_fk = ? " +
                    "WHERE matricola = ?";

            String query2 = "INSERT INTO tecnico (matricola, nome, cognome, codfiscale, telefono, email, codl_fk, codteam_fk) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";


            PreparedStatement preparedStatement1 = connessioneDB.getPreparedStatement(query);
            PreparedStatement preparedStatement2 = connessioneDB.getPreparedStatement(query2);

            preparedStatement1.setString(1, matricola2);
            preparedStatement1.setString(2, nome2);
            preparedStatement1.setString(3, cognome2);
            preparedStatement1.setString(4, codF);
            preparedStatement1.setString(5, telefono2);
            preparedStatement1.setString(6, email2);
            preparedStatement1.setString(7, codlFk);
            preparedStatement1.setString(8, nuovoCodiceTeam);
            preparedStatement1.setString(9, tecnicoDaSostituire.getMatricola());

            preparedStatement2.setString(1, matricola1);
            preparedStatement2.setString(2, nome1);
            preparedStatement2.setString(3, cognome1);
            preparedStatement2.setString(4, codF1);
            preparedStatement2.setString(5, telefono1);
            preparedStatement2.setString(6, email1);
            preparedStatement2.setString(7, codlFk1);
            preparedStatement2.setString(8, null);

            int updateCount1 = preparedStatement1.executeUpdate();
            int updateCount2 = preparedStatement2.executeUpdate();

            return updateCount1 > 0 && updateCount2 > 0; // Ritorna true se l'aggiornamento ha avuto successo, altrimenti false
        } catch (SQLException e) {
            System.out.println("Errore durante lo scambio dei tecnici");
            e.printStackTrace();
            return false;
        }

    }

}
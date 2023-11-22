package DAO;

import UTILITIES.Controller;
import UTILITIES.DB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ResponsabileDAO {
    private static DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public ResponsabileDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();
    }

    //////////////////////////RECUPERO CREDENZIALI///////////////////


    public boolean verificaCredenzialiDAO(String matricola, char[] password) {

        boolean responsabileTrovato = false;

        /*Utilizziamo la classe PreparedStatement per impedire le SQLInjection*/

        try {
            String query = "SELECT * FROM responsabile WHERE matricola= ? AND pw = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, String.valueOf(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                if (resultSet.getString("matricola").equals(matricola) && resultSet.getString("pw").equals(String.valueOf(password))) {
                responsabileTrovato = true;
                }
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return responsabileTrovato;

    }

    public static String [] nomecognomeRecovery(String matricola, char[] password) {

        String[] informazioni = new String[2];

        try {
            String query = "SELECT nome, cognome FROM responsabile WHERE matricola = ? and pw = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2,String.valueOf(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                informazioni[0] = resultSet.getString("nome");
                informazioni[1] = resultSet.getString("cognome");
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero nome cognome tramite matricola");
            e.printStackTrace();
        }

        return informazioni;

    }

    public boolean verificaMailResponsabile(String email) {

        boolean emailTrovata = false;

        try {
            String query = "SELECT * FROM responsabile WHERE email = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                emailTrovata = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return emailTrovata;
    }

    /////////////////////////////////RECUPERO Matricola////////////////////////////

    //Metodo che verifica se la matricola di un responsabile è presente nel database
    public boolean verificaMatricolaResponsabile(String matricola) {

        boolean matricolaTrovata = false;

        try {
            String query = "SELECT * FROM responsabile WHERE matricola = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                matricolaTrovata = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return matricolaTrovata;
    }

    //Metodo per recuperare la matricola dal database in base alla password

    public String matricolaRecovery(char[] password) {

        String matricolaRecuperata = null;

        try {
            String query = "SELECT matricola FROM responsabile WHERE pw = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, String.valueOf(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                matricolaRecuperata = resultSet.getString("matricola");
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero matricola tramite password");
            e.printStackTrace();
        }

        return matricolaRecuperata;

    }

    /////////////////////////////////RECUPERO PASSWORD////////////////////////////
    //Metodo per recuperare la password dal database in base alla mail
    public String recuperaPasswordResponsabile(String email) {

        String password = null;

        try {
            String query = "SELECT pw FROM responsabile WHERE email = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                password = resultSet.getString("pw");
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero password del responsabile");
            e.printStackTrace();
        }

        return password;
    }

    //Metodo per aggiornare la password di un utente presente nel database
    public boolean aggiornaPasswordResponsabile (String email, String nuovaPassword) {
        try {
            String query = "UPDATE responsabile SET pw= ? WHERE email = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, nuovaPassword);
            preparedStatement.setString(2, email);

            int rowsUpdated = preparedStatement.executeUpdate();

            /*Dato che la query ci restituisce il numero di update eseguiti
             * verifichiamo che l'operazione sia andata a buon fine (se maggiore di uno)*/

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nell' update della password nella tabella responsabile");
            return false;
        }
    }

    //Metodo per verificare se mail e matricola già utilizzati
    public boolean verifyMailUsername(String matricola, String email) {

        String matricolaDB;
        String emailDB;

        try {
            String query = "SELECT * FROM responsabile WHERE matricola = ? OR email = ?" ;
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                return true;
            }


        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
        return false;
    }



    //////////////////////INSERT RESPONSABILE/////////////////////7

    public boolean newResponsableRegister(String matricola, String nome, String cognome,String email, char[] password) {

        String query = "INSERT INTO RESPONSABILE (matricola, nome, cognome,email, pw) VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, cognome);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, String.valueOf(password));


            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

    }
}

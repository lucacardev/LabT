package DAO;

import DTO.Postazione;
import DTO.Utente;
import UTILITIES.*;
import DTO.Sede;
import DTO.Laboratorio;

import javax.swing.*;
import java.sql.*;
import java.util.Arrays;

public class UtenteDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public UtenteDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();

    }

    //////////////////////////RECUPERO CREDENZIALI///////////////////
    public boolean verificaCredenzialiDAO(String email, char[] password) {

        boolean utenteTrovato = false;

        /*Utilizziamo la classe PreparedStatement per impedire le SQLInjection*/

        try {
            String query = "SELECT * FROM utente WHERE email = ? AND pw = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, String.valueOf(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                utenteTrovato = true;
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }

        return utenteTrovato;

    }

    /////////////////////////////////RECUPERO EMAIL////////////////////////////

    //Metodo che verifica se la mail di un utente è presente nel database
    public boolean verificaMailUtente(String email) {

        boolean emailTrovata = false;

        try {
            String query = "SELECT * FROM utente WHERE email = ?";
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

    //Metodo per recuperare la password dal database in base alla mail
    public String recuperaPasswordUtenteDAO(String email) {

        String password = null;

        try {
            String query = "SELECT pw FROM utente WHERE email = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                password = resultSet.getString("pw");
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero password dell'utente");
            e.printStackTrace();
        }

        return password;
    }

    //Metodo per aggiornare la password di un utente presente nel database

    public boolean aggiornaPasswordUtenteDAO (String email, String nuovaPassword) {
        try {
            String query = "UPDATE utente SET pw= ? WHERE email = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, nuovaPassword);
            preparedStatement.setString(2, email);

            int rowsUpdated = preparedStatement.executeUpdate();

            /*Dato che la query ci restituisce il numero di update eseguiti
            * verifichiamo che l'operazione sia andata a buon fine (se maggiore di uno)*/

            return rowsUpdated > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Errore nell' update della password nella tabella utente");
            return false;
        }
    }

    //Metodo per verificare se mail e username già utilizzati

    public boolean verifyMailUsername(String username, String email) {

        String usernameDB;
        String emailDB;

        try {
            String query = "SELECT * FROM Utente WHERE username = ? OR email = ?" ;

            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, username);
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

    //Metodo per recuperare username legato alla mail

    public String usernameRecovery(String email) {

        String usernameRecuperato = null;

        try {
            String query = "SELECT username FROM utente WHERE email = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                usernameRecuperato = resultSet.getString("username");
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero username tramite email");
            e.printStackTrace();
        }

        return usernameRecuperato;

    }

    //////////////////////INSERT UTENTE/////////////////////7

    public boolean newUserRegister(String username, String email, char[] password) {

        String query = "INSERT INTO UTENTE (username, email, pw) VALUES (?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, String.valueOf(password));

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }

    }

    public Utente recuperoUtente(String username) {

        Utente utenteTrovato = null;

        try {
            String query = "SELECT * FROM utente WHERE username = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String usernameUtente = resultSet.getString("username");
                String emailUtente = resultSet.getString("email");
                String pwUtente = resultSet.getString("pw");

                utenteTrovato = new Utente(usernameUtente, emailUtente, pwUtente.toCharArray());
            }



        } catch (SQLException e) {
            System.out.println("Errore nel recupero dell'utente mediante il suo username");
            e.printStackTrace();
        }

        return  utenteTrovato;

    }


}





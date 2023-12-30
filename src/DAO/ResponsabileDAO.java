package DAO;

import DTO.Responsabile;
import DTO.Sede;
import UTILITIES.Controller;
import UTILITIES.DB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponsabileDAO {
    private static DB_Connection connessioneDB;
    Controller currController;

    public ResponsabileDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();

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
    public boolean verifyMatricolaMailR(String matricola, String email) {

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

    public Responsabile recuperoResponsabile (String codR_fk) {

        Responsabile responsabile = null;
        String matricola;
        String nome;
        String cognome;
        String codfis;
        String telefono;
        String email;
        char[] pw = new char[0];
        int cods_fk;

        try {

            String query = "SELECT * FROM Responsabile WHERE matricola = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, codR_fk);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                matricola = resultSet.getString("matricola");
                nome = resultSet.getString("nome");
                cognome = resultSet.getString("cognome");
                codfis = resultSet.getString("codfiscale");
                telefono = resultSet.getString("telefono");
                email = resultSet.getString("email");
                String password = resultSet.getString("pw");

                if (password != null) {

                    pw = password.toCharArray();

                }

                cods_fk = resultSet.getInt("codS_fk");
                SedeDAO sedeDao = new SedeDAO(currController);
                Sede sede = sedeDao.recuperoSede(cods_fk); // Metodo per ottenere l'oggetto Sede a partire dall'ID


                responsabile = new Responsabile(matricola, nome, cognome,codfis,telefono,email,pw,sede);

            }

        } catch (SQLException e) {

            System.out.println("Errore nella ricerca del responsabile");
            e.printStackTrace();

        }

        return responsabile;

    }

    //////////////////////INSERT RESPONSABILE/////////////////////7

    public boolean newResponsableRegister(String matricola, String nome, String cognome, String email, char[] password, Sede cods_fk) {

        String query = "INSERT INTO RESPONSABILE (matricola, nome, cognome,email, pw,cods_fk) VALUES (?, ?, ?, ?, ?,?)";

        try {
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, matricola);
            preparedStatement.setString(2, nome);
            preparedStatement.setString(3, cognome);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, String.valueOf(password));
            preparedStatement.setInt(6, cods_fk.getCodS());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

}
package DAO;

import DTO.Responsabile;
import DTO.Sede;
import UTILITIES.Controller;
import UTILITIES.DB_Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ResponsabileDAO {
    private static DB_Connection DBConnection;
    Controller currController;

    public ResponsabileDAO(Controller controller) {

        currController = controller;

        DBConnection = DB_Connection.getConnessione();

    }

    //////////////////////////RECUPERO CREDENZIALI///////////////////

    public boolean credentialsVerificationDAO(String matriculation, char[] password) {

        /*Utilizziamo la classe PreparedStatement per impedire le SQLInjection*/

        try {

            String query = "SELECT * FROM responsabile WHERE matricola= ? AND pw = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, matriculation);
            preparedStatement.setString(2, String.valueOf(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                if (resultSet.getString("matricola").equals(matriculation) && resultSet.getString("pw").equals(String.valueOf(password))) {
                return true;

                }

            }


        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    public static String [] nameSurnameRecovery(String matriculation, char[] password) {

        String[] information = new String[2];

        try {
            String query = "SELECT nome, cognome FROM responsabile WHERE matricola = ? and pw = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, matriculation);
            preparedStatement.setString(2,String.valueOf(password));
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                information[0] = resultSet.getString("nome");
                information[1] = resultSet.getString("cognome");

            }

        } catch (SQLException e) {

            System.out.println("Errore nel recupero nome cognome tramite matricola");
            e.printStackTrace();

        }

        return information;

    }

    public boolean managerMailCheckDAO(String email) {

        try {

            String query = "SELECT * FROM responsabile WHERE email = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return true;

            }

        } catch (SQLException e) {

            e.printStackTrace();

        }

        return false;

    }

    /////////////////////////////////RECUPERO PASSWORD////////////////////////////
    //Metodo per recuperare la password dal database in base alla mail
    public String managerPasswordRecoveryDAO(String email) {

        try {

            String query = "SELECT pw FROM responsabile WHERE email = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                return resultSet.getString("pw");

            }

        } catch (SQLException e) {

            System.out.println("Errore nel recupero password del responsabile");
            e.printStackTrace();

        }

        return " ";

    }

    //Metodo per aggiornare la password di un utente presente nel database
    public boolean managerPasswordUpdateDAO(String email, String newPassword) {

        try {

            String query = "UPDATE responsabile SET pw= ? WHERE email = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, newPassword);
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
    public boolean verifyManagerMailMatriculationDAO(String matriculation, String email) {

        try {

            String query = "SELECT * FROM responsabile WHERE matricola = ? OR email = ?" ;
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, matriculation);
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

    public Responsabile managerRecoveryDAO(String codR_fk) {
        
        String matriculation;
        String name;
        String surname;
        String taxCode;
        String telephoneNumber;
        String email;
        int cods_fk;

        try {

            String query = "SELECT * FROM Responsabile WHERE matricola = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, codR_fk);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                matriculation = resultSet.getString("matricola");
                name = resultSet.getString("nome");
                surname = resultSet.getString("cognome");
                taxCode = resultSet.getString("codfiscale");
                telephoneNumber = resultSet.getString("telefono");
                email = resultSet.getString("email");
                String password = resultSet.getString("pw");

                if (password != null) {

                    cods_fk = resultSet.getInt("codS_fk");
                    SedeDAO sedeDao = new SedeDAO(currController);
                    Sede headquarters = sedeDao.recuperoSede(cods_fk); // Metodo per ottenere l'oggetto Sede a partire dall'ID

                    return new Responsabile(matriculation, name, surname, taxCode, telephoneNumber, email, password.toCharArray(), headquarters);

                }

            }

        } catch (SQLException e) {

            System.out.println("Errore nella ricerca del responsabile");
            e.printStackTrace();

        }

        return null;

    }

    //////////////////////INSERT RESPONSABILE/////////////////////7

    public boolean newManagerSignIn(String matriculation, String name, String surname, String email, char[] password, Sede headquartersFk) {

        String query = "INSERT INTO RESPONSABILE (matricola, nome, cognome,email, pw,cods_fk) VALUES (?, ?, ?, ?, ?,?)";

        try {
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, matriculation);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, surname);
            preparedStatement.setString(4, email);
            preparedStatement.setString(5, String.valueOf(password));
            preparedStatement.setInt(6, headquartersFk.getCodS());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;

        }

    }

}

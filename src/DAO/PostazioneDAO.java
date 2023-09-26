package DAO;

import DTO.Sede;
import UTILITIES.*;
import DTO.Postazione;
import DTO.Laboratorio;

import java.sql.*;

public class PostazioneDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public PostazioneDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();


    }

    public Postazione recuperoPostazione(String codPostazione_fk) {

        Postazione postazione = null;
        String codPostazione = null;

        try {
            String query = "SELECT * FROM postazione WHERE codpostazione = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, codPostazione_fk);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                codPostazione = resultSet.getString("codpostazione");
                postazione = new Postazione(codPostazione, null, null);
            }



        } catch (SQLException e) {
            System.out.println("Errore nella ricerca della postazione");
            e.printStackTrace();
        }

        return  postazione;

    }

    public Integer recuperoCodlConPostazione(String codP) {

        int codL = -1;

        try {
            String query = "SELECT codl_fk FROM postazione WHERE codpostazione = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, codP);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                 codL = resultSet.getInt("codl_fk");

                return codL;
            } else {

                System.out.println("Non recuperato");

            }

        } catch (SQLException e) {
            System.out.println("Errore nella ricerca del codl mediante il codp");
            e.printStackTrace();
        }

            return codL;
    }


}

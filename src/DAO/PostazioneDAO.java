package DAO;

import DTO.Sede;
import UTILITIES.*;
import DTO.Postazione;
import DTO.Laboratorio;

import java.sql.*;

public class PostazioneDAO {

    private final DB_Connection connessioneDB;
    Controller currController;


    public PostazioneDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();

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

}

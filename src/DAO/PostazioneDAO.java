package DAO;

import DTO.Sede;
import UTILITIES.*;
import DTO.Postazione;
import DTO.Laboratorio;

import java.sql.*;

public class PostazioneDAO {

    private final DB_Connection DBConnection;
    Controller currController;


    public PostazioneDAO(Controller controller) {

        currController = controller;

        DBConnection = DB_Connection.getConnessione();

    }

    public Postazione workstationRecovery(String codPostazione_fk) {

        try {

            String query = "SELECT * FROM postazione WHERE codpostazione = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, codPostazione_fk);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String stationsCode = resultSet.getString("codpostazione");
                int seatNumbers = resultSet.getInt("num_posti");
                String laboratoryCodeFk = resultSet.getString("codl_fk");

                return new Postazione(stationsCode, seatNumbers, currController.labRecoveryWithCodeC(laboratoryCodeFk));

            }

        } catch (SQLException e) {

            System.out.println("Error while searching for the workstation");
            e.printStackTrace();

        }

        return  null;

    }

}

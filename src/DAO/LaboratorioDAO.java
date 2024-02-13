package DAO;

import UTILITIES.*;
import DTO.Laboratorio;
import java.sql.*;
import java.time.LocalTime;


public class LaboratorioDAO {
    private final DB_Connection DBConnection;

    Controller currController;

    public LaboratorioDAO(Controller controller) {

        currController = controller;

        DBConnection = DB_Connection.getConnessione();

    }

    public Laboratorio labRecoveryDAO(String codLab) {

        try {

            String query = "SELECT * FROM laboratorio WHERE codl = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1, codLab);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String codL = resultSet.getString("codl");
                String description = resultSet.getString("descrizione");
                LocalTime openingTime = resultSet.getTime("orario_apertura").toLocalTime();
                LocalTime closingTime = resultSet.getTime("orario_chiusura").toLocalTime();
                int techniciansNumber = resultSet.getInt("num_tecnici");
                int stationsNumber = resultSet.getInt("num_postazioni");

                return new Laboratorio(codL, description, openingTime, closingTime, techniciansNumber, stationsNumber);

            }


        } catch (SQLException e) {

            e.printStackTrace();

        }

        return  null;

    }

}
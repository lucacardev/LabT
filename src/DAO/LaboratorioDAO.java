package DAO;

import UTILITIES.*;
import DTO.Laboratorio;
import java.sql.*;
import java.time.LocalTime;


public class LaboratorioDAO {
    private final DB_Connection connessioneDB;

    Controller currController;

    public LaboratorioDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();

    }

    public Laboratorio recuperoLaboratorio(String codLab) {

        try {

            String query = "SELECT * FROM laboratorio WHERE codl = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, codLab);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                String codL = resultSet.getString("codl");
                String descrizione = resultSet.getString("descrizione");
                LocalTime orarioApertura = resultSet.getTime("orario_apertura").toLocalTime();
                LocalTime orarioChiusura = resultSet.getTime("orario_chiusura").toLocalTime();
                int numTecnici = resultSet.getInt("num_tecnici");
                int numPostazioni = resultSet.getInt("num_postazioni");

                return new Laboratorio(codL, descrizione, orarioApertura, orarioChiusura, numTecnici, numPostazioni);

            }


        } catch (SQLException e) {

            System.out.println("Errore nella ricerca della sede");
            e.printStackTrace();

        }

        return  null;

    }

}
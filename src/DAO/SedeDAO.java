package DAO;

import UTILITIES.*;
import DTO.Sede;
import DTO.Laboratorio;

import java.sql.*;

public class SedeDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public SedeDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();


    }

    public Sede recuperoSede(Integer codSede_fk) {

        Sede sede = null;
        int codSede;
        String nomeSede;

        try {
            String query = "SELECT * FROM sede WHERE cods = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setInt(1, codSede_fk);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                codSede = resultSet.getInt("cods");
                nomeSede = resultSet.getString("nome");
                sede = new Sede(codSede, nomeSede , null, null);
            }



        } catch (SQLException e) {
            System.out.println("Errore nella ricerca della sede");
            e.printStackTrace();
        }

            return  sede;

    }





    //Metodo che codifica il nome della sede in codice

    public int codificaSedeDAO (String nomeSede) {

        int codSede = -1;

        try {
            String query = "SELECT cods FROM sede WHERE nome ILIKE ?"; //Utilizziamo ILIKE per togliere il case sensitive
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1, '%' + nomeSede + '%');
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                codSede = resultSet.getInt("cods");
            }

        } catch (SQLException e) {
            System.out.println("Errore nella codifica della sede");
            e.printStackTrace();
        }

        return codSede;


    }



}
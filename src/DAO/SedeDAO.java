package DAO;

import UTILITIES.*;
import DTO.Sede;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
                sede = new Sede(codSede, nomeSede , null);
            }



        } catch (SQLException e) {
            System.out.println("Errore nella ricerca della sede");
            e.printStackTrace();
        }

            return  sede;

    }


    //Metodo che codifica il nome della sede in codice

    public Integer codificaSedeDAO (String nomeSede) {

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

    public List<Sede> recuperaListaSediDalDB() {
        List<Sede> sedi = new ArrayList<>();

        try {
            String query = "SELECT * FROM sede";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int codSede = resultSet.getInt("codS");
                String nomeSede = resultSet.getString("nome");
                String indirizzo = resultSet.getString("indirizzo");
                Sede sede = new Sede(codSede, nomeSede, indirizzo);
                sedi.add(sede);
            }
        } catch (SQLException e) {
            System.out.println("Errore durante il recupero delle sedi dal DB");
            e.printStackTrace();
        }

        return sedi;
    }
}
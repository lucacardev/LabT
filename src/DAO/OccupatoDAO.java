package DAO;

import UTILITIES.Controller;
import UTILITIES.DB_Connection;

import java.sql.Statement;

public class OccupatoDAO {
    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public OccupatoDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();
    }
}

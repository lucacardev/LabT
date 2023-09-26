package DAO;

import UTILITIES.*;
import DTO.Sede;
import DTO.Laboratorio;

import java.sql.*;

public class AppartenenzaDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public AppartenenzaDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();


    }




}

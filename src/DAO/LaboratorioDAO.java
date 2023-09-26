package DAO;

import UTILITIES.*;
import DTO.Laboratorio;
import DTO.Appartenenza;
import DTO.Sede;

import java.sql.*;


public class LaboratorioDAO {

    private DB_Connection connessioneDB;
    private Statement statement;

    Controller currController;

    /*Passo il controller come parametro per creare un collegamento tra quest'ultimo e
      la classe DAO*/

    public LaboratorioDAO(Controller controller) {

        currController = controller;

        /*Creo la connessione tra la classe DAO e il database, dopo aver ricevuto la connessione
          definisco lo statement per comunicare mediante le query*/

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();

    }


}

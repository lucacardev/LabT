package DAO;

import DTO.Strumento;
import DTO.Utente;
import UTILITIES.*;
import DTO.Sede;
import DTO.Laboratorio;

import javax.swing.*;
import java.sql.*;

public class PrenotazioneDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public PrenotazioneDAO (Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();


    }

    /////////////////////////////////////INSERIMENTO IN PRENOTAZIONE//////////////////////////////

    /*Utilizziamo TimeStamp per risolvere il problema d'incoerenza causato dal tipo di dato Date del database che è della librerira
    * java.swl.Date e il tipo di dato utilizzato nel codice appartenente alla libreria java.util.Date*/
    public boolean newUserBooking(Timestamp dataPrenotazione, Timestamp oraPrenotazione, Timestamp tempoUtilizzo, Utente username, Strumento strumento) {

        String query = "INSERT INTO PRENOTAZIONE (data_prenotaziones, ora_prenotaziones, tempo_utilizzos, username_fk, codstrumento_fk) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);

            preparedStatement.setTimestamp(1, dataPrenotazione);
            preparedStatement.setTimestamp(2, oraPrenotazione);
            preparedStatement.setTimestamp(3, tempoUtilizzo);
            preparedStatement.setString(4, username.getUsername());
            preparedStatement.setInt(5, strumento.getCodStrumento());

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();

            String errorMassage = e.getMessage();


            if(errorMassage.contains("ERRORE: Lo strumento non può essere prenotato per quell'ora.Il laboratorio è\n" +
                    "chiuso.")) {

                JOptionPane.showMessageDialog(null,"ATTENZIONE! Il laboratorio a quell'ora è chiuso \n" +
                        "Apertura 8:00 - Chiusura 20:00");

            } else if(errorMassage.contains("ERRORE: Durata minima di utilizzo deve essere superiore a 1 minuto.")) {

                JOptionPane.showMessageDialog(null, "ATTENZIONE! Il tempo di utilizzo di uno strumento" +
                        " deve essere superiore a 1 minuto!");

            } else if (errorMassage.contains("ERRORE: Strumento già prenotato per questo orario")) {

                JOptionPane.showMessageDialog(null, "ATTENZIONE! Strumento già prenotato per questo orario " +
                        "in quel giorno");

            }


            return false;
        }
    }


}




package DAO;

import DTO.*;
import UTILITIES.*;

import javax.swing.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;


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


            if(errorMassage.contains("ERRORE: Lo strumento non può essere prenotato per quell'ora.Il laboratorio è chiuso.")) {

                JOptionPane.showMessageDialog(null,"Lo strumento non può essere prenotato per quell'ora.Il laboratorio è chiuso.");

            } else if(errorMassage.contains("ERRORE: Durata minima di utilizzo deve essere superiore a 1 minuto.")) {

                JOptionPane.showMessageDialog(null, "ATTENZIONE! Il tempo di utilizzo di uno strumento" +
                        " deve essere superiore a 1 minuto!");

            } else if(errorMassage.contains("ERRORE: Lo strumento non può essere prenotato per quell'ora. Il laboratorio è chiuso.")) {

                JOptionPane.showMessageDialog(null,"Lo strumento non può essere prenotato per quell'ora.Il laboratorio è chiuso.");

            }

            else if (errorMassage.contains("ERRORE: Strumento già prenotato per questo orario")) {

                JOptionPane.showMessageDialog(null, "ATTENZIONE! Strumento già prenotato per questo orario " +
                        "in quel giorno");

            } else if (errorMassage.contains("Errore, stai provando a prenotare uno strumento per un'ora passata")) {

                JOptionPane.showMessageDialog(null, "Errore, stai provando a prenotare uno strumento per un'ora passata");

            }

            return false;
        }
    }

    public List<Prenotazione> recuperoMiePrenotazioni(Utente utenteLoggato) {

        List<Prenotazione> miePrenotazioni = new ArrayList<>();
        Prenotazione risultatoPrenotazioni = null;

        UtenteDAO utenteDAO = new UtenteDAO(currController);
        StrumentoDAO strumentoDAO = new StrumentoDAO(currController);

        try {
            String query = "SELECT * FROM prenotazione WHERE username_fk = ? ORDER BY data_prenotaziones DESC";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setString(1,  utenteLoggato.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();


            //Se è presente una riga successiva allora continua a ciclare
            //Prendiamo gli attributi dalle rispettive colonne

            while (resultSet.next()) {

                int codPrenotazione = resultSet.getInt("codprenotazione");
                Date dataPrenotazioneS = resultSet.getDate("data_prenotaziones");
                Time oraPrenotazioneS = resultSet.getTime("ora_prenotaziones");
                Time tempoUtilizzoS = resultSet.getTime("tempo_utilizzos");
                String usernameFk = resultSet.getString("username_fk");
                Integer codStrumntoFk = resultSet.getInt("codstrumento_fk");

                //Recupero di Utente e Strumento
                Utente utente = utenteDAO.recuperoUtente(usernameFk);
                Strumento strumento = strumentoDAO.recuperoStrumento(codStrumntoFk);

                //Creazione di un oggetto prneotazione da aggiungere alla lista
                 risultatoPrenotazioni = new Prenotazione(codPrenotazione, dataPrenotazioneS, oraPrenotazioneS, tempoUtilizzoS, utente, strumento);
                 miePrenotazioni.add(risultatoPrenotazioni);

            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero delle preontazioni in base all'username");
            e.printStackTrace();
        }

        return miePrenotazioni;

    }

    //Metodo per recuperare le prenotazioni in base allo strumento per calendario
    public List<Prenotazione> recuperoPrenStrumenti(Strumento strumento) {

        List<Prenotazione> prenStrumenti = new ArrayList<>();
        Prenotazione risultatoPrenotazioni = null;

        StrumentoDAO strumentoDAO = new StrumentoDAO(currController);

        try {
            String query = "SELECT * FROM prenotazione WHERE codstrumento_fk = ? ORDER BY data_prenotaziones DESC;";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setInt(1,  strumento.getCodStrumento());
            ResultSet resultSet = preparedStatement.executeQuery();


            //Se è presente una riga successiva allora continua a ciclare
            //Prendiamo gli attributi dalle rispettive colonne

            while (resultSet.next()) {

                Date dataPrenotazioneS = resultSet.getDate("data_prenotaziones");
                Time oraPrenotazioneS = resultSet.getTime("ora_prenotaziones");
                Time tempoUtilizzoS = resultSet.getTime("tempo_utilizzos");

                //Creazione di un oggetto prneotazione da aggiungere alla lista
                risultatoPrenotazioni = new Prenotazione(dataPrenotazioneS, oraPrenotazioneS, tempoUtilizzoS, strumento);
                prenStrumenti.add(risultatoPrenotazioni);

            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero delle preontazioni in base al cod strumento");
            e.printStackTrace();
        }

        return prenStrumenti;



    }

    public void eliminaPrenotazioneDAO(int codPrenotazione) {

        try {
            String query = "DELETE FROM prenotazione WHERE codprenotazione = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setInt(1,  codPrenotazione);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {
            System.out.println("Errore nell'eliminazione della prenotazione ");
            e.printStackTrace();
        }

    }

    public boolean modificaMiaPrenotazioneDAO (int codPrenotazione, Timestamp dataNuova, Timestamp oraNuova, Timestamp tempoUtilizzoNuovo) {

        try {
            String query = "UPDATE prenotazione SET " +
                    "data_prenotaziones = ?, " +
                    "ora_prenotaziones = ?, " +
                    "tempo_utilizzos = ? " +
                    "WHERE codprenotazione = ?";

            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setTimestamp(1, dataNuova);
            preparedStatement.setTimestamp(2,  oraNuova);
            preparedStatement.setTimestamp(3,  tempoUtilizzoNuovo);
            preparedStatement.setInt(4,  codPrenotazione);
            preparedStatement.executeUpdate();

            return true;


        } catch (SQLException e) {
            System.out.println("Errore nella modifica della prenotazione dell'utente");
            e.printStackTrace();

            String errorMassage = e.getMessage();


            if(errorMassage.contains("ERRORE: Lo strumento non può essere prenotato per quell'ora. Il laboratorio è chiuso.")) {

                JOptionPane.showMessageDialog(null,"Lo strumento non può essere prenotato per quell'ora.Il laboratorio è chiuso.");

            } else if(errorMassage.contains("ERRORE: Durata minima di utilizzo deve essere superiore a 1 minuto.")) {

                JOptionPane.showMessageDialog(null, "ATTENZIONE! Il tempo di utilizzo di uno strumento" +
                        " deve essere superiore a 1 minuto!");

            } else if (errorMassage.contains("ERRORE: Strumento già prenotato per questo orario")) {

                JOptionPane.showMessageDialog(null, "ATTENZIONE! Strumento già prenotato per questo orario " +
                        "in quel giorno");

            } else if (errorMassage.contains("Errore, stai provando a prenotare uno strumento per un'ora passata")) {

                JOptionPane.showMessageDialog(null, "Errore, stai provando a prenotare uno strumento per un'ora passata");

            }

            return false;
        }


    }

}




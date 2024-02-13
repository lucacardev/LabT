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
    private final DB_Connection DBConnection;
    Controller currController;


    public PrenotazioneDAO (Controller controller) {

        currController = controller;

        DBConnection = DB_Connection.getConnessione();

    }

    /////////////////////////////////////INSERIMENTO IN PRENOTAZIONE//////////////////////////////

    /*Utilizziamo TimeStamp per risolvere il problema d'incoerenza causato dal tipo di dato Date del database che è della librerira
    * java.swl.Date e il tipo di dato utilizzato nel codice appartenente alla libreria java.util.Date*/
    public boolean newUserBooking(Timestamp bookDate, Timestamp bookHours, Timestamp usageTime, Utente username, Strumento strumento) {

        String query = "INSERT INTO PRENOTAZIONE (data_prenotaziones, ora_prenotaziones, tempo_utilizzos, username_fk, codstrumento_fk) " +
                "VALUES (?, ?, ?, ?, ?)";

        try {

            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setTimestamp(1, bookDate);
            preparedStatement.setTimestamp(2, bookHours);
            preparedStatement.setTimestamp(3, usageTime);
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

            } else if (errorMassage.contains("ERRORE: Errore, stai provando a prenotare uno strumento per una data  passata")) {

                JOptionPane.showMessageDialog(null, "Errore, stai provando a prenotare uno strumento per una data passata");

            } else if (errorMassage.contains("Errore, stai provando a prenotare uno strumento per un'ora passata")) {

                JOptionPane.showMessageDialog(null, "Errore, stai provando a prenotare uno strumento per un'ora passata");

            }

            return false;
        }
    }

    public List<Prenotazione> myBookingRecoveryDAO(Utente loggedUser) {

        List<Prenotazione> myBookings = new ArrayList<>();

        UtenteDAO utenteDAO = new UtenteDAO(currController);
        StrumentoDAO strumentoDAO = new StrumentoDAO(currController);

        try {
            String query = "SELECT * FROM prenotazione WHERE username_fk = ? ORDER BY data_prenotaziones DESC";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setString(1,  loggedUser.getUsername());
            ResultSet resultSet = preparedStatement.executeQuery();

            //Se è presente una riga successiva allora continua a ciclare
            //Prendiamo gli attributi dalle rispettive colonne

            while (resultSet.next()) {

                int bookingCode = resultSet.getInt("codprenotazione");
                Date bookDateS = resultSet.getDate("data_prenotaziones");
                Time bookHoursS = resultSet.getTime("ora_prenotaziones");
                Time usageTimeS = resultSet.getTime("tempo_utilizzos");
                String usernameFk = resultSet.getString("username_fk");
                Integer toolCodeFk = resultSet.getInt("codstrumento_fk");

                //Recupero di Utente e Strumento
                Utente user = utenteDAO.recuperoUtente(usernameFk);
                Strumento tool = strumentoDAO.toolRecovery(toolCodeFk);

                //Creazione di un oggetto prneotazione da aggiungere alla lista
                 new Prenotazione(bookingCode, bookDateS, bookHoursS, usageTimeS, user, tool);
                myBookings.add(new Prenotazione(bookingCode, bookDateS, bookHoursS, usageTimeS, user, tool));

            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero delle preontazioni in base all'username");
            e.printStackTrace();
        }

        return myBookings;

    }

    //Metodo per recuperare le prenotazioni in base allo strumento per calendario
    public List<Prenotazione> toolsBookingsRecoveryC(Strumento tool) {

        List<Prenotazione> toolBookings = new ArrayList<>();

        try {
            String query = "SELECT * FROM prenotazione WHERE codstrumento_fk = ? ORDER BY data_prenotaziones DESC;";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setInt(1,  tool.getCodStrumento());
            ResultSet resultSet = preparedStatement.executeQuery();


            //Se è presente una riga successiva allora continua a ciclare
            //Prendiamo gli attributi dalle rispettive colonne

            while (resultSet.next()) {

                Date bookDateS = resultSet.getDate("data_prenotaziones");
                Time bookHoursS = resultSet.getTime("ora_prenotaziones");
                Time usageTimeS = resultSet.getTime("tempo_utilizzos");

                //Creazione di un oggetto prneotazione da aggiungere alla lista

                toolBookings.add(new Prenotazione(bookDateS, bookHoursS, usageTimeS, tool));

            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero delle preontazioni in base al cod strumento");
            e.printStackTrace();
        }

        return toolBookings;

    }

    public void bookingDeleteDAO(int toolCode) {

        try {
            String query = "DELETE FROM prenotazione WHERE codprenotazione = ?";
            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setInt(1,  toolCode);
            preparedStatement.executeUpdate();


        } catch (SQLException e) {

            System.out.println("Errore nell'eliminazione della prenotazione ");
            e.printStackTrace();

        }

    }

    public boolean changeMyBookingDAO (int bookingCode, Timestamp newDate, Timestamp newHour, Timestamp newUsageTime) {

        try {

            String query = "UPDATE prenotazione SET " +
                    "data_prenotaziones = ?, " +
                    "ora_prenotaziones = ?, " +
                    "tempo_utilizzos = ? " +
                    "WHERE codprenotazione = ?";

            PreparedStatement preparedStatement = DBConnection.getPreparedStatement(query);
            preparedStatement.setTimestamp(1, newDate);
            preparedStatement.setTimestamp(2,  newHour);
            preparedStatement.setTimestamp(3,  newUsageTime);
            preparedStatement.setInt(4,  bookingCode);
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

            } else if(errorMassage.contains("ERRORE: Errore, stai provando a prenotare uno strumento per una data  passata")) {

                JOptionPane.showMessageDialog(null, "ERRORE: Errore, stai provando a prenotare uno strumento per una data passata");


            } else if (errorMassage.contains("Errore, stai provando a prenotare uno strumento per un'ora passata")) {

                JOptionPane.showMessageDialog(null, "Errore, stai provando a prenotare uno strumento per un'ora passata");

            }

            return false;

        }

    }

}




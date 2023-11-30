package DAO;

import DTO.Postazione;
import DTO.Sede;
import DTO.Strumento;
import DTO.Utente;
import UTILITIES.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StrumentoDAO {

    private DB_Connection connessioneDB;
    private Statement statement;
    Controller currController;


    public StrumentoDAO(Controller controller) {

        currController = controller;

        connessioneDB = DB_Connection.getConnessione();
        statement = connessioneDB.getStatement();


    }

    //Metodo per recuperare gli strumenti in base al nome della sede

    public List<Strumento> recuperoStrumenti (String nomeSede, String descrizioneStrumento) {

        List<Strumento> strumenti = new ArrayList<>();

        //Caso in cui sia sede che descrizione non sono vuoti

        if(!nomeSede.isEmpty() && !descrizioneStrumento.isEmpty()) {

            SedeDAO sedeDAO = new SedeDAO(currController);
            PostazioneDAO postazioneDAO = new PostazioneDAO(currController);

            try {
                String query = "SELECT * FROM strumento JOIN sede ON cods = codsede_fk WHERE descrizione ILIKE ? AND codsede_fk = ?";
                PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
                preparedStatement.setString(1, '%' + descrizioneStrumento + '%');
                preparedStatement.setInt(2, sedeDAO.codificaSedeDAO(nomeSede));
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int codStrumento = resultSet.getInt("codstrumento");
                    String caratteristiche_tecniche = resultSet.getString("caratteristiche_tecniche");
                    String descrizione = resultSet.getString("descrizione");
                    Time tempoMaxUso = resultSet.getTime("tempomaxuso");
                    Integer codSede_fk = resultSet.getInt("codsede_fk");
                    String codPostazione_fk = resultSet.getString("codpostazione_fk");


                    //Recupero della Sede tramite l'attributo codsede_fk dello strumento
                    //Recupero allo stesso modo anche la postazione tramite metodi delle proprie classi DAO
                    Sede sede = sedeDAO.recuperoSede(codSede_fk);
                    Postazione postazione = postazioneDAO.recuperoPostazione(codPostazione_fk);


                    // Creazione di un oggetto Strumento e aggiunta alla lista
                    Strumento strumento = new Strumento(codStrumento, caratteristiche_tecniche, descrizione, tempoMaxUso, postazione, sede);
                    strumenti.add(strumento);
                }

            } catch (SQLException e) {
                System.out.println("Errore nella ricerca degli strumenti tramite sede e descrizione");
                e.printStackTrace();
            }




        }

        else if(!nomeSede.isEmpty()) {
            SedeDAO sedeDAO = new SedeDAO(currController);
            PostazioneDAO postazioneDAO = new PostazioneDAO(currController);

            try {
                String query = "SELECT * FROM strumento WHERE codsede_fk = ?";
                PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
                preparedStatement.setInt(1, sedeDAO.codificaSedeDAO(nomeSede));
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {
                    int codStrumento = resultSet.getInt("codstrumento");
                    String caratteristiche_tecniche = resultSet.getString("caratteristiche_tecniche");
                    String descrizione = resultSet.getString("descrizione");
                    Time tempoMaxUso = resultSet.getTime("tempomaxuso");
                    Integer codSede_fk = resultSet.getInt("codsede_fk");
                    String codPostazione_fk = resultSet.getString("codpostazione_fk");


                    //Recupero della Sede tramite l'attributo codsede_fk dello strumento
                    //Recupero allo stesso modo anche la postazione tramite metodi delle proprie classi DAO
                    Sede sede = sedeDAO.recuperoSede(codSede_fk);
                    Postazione postazione = postazioneDAO.recuperoPostazione(codPostazione_fk);


                    // Creazione di un oggetto Strumento e aggiunta alla lista
                    Strumento strumento = new Strumento(codStrumento, caratteristiche_tecniche, descrizione, tempoMaxUso, postazione, sede);
                    strumenti.add(strumento);
                }

            } catch (SQLException e) {
                System.out.println("Errore nella ricerca degli strumenti tramite sede");
                e.printStackTrace();
            }

            //Caso in cui descrizione è diverso da null
        } else if(!descrizioneStrumento.isEmpty()) {

            SedeDAO sedeDAO = new SedeDAO(currController);
            PostazioneDAO postazioneDAO = new PostazioneDAO(currController);

            try {
                String query = "SELECT * FROM strumento WHERE descrizione ILIKE ?";
                PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
                preparedStatement.setString(1, '%' + descrizioneStrumento + '%');
                ResultSet resultSet = preparedStatement.executeQuery();

                while (resultSet.next()) {

                    int codStrumento = resultSet.getInt("codstrumento");
                    String caratteristiche_tecniche = resultSet.getString("caratteristiche_tecniche");
                    String descrizione = resultSet.getString("descrizione");
                    Time tempoMaxUso = resultSet.getTime("tempomaxuso");
                    Integer codSede_fk = resultSet.getInt("codsede_fk");
                    String codPostazione_fk = resultSet.getString("codpostazione_fk");


                    //Recupero della Sede tramite l'attributo codsede_fk dello strumento
                    //Recupero allo stesso modo anche la postazione tramite metodi delle proprie classi DAO
                    Sede sede = sedeDAO.recuperoSede(codSede_fk);
                    Postazione postazione = postazioneDAO.recuperoPostazione(codPostazione_fk);


                    // Creazione di un oggetto Strumento e aggiunta alla lista
                    Strumento strumento = new Strumento(codStrumento, caratteristiche_tecniche, descrizione, tempoMaxUso, postazione, sede);
                    strumenti.add(strumento);
                }

            } catch (SQLException e) {
                System.out.println("Errore nella ricerca degli strumenti tramite descrizione");
                e.printStackTrace();

        }


    }
        return strumenti;
    }

    //Metodo per recuperare tutti gli strumenti

    public List<Strumento> recuperoTuttiStrumenti () {

        List<Strumento> allStruments = new ArrayList<>();
        SedeDAO sedeDAO = new SedeDAO(currController);
        PostazioneDAO postazioneDAO = new PostazioneDAO(currController);

        try {
            String query = "SELECT * FROM strumento";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                int codStrumento = resultSet.getInt("codstrumento");
                String caratteristiche_tecniche = resultSet.getString("caratteristiche_tecniche");
                String descrizione = resultSet.getString("descrizione");
                Time tempoMaxUso = resultSet.getTime("tempomaxuso");
                Integer codSede_fk = resultSet.getInt("codsede_fk");
                String codPostazione_fk = resultSet.getString("codpostazione_fk");


                //Recupero della Sede tramite l'attributo codsede_fk dello strumento
                //Recupero allo stesso modo anche la postazione tramite metodi delle proprie classi DAO
                Sede sede = sedeDAO.recuperoSede(codSede_fk);
                Postazione postazione = postazioneDAO.recuperoPostazione(codPostazione_fk);


                // Creazione di un oggetto Strumento e aggiunta alla lista
                Strumento strumento = new Strumento(codStrumento, caratteristiche_tecniche, descrizione, tempoMaxUso, postazione, sede);
                allStruments.add(strumento);
            }

        } catch (SQLException e) {
            System.out.println("Errore nel recupero di tutti gli strumenti");
            e.printStackTrace();
        }

        return allStruments;

    }

    public Strumento recuperoStrumento(Integer codStrumento) {

        Strumento strumentoTrovato = null;
        SedeDAO sedeDAO = new SedeDAO(currController);
        PostazioneDAO postazioneDAO = new PostazioneDAO(currController);

        try {
            String query = "SELECT * FROM strumento WHERE codstrumento = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setInt(1, codStrumento);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                Integer codStrumentoRec = resultSet.getInt("codstrumento");
                String caratteristicheTecnicheRec = resultSet.getString("caratteristiche_tecniche");
                String descrizioneRec = resultSet.getString("descrizione");
                Time tempoMaxUsoRec = resultSet.getTime("tempomaxuso");
                Integer codSede_fk = resultSet.getInt("codsede_fk");
                String codPostazione_fk = resultSet.getString("codpostazione_fk");


                //Recupero della Sede tramite l'attributo codsede_fk dello strumento
                //Recupero allo stesso modo anche la postazione tramite metodi delle proprie classi DAO
                Sede sede = sedeDAO.recuperoSede(codSede_fk);
                Postazione postazione = postazioneDAO.recuperoPostazione(codPostazione_fk);


                strumentoTrovato = new Strumento(codStrumentoRec, caratteristicheTecnicheRec, descrizioneRec,
                                        tempoMaxUsoRec, postazione, sede);
            }


        } catch (SQLException e) {
            System.out.println("Errore nel recupero dello strumento mediante il suo codice");
            e.printStackTrace();
        }

        return  strumentoTrovato;

    }

    public Time getStrumentHour(Strumento strumento) {

        Time tempoMaxUso = null;

        try {
            String query = "SELECT tempomaxuso FROM strumento WHERE codstrumento = ?";
            PreparedStatement preparedStatement = connessioneDB.getPreparedStatement(query);
            preparedStatement.setInt(1, strumento.getCodStrumento());
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {

                tempoMaxUso = resultSet.getTime("tempomaxuso");
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return tempoMaxUso;
    }

}
package GUI;

import DAO.PrenotazioneDAO;
import DTO.*;
import DAO.SedeDAO;
import UTILITIES.Controller;
import UTILITIES.EmailSender;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/*Nonostante nel nome della classe ci sia la parola Frame quest'ultima è un JDialog dato che non vogliamo che aperta la finestra l'utente
* possa selezionare altri strumenti nella finestra principale, causando così errori con le diverse sincronizzazioni del TablePanel, JDialog
* ci facilita questa impedizione, necessità però di ricevere il Frame come parent*/

public class BookingFrame extends JDialog{

    private final JPanel mainPanel = new JPanel();
    private static final JLabel dataText = new JLabel("Data: ");
    private static final JLabel oraText = new JLabel("Ora: ");
    private  static final JLabel tempoUtilizzoText = new JLabel("Tempo Utilizzo: ");
    private  static final JLabel codStrumento = new JLabel("Codice Strumento: ");
    private  static final JLabel codPrenotazione = new JLabel("Codice Prenotazione: ");

    private final BtnLayout prenotaButton = new BtnLayout("Prenota");
    private final BtnLayout modificaButton = new BtnLayout("Modifica Prenotazione");

    private Date selectedDate;
    private Date selectedTime;
    private Date selectedUtilizationTime;

    private Date selezionaDataModificata;
    private Date selezionaOraModificata;
    private Date selezionaTempoUtilizzoModificato;

    private JLabel codiceStrumento;
    private JLabel codicePrenotazione;

    private JDateChooser dateChooser;

    private int codL;

    Utente utenteLoggato;

    Controller myController;
    Utente utenteCorrente;
    Strumento strumentoSelezionato;
    Postazione postazioneSelezionata;

    Prenotazione prenotazioneDaModificare;


    public  BookingFrame(JFrame parent, Controller controller , Utente utenteCorrente, Strumento strumentoScelto) {

        super(parent, "Nuova Prenotazione", true);


        myController = controller;
        utenteLoggato = utenteCorrente;
        strumentoSelezionato = strumentoScelto;

        setSize(600,200);

        setLocationRelativeTo(null);

        add(mainPanel);

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        //Testo della data
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(dataText, gbc);

        //Testo dell'ora
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(oraText, gbc);

        //Testo del tempo di utilizzo
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(tempoUtilizzoText, gbc);

        //Testo del codice strumento
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(codStrumento, gbc);


        // Creazione calendario
        dateChooser = new JDateChooser();
        JCalendar calendar = dateChooser.getJCalendar();
        calendar.setMinSelectableDate(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(dateChooser, gbc);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        // Impostiamo di default l'ora del laboratorio
        Date initialTime = new Date();
        initialTime.setHours(8);
        initialTime.setMinutes(0);
        initialTime.setSeconds(0);
        initialTime.setTime((initialTime.getTime() / 1000) * 1000);


        TimeSpinnerModel timeSpinnerModel = new TimeSpinnerModel(initialTime);
        JSpinner timeSpinner = new JSpinner(timeSpinnerModel);

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) timeSpinner.getEditor();
        editor.getTextField().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(timeFormat)));


        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(timeSpinner, gbc);

        // Creazione campo per il tempo di utilizzo
        Date initialUsageTime = new Date();
        if (strumentoSelezionato != null) {
            initialUsageTime.setHours(strumentoSelezionato.getTempoMaxUso().getHours());
            initialUsageTime.setMinutes(strumentoSelezionato.getTempoMaxUso().getMinutes());
            initialUsageTime.setSeconds(0);
            initialUsageTime.setTime((initialUsageTime.getTime() / 1000) * 1000);

        } else {
            initialUsageTime.setHours(0);
            initialUsageTime.setMinutes(0);
            initialUsageTime.setSeconds(0);
        }

        CustomTimeSpinnerModel timeSpinnerModel2 = new CustomTimeSpinnerModel(initialUsageTime);
        JSpinner timeSpinnerTempoUtilizzo = new JSpinner(timeSpinnerModel2);

        JSpinner.DefaultEditor editor2 = (JSpinner.DefaultEditor) timeSpinnerTempoUtilizzo.getEditor();
        SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm");
        editor2.getTextField().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(timeFormat2)));

        gbc.gridx = 2;
        gbc.gridy = 1;
        mainPanel.add(timeSpinnerTempoUtilizzo, gbc);

        // Codice dello strumento
        codiceStrumento = new JLabel("-1");
        gbc.gridx = 3;
        gbc.gridy = 1;
        mainPanel.add(codiceStrumento, gbc);

        // Bottone per prenotare
        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(prenotaButton, gbc);

        prenotaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                selectedDate = dateChooser.getDate();
                selectedTime = (Date) timeSpinner.getValue();
                selectedUtilizationTime = (Date) timeSpinnerTempoUtilizzo.getValue();

                if (selectedDate != null && selectedTime != null && selectedUtilizationTime != null) {
                    Calendar selectedUtilizationCalendar = Calendar.getInstance();
                    Calendar maxUtilizationCalendar = Calendar.getInstance();

                    selectedUtilizationCalendar.setTime(selectedUtilizationTime);
                    maxUtilizationCalendar.setTime(strumentoSelezionato.getTempoMaxUso());

                    /*Il codice seguente serve per far si che non ci siano errori durante il confronto
                    * quindi azzeriamo i millisecondi per i due calendari */

                    selectedUtilizationCalendar.set(Calendar.YEAR, 0);
                    selectedUtilizationCalendar.set(Calendar.MONTH, 0);
                    selectedUtilizationCalendar.set(Calendar.DAY_OF_MONTH, 0);
                    selectedUtilizationCalendar.set(Calendar.MILLISECOND, 0);

                    maxUtilizationCalendar.set(Calendar.YEAR, 0);
                    maxUtilizationCalendar.set(Calendar.MONTH, 0);
                    maxUtilizationCalendar.set(Calendar.DAY_OF_MONTH, 0);
                    maxUtilizationCalendar.set(Calendar.MILLISECOND, 0);

                    if (selectedUtilizationCalendar.getTime().getTime() > maxUtilizationCalendar.getTime().getTime()) {
                        JOptionPane.showMessageDialog(null, "ATTENZIONE! Hai superato il tempo massimo di utilizzo dello strumento: " + strumentoSelezionato.getTempoMaxUso());
                    } else {
                        Prenotazione prenotazioneDTO = new Prenotazione(selectedDate, selectedTime, selectedUtilizationTime, utenteLoggato, strumentoSelezionato);

                        if (myController.newUserBookingC(prenotazioneDTO)) {
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                            String message = "Prenotazione confermata:\n" +
                                    "Data: " + dateFormat.format(prenotazioneDTO.getData_prenotazioneS()) + "\n" +
                                    "Ora: " + timeFormat.format(prenotazioneDTO.getOra_prenotazioneS()) + "\n" +
                                    "Tempo di utilizzo: " + timeFormat.format(prenotazioneDTO.getTempo_utilizzoS()) + "\n" +
                                    "Utente: " + prenotazioneDTO.getUsername_fk().getUsername() + "\n" +
                                    "Strumento: " + prenotazioneDTO.getCodStrumento_fk().getCodStrumento() + "\n" +
                                    "Sede: " + strumentoSelezionato.getCodSede_fk().getNome();

                            JOptionPane.showMessageDialog(null, message, "Conferma Prenotazione", JOptionPane.INFORMATION_MESSAGE);

                            String riepilogoPrenotazione = "Data: " + dateFormat.format(prenotazioneDTO.getData_prenotazioneS()) + "\n" +
                                    "Ora: " + timeFormat.format(prenotazioneDTO.getOra_prenotazioneS()) + "\n" +
                                    "Tempo di utilizzo: " + timeFormat.format(prenotazioneDTO.getTempo_utilizzoS()) + "\n" +
                                    "Utente: " + prenotazioneDTO.getUsername_fk().getUsername() + "\n" +
                                    "Strumento: " + prenotazioneDTO.getCodStrumento_fk().getCodStrumento() + "\n" +
                                    "Sede: " + strumentoSelezionato.getCodSede_fk().getNome() + "\n" +
                                    "Indirizzo: " + strumentoSelezionato.getCodSede_fk().getIndirizzo();

                            EmailSender.sendVerificationCode(utenteLoggato.getEmail(), " ", "Riepilogo Prenotazione",
                                    "Prenotazione avvenuta con sucesso: \n" + riepilogoPrenotazione);

                            dispose();
                        }
                    }
                }
            }
        });

        setResizable(false);
    }

    private static class CustomTimeSpinnerModel extends SpinnerDateModel {
        public CustomTimeSpinnerModel(Date value) {
            super(value, null, null, Calendar.MINUTE);
        }

        @Override
        public void setCalendarField(int calendarField) {
            // Sovrascriviamo il metodo per assicurarci che venga sempre utilizzato Calendar.MINUTE
            super.setCalendarField(Calendar.MINUTE);
        }
    }



    ///////////////////////////////////////////BOOKING FRAME MODIFICA PRENOTAZIONE///////////////////////

    public  BookingFrame(JFrame parent, Controller controller , Utente utenteCorrente, Prenotazione prenotazione) {

        super(parent, "Modifica Prenotazione", true);

        myController = controller;
        utenteLoggato = utenteCorrente;
        prenotazioneDaModificare = prenotazione;

        Strumento strumentoRecDaPren;
        strumentoRecDaPren = myController.recuperoStrumentoC(prenotazione.getCodStrumento_fk().getCodStrumento());

        //Recuperiamo lo strumento in base al codice dello strumento salvato in esso

        setSize(600,200);

        setLocationRelativeTo(null);

        add(mainPanel);

        mainPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        //Testo della data
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(dataText, gbc);

        //Testo dell'ora
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(oraText, gbc);

        //Testo del tempo di utilizzo
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(tempoUtilizzoText, gbc);

        //Testo del codice prenotazione
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.weightx = 0.25;
        mainPanel.add(codPrenotazione, gbc);


        //Creazione calendario
        dateChooser = new JDateChooser();
        JCalendar calendar = dateChooser.getJCalendar();
        calendar.setMinSelectableDate(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(dateChooser, gbc);


        //Impostiamo di default l'ora del laboratorio
        Date initialTime = new Date();
        initialTime.setHours(8);
        initialTime.setMinutes(0);
        initialTime.setSeconds(0);

        TimeSpinnerModel timeSpinnerModel = new TimeSpinnerModel(initialTime);
        JSpinner timeSpinner = new JSpinner(timeSpinnerModel);

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) timeSpinner.getEditor();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        editor.getTextField().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(timeFormat)));

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(timeSpinner, gbc);


        //In questo modo quando vogliamo prenotare uno strumento ci appare direttamente il tempo massimo
        //di utilizzo inerente allo strumento selezionato
        Date initialUsageTime = new Date();
        if(strumentoRecDaPren != null) {
            initialUsageTime.setHours(strumentoRecDaPren.getTempoMaxUso().getHours());
            initialUsageTime.setMinutes(strumentoRecDaPren.getTempoMaxUso().getMinutes());
            initialUsageTime.setSeconds(0);
        } else
        {
            initialUsageTime.setHours(0);
            initialUsageTime.setMinutes(0);
            initialUsageTime.setSeconds(0);
        }

        CustomTimeSpinnerModel timeSpinnerModel2 = new CustomTimeSpinnerModel(initialUsageTime);
        JSpinner timeSpinnerTempoUtilizzo = new JSpinner(timeSpinnerModel2);

        JSpinner.DefaultEditor editor2 = (JSpinner.DefaultEditor) timeSpinnerTempoUtilizzo.getEditor();
        SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm");
        editor2.getTextField().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(timeFormat2)));

        gbc.gridx = 2;
        gbc.gridy = 1;
        mainPanel.add(timeSpinnerTempoUtilizzo, gbc);

        //Codice dello strumento
        codicePrenotazione = new JLabel(Integer.toString(prenotazioneDaModificare.getCod_prenotazione()));
        gbc.gridx = 3;
        gbc.gridy = 1;
        mainPanel.add(codicePrenotazione, gbc);

        //Bottone per confermare modifica
        gbc.gridx = 0;
        gbc.gridy = 2;

        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(modificaButton, gbc);




        modificaButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {


                selectedDate = dateChooser.getDate();
                selectedTime = (Date) timeSpinner.getValue();
                selectedUtilizationTime = (Date) timeSpinnerTempoUtilizzo.getValue();


                if(selectedDate != null && selectedTime != null && selectedUtilizationTime != null) {

                    Calendar selectedUtilizationCalendar = Calendar.getInstance();
                    Calendar maxUtilizationCalendar = Calendar.getInstance();

                    selectedUtilizationCalendar.setTime(selectedUtilizationTime);
                    maxUtilizationCalendar.setTime(strumentoRecDaPren.getTempoMaxUso());

                    /*Il codice seguente serve per far si che non ci siano errori durante il confronto
                     * quindi azzeriamo i millisecondi per i due calendari */

                    selectedUtilizationCalendar.set(Calendar.YEAR, 0);
                    selectedUtilizationCalendar.set(Calendar.MONTH, 0);
                    selectedUtilizationCalendar.set(Calendar.DAY_OF_MONTH, 0);
                    selectedUtilizationCalendar.set(Calendar.MILLISECOND, 0);

                    maxUtilizationCalendar.set(Calendar.YEAR, 0);
                    maxUtilizationCalendar.set(Calendar.MONTH, 0);
                    maxUtilizationCalendar.set(Calendar.DAY_OF_MONTH, 0);
                    maxUtilizationCalendar.set(Calendar.MILLISECOND, 0);

                    if(selectedUtilizationCalendar.getTime().getTime() > maxUtilizationCalendar.getTime().getTime()) {


                        JOptionPane.showMessageDialog(null, "ATTENZIONE! hai superato il tempo massimo di utilizzo dello strumento: "
                                + strumentoRecDaPren.getTempoMaxUso());

                    } else {

                    //Creo l'oggetto prenotazione e gli passo i dati inseriti dall'utente

                    Prenotazione prenotazioneDTO = new Prenotazione(prenotazione.getCod_prenotazione(), selectedDate, selectedTime, selectedUtilizationTime, utenteLoggato, strumentoRecDaPren);


                    //Se l'operazione è avvenuta con successo allora mostro il messaggio di modifica avvenuta con successo
                        // Prima controlliamo che la prenotazione non sia già presente con gli stessi dati
                    if(myController.modificaMiaPrenotazioneC(prenotazioneDTO)) {

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                        dateFormat.getCalendar().set(Calendar.MILLISECOND, 0);
                        timeFormat.getCalendar().set(Calendar.MILLISECOND,0);


                        String message = "Prenotazione Modificata Con Successo:\n" +
                                "Data: " + dateFormat.format(prenotazioneDTO.getData_prenotazioneS()) + "\n" +
                                "Ora: " + timeFormat.format(prenotazioneDTO.getOra_prenotazioneS()) + "\n" +
                                "Tempo di utilizzo: " + timeFormat.format(prenotazioneDTO.getTempo_utilizzoS()) + "\n" +
                                "Utente: " + prenotazioneDTO.getUsername_fk().getUsername() + "\n" +
                                "Strumento: " + prenotazioneDTO.getCodStrumento_fk().getCodStrumento() + "\n" +
                                "Sede: " + strumentoRecDaPren.getCodSede_fk().getNome();

                        JOptionPane.showMessageDialog(null, message, "Conferma Prenotazione", JOptionPane.INFORMATION_MESSAGE);

                        String riepilogoPrenotazione =  "Data: " + dateFormat.format(prenotazioneDTO.getData_prenotazioneS()) + "\n" +
                                "Ora: " + timeFormat.format(prenotazioneDTO.getOra_prenotazioneS()) + "\n" +
                                "Tempo di utilizzo: " + timeFormat.format(prenotazioneDTO.getTempo_utilizzoS()) + "\n" +
                                "Utente: " + prenotazioneDTO.getUsername_fk().getUsername() + "\n" +
                                "Strumento: " + prenotazioneDTO.getCodStrumento_fk().getCodStrumento() + "\n" +
                                "Sede: " + strumentoRecDaPren.getCodSede_fk().getNome() + "\n" +
                                "Indirizzo: " + strumentoRecDaPren.getCodSede_fk().getIndirizzo();



                        EmailSender.sendVerificationCode(utenteLoggato.getEmail(), " ", "Riepilogo Prenotazione Modificata",
                                "Prenotazione modificata con sucesso: \n" + riepilogoPrenotazione);

                        //Aggiorniamo la tabella delle mie prenotazioni



                        //Chiusura del JDialog
                        dispose();

                    }

                    }


                }

            }


        });



        setResizable(false);


    }



    public void setCodStrumento(String codStrumento) {

        codiceStrumento.setText(codStrumento);

    }

    public void setStrumentoAttuale(Strumento strumento) {

        strumentoSelezionato = strumento;

    }

    /*

    public void getStrumentHour(Strumento strumento) {

        Time tempoMaxUtilizzoStrum = myController.getStrumentHourC(strumento);

        minutiTempoUtilizzo = tempoMaxUtilizzoStrum.getMinutes();
        oraTempoUtilizzo = tempoMaxUtilizzoStrum.getHours();

    }

    */


    public void setPostazioneAttuale(Postazione postazione) {

        postazioneSelezionata = postazione;
    }


}



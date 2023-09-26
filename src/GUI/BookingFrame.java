package GUI;

import DAO.PrenotazioneDAO;
import DTO.Postazione;
import DTO.Prenotazione;
import DTO.Strumento;
import DTO.Utente;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/*Nonostante nel nome della classe ci sia la parola Frame quest'ultima è un JDialog dato che non vogliamo che aperta la finestra l'utente
* possa selezionare altri strumenti nella finestra principale, causando così errori con le diverse sincronizzazioni del TablePanel, JDialog
* ci facilita questa impedizione, necessità però di ricevere il Frame come parent*/

public class BookingFrame extends JDialog{

    private  static final JPanel mainPanel = new JPanel();
    private static final JLabel dataText = new JLabel("Data: ");
    private static final JLabel oraText = new JLabel("Ora: ");
    private static final JLabel tempoUtilizzoText = new JLabel("Tempo Utilizzo: ");
    private static final JLabel codStrumento = new JLabel("Codice Strumento: ");

    private static final BtnLayout prenotaButton = new BtnLayout("Prenota");

    private Date selectedDate;
    private Date selectedTime;
    private Date selectedUtilizationTime;

    private JLabel codiceStrumento;

    private JDateChooser dateChooser;

    private int codL;

    Utente utenteLoggato;

    Controller myController;
    Utente utenteCorrente;
    Strumento strumentoSelezionato;
    Postazione postazioneSelezionata;


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


        //Creazione calendario
        dateChooser = new JDateChooser();
        JCalendar calendar = dateChooser.getJCalendar();
        calendar.setMinSelectableDate(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");

        gbc.gridx = 0;
        gbc.gridy = 1;
        mainPanel.add(dateChooser, gbc);

        //Creazione campo per l'ora

        TimeSpinnerModel timeSpinnerModel = new TimeSpinnerModel();
        JSpinner timeSpinner = new JSpinner(timeSpinnerModel);

        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) timeSpinner.getEditor();
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        editor.getTextField().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(timeFormat)));

        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(timeSpinner, gbc);

        //Creazione campo per il tempo di utilizzo

        TimeSpinnerModel timeSpinnerModel2 = new TimeSpinnerModel();
        JSpinner timeSpinnerTempoUtilizzo = new JSpinner(timeSpinnerModel2);

        JSpinner.DefaultEditor editor2 = (JSpinner.DefaultEditor) timeSpinnerTempoUtilizzo.getEditor();
        SimpleDateFormat timeFormat2 = new SimpleDateFormat("HH:mm");
        editor2.getTextField().setFormatterFactory(new DefaultFormatterFactory(new DateFormatter(timeFormat2)));

        gbc.gridx = 2;
        gbc.gridy = 1;
        mainPanel.add(timeSpinnerTempoUtilizzo, gbc);

        //Codice dello strumento
        codiceStrumento = new JLabel("-1");
        gbc.gridx = 3;
        gbc.gridy = 1;
        mainPanel.add(codiceStrumento, gbc);

        //Bottone per prenotare
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


                if(selectedDate != null && selectedTime != null && selectedUtilizationTime != null) {



                    if(selectedUtilizationTime.getTime() > strumentoSelezionato.getTempoMaxUso().getTime()) {

                        JOptionPane.showMessageDialog(null, "ATTENZIONE! hai superato il tempo massimo di utilizzo dello strumento: "
                                + strumentoSelezionato.getTempoMaxUso());

                    }



                    //Creo l'oggetto prenotazione e gli passo i dati inseriti dall'utente

                    Prenotazione prenotazioneDTO = new Prenotazione(selectedDate, selectedTime, selectedUtilizationTime, utenteLoggato, strumentoSelezionato);

                    //Se l'operazione è avvenuta con successo allora mostro il messaggio di avvenimento prenotazione con successo
                    if(myController.newUserBookingC(prenotazioneDTO)) {

                        //JOptionPane.showMessageDialog(null, utenteLoggato.getUsername() + " prenotazione confermata!");

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

                        String message = "Prenotazione confermata:\n" +
                                "Data: " + dateFormat.format(prenotazioneDTO.getData_prenotazioneS()) + "\n" +
                                "Ora: " + timeFormat.format(prenotazioneDTO.getOra_prenotazioneS()) + "\n" +
                                "Tempo di utilizzo: " + timeFormat.format(prenotazioneDTO.getTempo_utilizzoS()) + "\n" +
                                "Utente: " + prenotazioneDTO.getUsername_fk().getUsername() + "\n" +
                                "Strumento: " + prenotazioneDTO.getCodStrumento_fk().getCodStrumento();

                        JOptionPane.showMessageDialog(null, message, "Conferma Prenotazione", JOptionPane.INFORMATION_MESSAGE);

                        String riepilogoPrenotazione =  "Data: " + dateFormat.format(prenotazioneDTO.getData_prenotazioneS()) + "\n" +
                                "Ora: " + timeFormat.format(prenotazioneDTO.getOra_prenotazioneS()) + "\n" +
                                "Tempo di utilizzo: " + timeFormat.format(prenotazioneDTO.getTempo_utilizzoS()) + "\n" +
                                "Utente: " + prenotazioneDTO.getUsername_fk().getUsername() + "\n" +
                                "Strumento: " + prenotazioneDTO.getCodStrumento_fk().getCodStrumento();

                        EmailSender.sendVerificationCode(utenteLoggato.getEmail(), " ", "Riepilogo Prenotazione",
                                "Prenotazione avvenuta con sucesso: \n" + riepilogoPrenotazione);

                        //Chiusura del JDialog

                        dispose();

                    }


                } else {

                    JOptionPane.showMessageDialog(null,"ATTENZIONE! I campi non possono essere vuoti");

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

    public void setPostazioneAttuale(Postazione postazione) {

        postazioneSelezionata = postazione;
    }


}



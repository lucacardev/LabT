package GUI;

import DTO.Prenotazione;
import DTO.Utente;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyBooking extends JPanel implements PrenotazioneSelectionListener {

    Controller myController;
    Utente utenteLoggato;
    BookingFrame myBooking;
    private static final JPanel topPanel = new JPanel();
    private static final JPanel footerPanel = new JPanel();
    private static final BtnLayout backButton = new BtnLayout("Indietro");
    private static final BtnLayout modifyButton = new BtnLayout("Modifica");
    private static final BtnLayout deleteButton = new BtnLayout("Elimina");
    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyBooking.this);
    private Prenotazione myPrenotazioneSelez;
    private int numeroPrenotazione;
    GridBagConstraints footerPanelGbc = new GridBagConstraints();

    public MyBooking(Controller controller, Utente utente)  {

        myController = controller;
        utenteLoggato = utente;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.80;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.CENTER;
        add(topPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.20;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        add(footerPanel, gbc);

        //////////////////////////////////////////TOP PANEL//////////////////////////////////////

        topPanel.setLayout(new BorderLayout());

        //Creo la tabella delle prenotazioni effettuate dall'utente che ha loggato
        TablePanel tablePanel = new TablePanel(myController.recuperoMiePrenotazioniC(utenteLoggato), this );
        topPanel.add(tablePanel, BorderLayout.CENTER);

        //////////////////////////////////////////FOOTER PANEL//////////////////////////////////////

        footerPanel.setLayout(new GridBagLayout());

        //Creazione bottone per tornare indietro

            footerPanelGbc.gridx = 0;
            footerPanelGbc.gridy = 0;
            footerPanelGbc.anchor = GridBagConstraints.LINE_START;
            footerPanelGbc.insets = new Insets(5, 15, 10, 0);
            footerPanelGbc.weightx = 0.33;
            footerPanel.add(backButton, footerPanelGbc);

            //Azione quando il pulsante indietro viene premuto

            backButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyBooking.this);

                    mainWindow.showCard("homePage");

                }
            });

        //Creazione bottone per modificare la prenotazione
        footerPanelGbc.gridx = 1;
        footerPanelGbc.gridy = 0;
        footerPanelGbc.anchor = GridBagConstraints.CENTER;
        footerPanelGbc.insets = new Insets(5, 0, 10, 0);
        footerPanelGbc.weightx = 0.33;
        modifyButton.setBackground(new Color(224, 186, 6));
        footerPanel.add(modifyButton, footerPanelGbc);

        //Creazione bottone per eliminare la prenotazione
        footerPanelGbc.gridx = 2;
        footerPanelGbc.gridy = 0;
        footerPanelGbc.anchor = GridBagConstraints.LINE_END;
        footerPanelGbc.insets = new Insets(5, 0, 10, 15);
        footerPanelGbc.weightx = 0.33;
        deleteButton.setBackground(Color.RED);
        footerPanel.add(deleteButton, footerPanelGbc);

        if(myPrenotazioneSelez == null) {
            modifyButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        modifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                myBooking = new BookingFrame(mainWindow, myController, utenteLoggato, myPrenotazioneSelez);
                myBooking.setVisible(true);

                //Aggiorniamo la tabella per far apparire le modifiche effettuate
                tablePanel.setDataMiePrenotazioni(myController.recuperoMiePrenotazioniC(utenteLoggato));

            }
        });

        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                int scelta = JOptionPane.showConfirmDialog(MyBooking.this, "Sei sicuro di voler eliminare questa prenotazione?",
                        "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

                //Chiediamo all'utente se è sicuro di eliminare la prenotazione
                if(scelta == JOptionPane.OK_OPTION) {

                    //Passiamo al metodo del Controller la prenotazione selezionata (ricevuta da TablePanel) che vogliamo eliminare
                    myController.eliminaPrenotazioneC(myPrenotazioneSelez);

                    //Chiamiamo il metodo di TablePanel per aggiornare la lista delle prenotazioni
                    tablePanel.setDataMiePrenotazioni(myController.recuperoMiePrenotazioniC(utenteLoggato));

                    topPanel.revalidate();
                    topPanel.repaint();

                    JOptionPane.showMessageDialog(MyBooking.this, "Prenotazione numero: " + numeroPrenotazione + " eliminata correttamente");

                }

            }
        });

    }

    /*Questo metodo ci permette di ottenere la prnenotazione selezionata dalla tabella che mostra le prenotazioni
    * dell'utente ed è un metodo dell'interfaccia implementata alla classe MyBooking (attuale).
    * In questo modo possiamo passare le informazioni inerenti alle righe selezionate.*/

    @Override
    public void prenotazioneSelected(Prenotazione prenotazione) {

        myPrenotazioneSelez = prenotazione;

        //Per evitare errori nella stampa della prneotazione eliminata
        //Salviamo il numero della prneotazione in una variabile temporanea di classe
        if(myPrenotazioneSelez != null) {

            numeroPrenotazione = myPrenotazioneSelez.getCod_prenotazione();

        }

        if (myPrenotazioneSelez != null) {

            // Se è stata selezionata, rendi visibili i bottoni
            modifyButton.setVisible(true);
            deleteButton.setVisible(true);

        } else {

            // Se non è stata selezionata, nascondi i bottoni
            modifyButton.setVisible(false);
            deleteButton.setVisible(false);

        }

    }

}

package GUI;

import DTO.Prenotazione;
import DTO.Utente;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyBooking extends JPanel implements BookingSelectionListener {

    Controller myController;
    Utente loggedUser;
    NewBookingToolSelected myBooking;
    private  final JPanel topPanel = new JPanel();
    private  final JButton modifyButton = new JButton("Modifica");
    private  final JButton deleteButton = new JButton("Elimina");
    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyBooking.this);
    private Prenotazione myBookingSelected;
    private int bookingNumber ;
    GridBagConstraints footerPanelGbc = new GridBagConstraints();

    public MyBooking(Controller controller, Utente utente)  {

        myController = controller;
        loggedUser = utente;

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
        JPanel footerPanel = new JPanel();
        add(footerPanel, gbc);

        //////////////////////////////////////////TOP PANEL//////////////////////////////////////

        topPanel.setLayout(new BorderLayout());

        //Creo la tabella delle prenotazioni effettuate dall'utente che ha loggato
        TablePanel tablePanel = new TablePanel(myController.myBookingRecoveryC(loggedUser), this );
        topPanel.add(tablePanel, BorderLayout.CENTER);

        //////////////////////////////////////////FOOTER PANEL//////////////////////////////////////

        footerPanel.setLayout(new GridBagLayout());

        //Creazione bottone per tornare indietro

            footerPanelGbc.gridx = 0;
            footerPanelGbc.gridy = 0;
            footerPanelGbc.anchor = GridBagConstraints.LINE_START;
            footerPanelGbc.insets = new Insets(5, 15, 10, 0);
            footerPanelGbc.weightx = 0.33;
        JButton backButton = new JButton("Indietro");
        backButton.setBackground(new Color(35,171,144));
        backButton.setForeground(Color.white);
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
        modifyButton.setForeground(Color.white);
        footerPanel.add(modifyButton, footerPanelGbc);

        //Creazione bottone per eliminare la prenotazione
        footerPanelGbc.gridx = 2;
        footerPanelGbc.gridy = 0;
        footerPanelGbc.anchor = GridBagConstraints.LINE_END;
        footerPanelGbc.insets = new Insets(5, 0, 10, 15);
        footerPanelGbc.weightx = 0.33;
        deleteButton.setBackground(Color.RED);
        deleteButton.setForeground(Color.WHITE);
        footerPanel.add(deleteButton, footerPanelGbc);

        if(myBookingSelected == null) {
            modifyButton.setVisible(false);
            deleteButton.setVisible(false);
        }

        modifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                myBooking = new NewBookingToolSelected(mainWindow, myController, loggedUser, myBookingSelected);
                myBooking.setVisible(true);

                //Aggiorniamo la tabella per far apparire le modifiche effettuate
                tablePanel.setDataMyBooking(myController.myBookingRecoveryC(loggedUser));

            }
        });

        deleteButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                super.mouseClicked(e);

                int choice = JOptionPane.showConfirmDialog(MyBooking.this, "Sei sicuro di voler eliminare questa prenotazione?",
                        "Conferma Eliminazione", JOptionPane.YES_NO_OPTION);

                //Chiediamo all'utente se è sicuro di eliminare la prenotazione
                if(choice == JOptionPane.OK_OPTION) {

                    //Passiamo al metodo del Controller la prenotazione selezionata (ricevuta da TablePanel) che vogliamo eliminare
                    myController.deleteBookingsC(myBookingSelected);

                    //Chiamiamo il metodo di TablePanel per aggiornare la lista delle prenotazioni
                    tablePanel.setDataMyBooking(myController.myBookingRecoveryC(loggedUser));

                    topPanel.revalidate();
                    topPanel.repaint();

                    JOptionPane.showMessageDialog(MyBooking.this, "Prenotazione numero: " + bookingNumber  + " eliminata correttamente");

                }

            }
        });

    }

    /*Questo metodo ci permette di ottenere la prnenotazione selezionata dalla tabella che mostra le prenotazioni
    * dell'utente ed è un metodo dell'interfaccia implementata alla classe MyBooking (attuale).
    * In questo modo possiamo passare le informations inerenti alle righe selezionate.*/


    @Override
    public void prenotazioneSelected(Prenotazione booking) {

        myBookingSelected = booking;

        //Per evitare errori nella stampa della prneotazione eliminata
        //Salviamo il numero della prneotazione in una variabile temporanea di classe
        if(myBookingSelected != null) {

            bookingNumber  = myBookingSelected.getCod_prenotazione();

        }

        if (myBookingSelected != null) {

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

package GUI;

import DTO.Utente;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MyBooking extends JPanel {

    Controller myController;
    Utente utenteLoggato;

    private static final JPanel topPanel = new JPanel();
    private static final JPanel footerPanel = new JPanel();

    private static BtnLayout backButton = new BtnLayout("Indietro");



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
        TablePanel tablePanel = new TablePanel(myController.recuperoMiePrenotazioniC(utenteLoggato));
        topPanel.add(tablePanel, BorderLayout.CENTER);

        //////////////////////////////////////////FOOTER PANEL//////////////////////////////////////

        footerPanel.setLayout(new GridBagLayout());
        footerPanelGbc.insets = new Insets(5,5,5,5);


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







    }



}

package GUI;

import DTO.Utente;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;

public class MyBooking extends JPanel {

    Controller myController;
    Utente utenteLoggato;

    private static final JPanel topPanel = new JPanel();
    private static final JPanel footerPanel = new JPanel();

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
        topPanel.setBackground(Color.red);
        add(topPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 0.20;
        gbc.weightx = 1;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        footerPanel.setBackground(Color.blue);
        add(footerPanel, gbc);

        //////////////////////////////////////////TOP PANEL//////////////////////////////////////

        topPanel.setLayout(new BorderLayout());
        TablePanel tablePanel = new TablePanel(null);
        topPanel.add(tablePanel, BorderLayout.CENTER);










    }
}

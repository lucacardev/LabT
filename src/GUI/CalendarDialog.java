package GUI;

import DTO.Strumento;
import UTILITIES.Controller;

import javax.swing.*;

public class CalendarDialog extends JDialog {
    Controller myController;

    public CalendarDialog(JFrame parent, Controller controller, Strumento strumento) {

        super(parent, "Calendario Prenotazioni", true);

        myController = controller;

        //Chiamimao il metodo del controller per recuperare la lista

        TablePanel tablePanel = new TablePanel(myController.toolsBookingsRecoveryC(strumento), " ");

        getContentPane().add(tablePanel);

        setSize(600,400);

        setLocationRelativeTo(null);

    }

}

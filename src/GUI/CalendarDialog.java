package GUI;

import DTO.Strumento;
import UTILITIES.Controller;

import javax.swing.*;

public class CalendarDialog extends JDialog {

    private TablePanel tablePanel;

    Controller myController;

    public CalendarDialog(JFrame parent, Controller controller, Strumento strumento) {

        super(parent, "Calendario Prenotazioni", true);

        myController = controller;

        //Qui devo chiamare il metododo del controller che mi recupera la lista

            tablePanel = new TablePanel(myController.recuperoPrenStrumenti(strumento), " ");


        getContentPane().add(tablePanel);

        setSize(600,400);

        setLocationRelativeTo(null);


    }



}

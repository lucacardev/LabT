package GUI;

import DTO.Strumento;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class SummaryWindow extends JDialog {

    private JComboBox<Integer> yearComboBox;
    private JComboBox<String> monthComboBox;

     Controller myController;

    public SummaryWindow(JFrame parent, Controller controller, Strumento strumento) {

        super(parent, "Riepilogo Strumento", true);

        myController = controller;

        setSize(350,190);

        setLocationRelativeTo(null);

        JButton okButton = new JButton("Visualizza Riepilogo");

        LocalDate currentDate = LocalDate.now();

        yearComboBox = new JComboBox<>();
        for (int year = 2022; year <= currentDate.getYear(); year++) {
            yearComboBox.addItem(year);
        }

        monthComboBox = new JComboBox<>(new String[]{" - " ,"Gennaio", "Febbraio", "Marzo", "Aprile", "Maggio",
                "Giugno", "Luglio", "Agosto", "Settembre", "Ottobre", "Novembre", "Dicembre"});

        setLayout(new GridBagLayout());
        GridBagConstraints summaryGBC = new GridBagConstraints();
        summaryGBC.insets = new Insets(0,10,10,10);



        //Testo mese
        summaryGBC.gridx = 0;
        summaryGBC.gridy = 0;
        summaryGBC.weightx = 0.33;
        summaryGBC.anchor = GridBagConstraints.LINE_START;
        add(new JLabel("Mese: "), summaryGBC);

        //Testo anno
        summaryGBC.gridx = 1;
        summaryGBC.gridy = 0;
        summaryGBC.anchor = GridBagConstraints.LINE_START;
        summaryGBC.weightx = 0.33;
        add(new JLabel("Anno: "), summaryGBC);

        //Testo codice strumento
        summaryGBC.gridx = 2;
        summaryGBC.gridy = 0;
        summaryGBC.anchor = GridBagConstraints.LINE_START;
        summaryGBC.weightx = 0.33;
        add(new JLabel("Strumento: "), summaryGBC);

        //Scelta del mese
        summaryGBC.gridx = 0;
        summaryGBC.gridy = 1;
        summaryGBC.anchor = GridBagConstraints.LINE_START;
        summaryGBC.weightx = 0.33;
        add(monthComboBox, summaryGBC);

        //Scelta dell'anno
        summaryGBC.gridx = 1;
        summaryGBC.gridy = 1;
        summaryGBC.anchor = GridBagConstraints.LINE_START;
        summaryGBC.weightx = 0.33;
        add(yearComboBox, summaryGBC);

        //Visualizza codStrumento
        summaryGBC.gridx = 2;
        summaryGBC.gridy = 1;
        summaryGBC.anchor = GridBagConstraints.CENTER;
        summaryGBC.weightx = 0.33;
        add(new JLabel(strumento.getCodStrumento().toString()), summaryGBC);

        summaryGBC.insets = new Insets(10,0,0,0);

        //Pulsante
        summaryGBC.gridx = 0;
        summaryGBC.gridy = 3;
        summaryGBC.anchor = GridBagConstraints.CENTER;
        summaryGBC.weightx = 1.0;
        summaryGBC.gridwidth = GridBagConstraints.REMAINDER;
        add(okButton, summaryGBC);

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ottieniamo l'anno e il mese selezionati
                int selectedYear = (int) yearComboBox.getSelectedItem();
                String selectedMonth = (String) monthComboBox.getSelectedItem();

                System.out.println(conversioneMese(selectedMonth));

                if (conversioneMese(selectedMonth) != 0) {
                    JOptionPane.showMessageDialog(null,
                            myController.riepilogoStrumentoC(strumento, conversioneMese(selectedMonth), selectedYear)
                            , "Riepilogo Strumento", JOptionPane.INFORMATION_MESSAGE);

                    // Chiudi il dialog
                    dispose();
                } else {

                    JOptionPane.showMessageDialog(null,
                            myController.riepilogoStrumentoNoMeseC(strumento, selectedYear),
                            "Riepilogo Strumento", JOptionPane.INFORMATION_MESSAGE);

                }
            }
        });

    }

    public int conversioneMese(String mese) {

        switch (mese) {
            case " - ":
                return 0;
            case "Gennaio":
                return 1;

            case "Febbraio":
                return 2;

            case "Marzo":
                return 3;

            case "Aprile":
                return 4;

            case "Maggio":
                return 5;

            case "Giugno":
                return 6;

            case "Luglio":
                return 7;

            case "Agosto":
                return 8;

            case "Settembre":
                return 9;

            case "Ottobre":
                return 10;

            case "Novembre":
                return 11;

            case "Dicembre":
                return 12;

            default:

                return -1;

        }


    }

}

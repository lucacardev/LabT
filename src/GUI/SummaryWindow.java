package GUI;

import DTO.Strumento;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;

public class SummaryWindow extends JDialog {

    private final JComboBox<Integer> yearComboBox;
    private final JComboBox<String> monthComboBox;
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
        add(new JLabel("month: "), summaryGBC);

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

        //choice del mese
        summaryGBC.gridx = 0;
        summaryGBC.gridy = 1;
        summaryGBC.anchor = GridBagConstraints.LINE_START;
        summaryGBC.weightx = 0.33;
        add(monthComboBox, summaryGBC);

        //choice dell'anno
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

                if (monthConverter(selectedMonth) != 0) {

                    JOptionPane.showMessageDialog(null,
                            myController.toolSummaryC(strumento, monthConverter(selectedMonth), selectedYear)
                            , "Riepilogo Strumento", JOptionPane.INFORMATION_MESSAGE);

                    // Chiudi il dialog
                    dispose();

                } else {

                    JOptionPane.showMessageDialog(null,
                            myController.noMonthToolSummaryC(strumento, selectedYear),
                            "Riepilogo Strumento", JOptionPane.INFORMATION_MESSAGE);

                }

            }

        });

    }

    public int monthConverter(String month) {

        return switch (month) {

            case " - " -> 0;
            case "Gennaio" -> 1;
            case "Febbraio" -> 2;
            case "Marzo" -> 3;
            case "Aprile" -> 4;
            case "Maggio" -> 5;
            case "Giugno" -> 6;
            case "Luglio" -> 7;
            case "Agosto" -> 8;
            case "Settembre" -> 9;
            case "Ottobre" -> 10;
            case "Novembre" -> 11;
            case "Dicembre" -> 12;
            default -> -1;

        };

    }

}

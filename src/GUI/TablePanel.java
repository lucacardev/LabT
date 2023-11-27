package GUI;

import DTO.Prenotazione;
import DTO.Strumento;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class TablePanel extends JPanel {

    private JTable tableStrumenti;
    private JTable tablePrenotazioni;

    private JTable tablePrenStrumenti;
    private TableModelStrumento tableModelStrumento;

    private TableModelMiePrenotazioni tableModelMiePrenotazioni;

    private TableModelPrenStrum tableModelPrenStrum;

    private Integer codStrumentoAttuale = null;

    //////////////////////////////////////TABLE PANEL PER STRUMENTI////////////////////////////////
    public TablePanel(List<Strumento> strumenti, NewBooking newBooking, BookingFrame bookingFrame) {

        //RAPPRESENTAZIONE TABELLA STRUMENTI DOPO LA RICERCA

        tableModelStrumento = new TableModelStrumento(strumenti);

        tableStrumenti = new JTable(tableModelStrumento);


        setLayout(new BorderLayout());
        add(new JScrollPane(tableStrumenti), BorderLayout.CENTER);

        tableStrumenti.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {
                    int selectedRow = tableStrumenti.getSelectedRow();
                    if (selectedRow != -1) {

                        Strumento selectedStrumento = tableModelStrumento.getStrumentoAtRow(selectedRow);

                            codStrumentoAttuale = selectedStrumento.getCodStrumento();

                            //Con questo metodo passiamo lo strumento selezionato all'oggetto CalendarDialog
                            newBooking.setStrumSelezCalend(selectedStrumento);

                            //Passiamo lo Strumento alla classe BookingFrame quando l'utente prenota uno strumento
                            bookingFrame.setStrumentoAttuale(selectedStrumento);



                            newBooking.setCodStrumentoBookingFrame(codStrumentoAttuale.toString());
                            System.out.println(codStrumentoAttuale.toString());

                            newBooking.bookingButtonAvailability();
                            newBooking.bookingButtonCalendar();



                    } else {
                        newBooking.removeBookingButton();
                        newBooking.removeCalendarButton();
                    }
                }
            }
        });

    }

    //////////////////////////////TABLE PANEL PER LE PRENOTAZIONI DELL'UTENTE////////////////

    public TablePanel(List<Prenotazione> listaPrenotazioni) {

        tableModelMiePrenotazioni = new TableModelMiePrenotazioni(listaPrenotazioni);

        tablePrenotazioni = new JTable(tableModelMiePrenotazioni);

        setLayout(new BorderLayout());
        add(new JScrollPane(tablePrenotazioni), BorderLayout.CENTER);


    }

    //Creo un nuovo costruttore per il calendario, gli aggiungo il parametro String per differenziarlo
    //da quello per le prenotazioni dell'utente.

    public TablePanel(List<Prenotazione> listaPrenotazioniStrumento, String calendario) {

       tableModelPrenStrum = new TableModelPrenStrum(listaPrenotazioniStrumento);

       tablePrenStrumenti = new JTable(tableModelPrenStrum);

        setLayout(new BorderLayout());
        add(new JScrollPane(tablePrenStrumenti), BorderLayout.CENTER);

    }


    public boolean isRowSelected() {

        return tableStrumenti.getSelectedRow() != -1;


    }



    public void setData(List<Strumento> listaStrumenti) {

        tableModelStrumento.setData(listaStrumenti);

    }




    public void aggiorna() {

        tableModelStrumento.fireTableDataChanged();

    }
}

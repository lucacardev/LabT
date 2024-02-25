package GUI;

import DTO.Prenotazione;
import DTO.Strumento;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.util.List;

public class TablePanel extends JPanel {
    private JTable toolsTable;
    private JTable bookingsTable;
    private ToolsTableModel toolsTableModel;
    private MyBookingsTableModel myBookingsTableModel;
    private Integer currentToolCode = null;

    private BookingSelectionListener bookingSelectionListener;

    //////////////////////////////////////TABLE PANEL PER STRUMENTI////////////////////////////////
    public TablePanel(List<Strumento> strumenti, NewBooking newBooking, NewBookingToolSelected newBookingToolSelected) {

        //RAPPRESENTAZIONE TABELLA STRUMENTI DOPO LA RICERCA

        toolsTableModel = new ToolsTableModel(strumenti);

        toolsTable = new JTable(toolsTableModel);

        setLayout(new BorderLayout());
        add(new JScrollPane(toolsTable), BorderLayout.CENTER);

        toolsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                //In questo modo impediamo all'utente di selezionare più righe alla volta evitando errori
                ListSelectionModel selectionModel = toolsTable.getSelectionModel();
                selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                if (!e.getValueIsAdjusting()) {
                    int selectedRow = toolsTable.getSelectedRow();
                    if (selectedRow != -1) {

                        Strumento selectedStrumento = toolsTableModel.getToolAtRow(selectedRow);

                        currentToolCode = selectedStrumento.getCodStrumento();

                        //Con questo metodo passiamo lo strumento selezionato all'oggetto CalendarDialog
                        newBooking.setToolSelectedInCalendar(selectedStrumento);

                        //Passiamo lo Strumento alla classe BookingFrame quando l'utente prenota uno strumento
                        if(newBookingToolSelected != null) {

                            newBookingToolSelected.setCurrentTool(selectedStrumento);

                        }

                        newBooking.setToolCodeNewBookingToolSelected(currentToolCode.toString());
                        newBooking.bookingButtonAvailability();
                        newBooking.bookingButtonCalendar();
                        newBooking.setSummaryButton();

                    } else {

                        newBooking.removeBookingButton();
                        newBooking.removeCalendarButton();
                        newBooking.removeSummaryButton();

                    }

                }

            }

        });

    }

    //////////////////////////////TABLE PANEL PER LE PRENOTAZIONI DELL'UTENTE////////////////

    public TablePanel(List<Prenotazione> bookingList, BookingSelectionListener listener) {

        this.bookingSelectionListener = listener;

        myBookingsTableModel = new MyBookingsTableModel(bookingList);

        bookingsTable = new JTable(myBookingsTableModel);

        bookingsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {

                //In questo modo impediamo all'utente di selezionare più righe alla volta evitando errori
                ListSelectionModel selectionModel = bookingsTable.getSelectionModel();
                selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

                //Prendiamo le informazioni della riga selezionata
                if (!e.getValueIsAdjusting()) {

                    int selectedRow = bookingsTable.getSelectedRow();
                    if (selectedRow != -1) {

                        //Prendiamo la prenotazione della riga tramite il metodo di tableModelMiePrenotazioni
                        Prenotazione myBookingSelected = myBookingsTableModel.getBookingAtRow(selectedRow);

                        //Chiamiamo il metodo dell'interfaccia e allo stesso tempo definiamo il valore della variabile
                        //presente in MyBooking così da inviare a quest'ultima le informazioni sulla prenotazione selezionata
                        bookingSelectionListener.prenotazioneSelected(myBookingSelected);

                    }

                    else {

                        //In questo modo quando non è selezionata nessuna riga non viene salvata l'ultima prneotazione e i pulsanti
                        //non sono disponibili e quindi non permettono di modificare o eliminare
                        bookingSelectionListener.prenotazioneSelected(null);

                    }

                }

            }

        });

        setLayout(new BorderLayout());
        add(new JScrollPane(bookingsTable), BorderLayout.CENTER);

    }

    //Creo un nuovo costruttore per il calendario, gli aggiungo il parametro String per differenziarlo
    //da quello per le prenotazioni dell'utente.

    //////////////////////////////TABLE PANEL PER IL CALENDARIO DEGLI STRUMENTI////////////////
    public TablePanel(List<Prenotazione> listaPrenotazioniStrumento, String calendario) {

        ToolsBookingsTableModel toolsBookingsTableModel = new ToolsBookingsTableModel(listaPrenotazioniStrumento);

        JTable tablePrenStrumenti = new JTable(toolsBookingsTableModel);

        setLayout(new BorderLayout());
        add(new JScrollPane(tablePrenStrumenti), BorderLayout.CENTER);

    }

    public void setData(List<Strumento> listaStrumenti) {

        toolsTableModel.setData(listaStrumenti);
        toolsTableModel.fireTableDataChanged();

    }

    public void setDataMyBooking(List<Prenotazione> listaPrenotazioni) {

        myBookingsTableModel.setData(listaPrenotazioni);
        myBookingsTableModel.fireTableDataChanged();

    }

}

package GUI;

import DTO.Prenotazione;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class MyBookingsTableModel extends AbstractTableModel {

    private final String[] columnsName = {"Codice Prenotazione", "Data", "Ora", "Tempo di utilizzo", "Utente", "Codice Strumento"};
    private List<Prenotazione> bookList;

    public MyBookingsTableModel(List<Prenotazione> bookList) {

        this.bookList = bookList;

    }

    @Override
    public String getColumnName(int column) {

        return columnsName[column];

    }

    @Override
    public int getRowCount() {

        if(bookList != null) {

            return bookList.size();

        } else {

            return 22;

        }

    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (bookList != null) {

            Prenotazione prenotazione = bookList.get(rowIndex);

            return switch (columnIndex) {

                case 0 -> prenotazione.getCod_prenotazione();
                case 1 -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    yield sdf.format(prenotazione.getData_prenotazioneS());

                }

                case 2 -> {

                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    yield sdfTime.format(prenotazione.getOra_prenotazioneS());

                }

                case 3 -> {

                    SimpleDateFormat sdfTime3 = new SimpleDateFormat("HH:mm");
                    yield sdfTime3.format(prenotazione.getTempo_utilizzoS());

                }

                case 4 -> prenotazione.getUsername_fk().getUsername();
                case 5 -> prenotazione.getCodStrumento_fk().getCodStrumento();

                default -> null;

            };

        } else {

            System.out.println("La liste delle prenotazioni è vuota");
            return null;

        }

    }

    public void setData(List<Prenotazione> bookList) {

        this.bookList = bookList;

    }

    public Prenotazione getPrenotazioneAtRow(int rowIndex) {

        if (bookList != null && rowIndex >= 0 && rowIndex < bookList.size()) {
            return bookList.get(rowIndex);

        }

        return null;

    }

}
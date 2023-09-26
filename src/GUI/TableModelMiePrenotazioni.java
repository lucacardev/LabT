package GUI;

import DTO.Prenotazione;
import DTO.Strumento;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModelMiePrenotazioni extends AbstractTableModel {

    private final String[] columnsName = {"Codice Prenotazione", "Data", "Tempo di utilizzo", "Username", "Codice Strumento"};
    private List<Prenotazione> listaPrenotazioni;

    public TableModelMiePrenotazioni(List<Prenotazione> listaPrenotazioni) {

        this.listaPrenotazioni = listaPrenotazioni;


    }

    @Override
    public String getColumnName(int column) {

        return columnsName[column];

    }

    @Override
    public int getRowCount() {
        return 10;
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (listaPrenotazioni != null) {

            Prenotazione prenotazione = listaPrenotazioni.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return prenotazione.getCod_prenotazione();

                case 1:
                    return prenotazione.getData_prenotazioneS();

                case 2:
                    return prenotazione.getTempo_utilizzoS();

                case 3:
                    return prenotazione.getUsername_fk();

                case 4:
                    return prenotazione.getCodStrumento_fk();

                default:
                    return null;
            }

        } else {
            return null;
        }
    }

    public void setData(List<Prenotazione> listaPrenotazioni) {

        this.listaPrenotazioni = listaPrenotazioni;

        fireTableDataChanged(); // Notifica alla tabella che i dati sono stati cambiati
    }

    public Prenotazione getPrenotazioneAtRow(int rowIndex) {
        if (listaPrenotazioni != null && rowIndex >= 0 && rowIndex < listaPrenotazioni.size()) {
            return listaPrenotazioni.get(rowIndex);
        }
        return null;
    }

    }

package GUI;

import DTO.Prenotazione;
import DTO.Strumento;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static javax.swing.text.StyleConstants.setForeground;

public class TableModelMiePrenotazioni extends AbstractTableModel {

    private final String[] columnsName = {"Codice Prenotazione", "Data", "Ora", "Tempo di utilizzo", "Utente", "Codice Strumento"};
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

        if(listaPrenotazioni != null) {

            return listaPrenotazioni.size();

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

        if (listaPrenotazioni != null) {

            Prenotazione prenotazione = listaPrenotazioni.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    return prenotazione.getCod_prenotazione();

                case 1:
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return sdf.format(prenotazione.getData_prenotazioneS());

                case 2:
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    return sdfTime.format(prenotazione.getOra_prenotazioneS());

                case 3:
                    SimpleDateFormat sdfTime3 = new SimpleDateFormat("HH:mm");
                    return sdfTime3.format(prenotazione.getTempo_utilizzoS());

                case 4:
                    return prenotazione.getUsername_fk().getUsername();

                case 5:

                    return prenotazione.getCodStrumento_fk().getCodStrumento();

                default:
                    return null;
            }

        } else {
            System.out.println("La liste delle prenotazioni è vuota");
            return null;
        }
    }

    public void setData(List<Prenotazione> listaPrenotazioni) {

        this.listaPrenotazioni = listaPrenotazioni;

    }

    public Prenotazione getPrenotazioneAtRow(int rowIndex) {
        if (listaPrenotazioni != null && rowIndex >= 0 && rowIndex < listaPrenotazioni.size()) {
            return listaPrenotazioni.get(rowIndex);
        }
        return null;
    }

    }

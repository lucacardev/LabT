package GUI;

import DTO.Prenotazione;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class TableModelPrenStrum extends AbstractTableModel {

    private final String[] columnsName = {"Data", "Ora", "Tempo di utilizzo", "Codice Strumento"};
    private List<Prenotazione> listaPrenStrumenti;

    public TableModelPrenStrum(List <Prenotazione> listaPrenStrumenti)  {

        this.listaPrenStrumenti = listaPrenStrumenti;

    }


    @Override
    public int getRowCount() {

        if (listaPrenStrumenti != null) {

            return listaPrenStrumenti.size();

        } else {

            return 22;
        }
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public String getColumnName(int column) {

        return columnsName[column];

    }

    public Object getValueAt(int rowIndex, int columnIndex) {

        if (listaPrenStrumenti != null) {

            Prenotazione prenotazione = listaPrenStrumenti.get(rowIndex);

            switch (columnIndex) {
                case 0:
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    return sdf.format(prenotazione.getData_prenotazioneS());

                case 1:
                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    return sdfTime.format(prenotazione.getOra_prenotazioneS());

                case 2:
                    SimpleDateFormat sdfTime3 = new SimpleDateFormat("HH:mm");
                    return sdfTime3.format(prenotazione.getTempo_utilizzoS());

                case 3:
                    return prenotazione.getCodStrumento_fk().getCodStrumento();

                default:
                    return null;
            }

        } else {
            System.out.println("La lista delle prenotazioni dello strumento è vuota");
            return null;
        }
    }
}

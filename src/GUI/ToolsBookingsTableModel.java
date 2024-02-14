package GUI;

import DTO.Prenotazione;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;
import java.util.List;

public class ToolsBookingsTableModel extends AbstractTableModel {

    private final String[] columnsName = {"Data", "Ora", "Tempo di utilizzo", "Codice Strumento"};
    private final List<Prenotazione> toolListBook;

    public ToolsBookingsTableModel(List <Prenotazione> toolListBook)  {

        this.toolListBook = toolListBook;

    }

    @Override
    public int getRowCount() {

        if (toolListBook != null) {

            return toolListBook.size();

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

        if (toolListBook != null) {

            Prenotazione prenotazione = toolListBook.get(rowIndex);

            return switch (columnIndex) {

                case 0 -> {

                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    yield sdf.format(prenotazione.getData_prenotazioneS());

                }
                case 1 -> {

                    SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm");
                    yield sdfTime.format(prenotazione.getOra_prenotazioneS());

                }
                case 2 -> {

                    SimpleDateFormat sdfTime3 = new SimpleDateFormat("HH:mm");
                    yield sdfTime3.format(prenotazione.getTempo_utilizzoS());

                }

                case 3 -> prenotazione.getCodStrumento_fk().getCodStrumento();
                default -> null;

            };

        } else {

            System.out.println("La lista delle prenotazioni dello strumento è vuota");
            return null;

        }

    }

}

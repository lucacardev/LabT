package GUI;

import DTO.Tecnico;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModelTeam extends AbstractTableModel {

    private String columnsName = "Tecnici da sostituire: ";
    private List<Tecnico> listaTecniciTeam;

    public TableModelTeam(List<Tecnico> tecniciTeam) {

        listaTecniciTeam = tecniciTeam;

    }

    @Override
    public String getColumnName(int column) {

        return columnsName;

    }

    @Override
    public int getRowCount() {
        if(listaTecniciTeam != null) {

            return listaTecniciTeam.size();

        } else {

            return 10;

        }
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {

        if (listaTecniciTeam != null) {

            Tecnico tecnico = listaTecniciTeam.get(rowIndex);

            if (columnIndex == 0) {
                return tecnico.getNome() + " " + tecnico.getCognome() + " " + tecnico.getMatricola();
            }

        } else {
            System.out.println("La lista dei tecnici del team è vuota");
        }

        return null;
    }

    public Tecnico getTecnicoAtRow(int rowIndex) {
        if (listaTecniciTeam != null && rowIndex >= 0 && rowIndex < listaTecniciTeam.size()) {
            return listaTecniciTeam.get(rowIndex);
        }
        return null;
    }

    void modificaNomeColonne(String nomeColonna) {

        columnsName = nomeColonna;
    }



}

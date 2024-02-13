package GUI;

import DTO.Tecnico;

import javax.swing.table.AbstractTableModel;
import java.util.List;

public class TableModelTeam extends AbstractTableModel {

    private String columnsName = "Tecnici da sostituire: ";
    private final List<Tecnico> teamTecList;

    public TableModelTeam(List<Tecnico> techniciansTeams) {

        teamTecList = techniciansTeams;

    }

    @Override
    public String getColumnName(int column) {

        return columnsName;

    }

    @Override
    public int getRowCount() {

        if(teamTecList != null) {

            return teamTecList.size();

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

        if (teamTecList != null) {

            Tecnico tecnico = teamTecList.get(rowIndex);

            if (columnIndex == 0) {

                return tecnico.getNome() + " " + tecnico.getCognome() + " " + tecnico.getMatricola();

            }

        } else {

            System.out.println("La lista dei tecnici del team è vuota");

        }

        return null;

    }

    public Tecnico getTechnicianAtRow(int rowIndex) {

        if (teamTecList != null && rowIndex >= 0 && rowIndex < teamTecList.size()) {

            return teamTecList.get(rowIndex);

        }

        return null;

    }

    void editColumnsName() {

        columnsName = "Possibili sostituti: ";

    }

}

package GUI;

import DAO.TecnicoDAO;
import DTO.Responsabile;
import DTO.Team;
import DTO.Tecnico;
import UTILITIES.Controller;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.List;

public class ModOrganigramma extends JDialog {

    private final JTable toBeReplacedTab;
    private final JTable substituteTab;
    private Tecnico tecToBeReplaced;
    private Tecnico tecReplaced;
    private final Team currentTeam;
    private final Responsabile currentManager;
    Controller myController;

    private int[] selectedRows = new int[0];

    public ModOrganigramma(MainWindow mainWindow, Controller controller, Responsabile manager, List<Tecnico> tecnici, Team team) {

        super(mainWindow, "Modifica Organigramma", true);

        myController = controller;
        currentTeam = team;
        currentManager = manager;

        setSize(500, 350);

        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        ////////////////////////////////PANNELLO SINISTRO/////////////////////////////
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Creazione della tabella sinistra
        TableModelTeam toBeReplacedTableModel = new TableModelTeam(tecnici);
        toBeReplacedTab = new JTable(toBeReplacedTableModel) {

            //Permette di selezionare più righe senza l'uso di CTRL
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                ListSelectionModel selectionModel = getSelectionModel();
                boolean selected = selectionModel.isSelectedIndex(rowIndex);

                System.out.println("Righe selezionate: " + selectedRows.length);

                //Se la riga è già selezionata allora rimuoviamo la selezione
                if (selected) {

                    selectionModel.removeSelectionInterval(rowIndex, rowIndex);
                    getValueAt(rowIndex, columnIndex);

                    substituteTab.setVisible(selectedRows.length < 2);

                } else {

                        if (selectedRows.length < 2) {

                            //Se ci sono due selezioni nella prima tabella la seconda scompare
                            substituteTab.setVisible(selectedRows.length != 1);

                            //Togliamo le eventuali selezioni fatte in precedenza per evitare bug
                            substituteTab.clearSelection();

                            selectionModel.addSelectionInterval(rowIndex, rowIndex);

                        } else {

                            JOptionPane.showMessageDialog(null, "Se intendi cambiare la gerarchia di due tecnici" +
                                    " dello stesso team, puoi selezionare solo i due tecnici interessati", "Errore selezione", JOptionPane.ERROR_MESSAGE);

                        }

                }

                toBeReplacedTab.setRowSelectionAllowed(true);

            }

        };


        // Aggiungi la tabella a uno JScrollPane
        JScrollPane scrollPane = new JScrollPane(toBeReplacedTab);

        // Inserimento JScrollPane nel pannello sinistro
        leftPanel.add(scrollPane, BorderLayout.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.5;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.BOTH;
        add(leftPanel, gbc);

        //////////////////////////////////////////PANNELLO DESTRO//////////////////////////////
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());

        // Creazione della tabella di destra
        TecnicoDAO tecnicoDAO = new TecnicoDAO(controller);

        // Recuperiamo i tecnici senza un team
        List<Tecnico> tecFullList = tecnicoDAO.recuperoTecniciNoTeam();

        TableModelTeam substituteTableModel = new TableModelTeam(tecFullList);

        substituteTableModel.editColumnsName();
        substituteTab = new JTable(substituteTableModel);
        substituteTab.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Aggiungi la tabella a uno JScrollPane
        JScrollPane scrollPane2 = new JScrollPane(substituteTab);

        // Inserimento JScrollPane nel pannello destro
        rightPanel.add(scrollPane2, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(rightPanel, gbc);

        ///////////////////////////////PANNELLO SOTTO//////////////////////////////
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());

        // Inserisci il pulsante "Conferma Sostituzione" nella parte inferiore
        JButton substitutionConfirm = new JButton("Conferma Sostituzione");
        substitutionConfirm.setBackground(new Color(23, 65, 95));
        substitutionConfirm.setForeground(Color.WHITE);
        footerPanel.add(substitutionConfirm, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(footerPanel, gbc);
        
        toBeReplacedTab.getSelectionModel().addListSelectionListener(new ListSelectionListener() {


            @Override
            public void valueChanged(ListSelectionEvent e) {

                selectedRows = toBeReplacedTab.getSelectedRows();

                    if (e.getValueIsAdjusting()) {

                        System.out.println(Arrays.toString(selectedRows));

                    }
            }

        });



        //Azione quando il bottone di conferma sostituzione viene premuto
        substitutionConfirm.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int selectedRows2 = substituteTab.getSelectedRow();

                if (selectedRows.length > 0 && selectedRows2 != -1 ) {

                    tecToBeReplaced =  toBeReplacedTableModel.getTechnicianAtRow(selectedRows[0]);
                    tecReplaced = substituteTableModel.getTechnicianAtRow(selectedRows2);


                    Object[] options = { "Sostituisci", "Annulla" };

                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Vuoi confermare l'operazione? \n"
                            + tecToBeReplaced.getNome() + " " + tecToBeReplaced.getCognome() + " " + tecToBeReplaced.getMatricola()
                                    + " --> " + tecReplaced.getNome() + " " + tecReplaced.getCognome() + " "  + tecReplaced.getMatricola(),
                            "Conferma sostituzione",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if(choice == JOptionPane.YES_OPTION) {

                        //Caso in cui il tecnico da sostituire è il leader del team
                        if(tecToBeReplaced.getMatricola().equals(currentTeam.getMatricolaL())) {

                            myController.technicianUpdate1To1C(tecToBeReplaced, tecReplaced, currentTeam);

                            myController.updateTeamLeaderC(currentTeam, tecReplaced);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, currentManager);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }

                        } else {

                            myController.technicianUpdate1To1C(tecToBeReplaced, tecReplaced, currentTeam);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, currentManager);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }
                        }

                        dispose();

                    }

                } else if(selectedRows.length == 2) {

                    tecToBeReplaced = toBeReplacedTableModel.getTechnicianAtRow(selectedRows[0]);
                    tecReplaced = toBeReplacedTableModel.getTechnicianAtRow(selectedRows[1]);

                    Object[] options = { "Sostituisci", "Annulla" };

                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Vuoi confermare l'operazione? \n"
                                    + tecToBeReplaced.getNome() + " " + tecToBeReplaced.getCognome() + " " + tecToBeReplaced.getMatricola()
                                    + " --> " + tecReplaced.getNome() + " " + tecReplaced.getCognome() + " "  + tecReplaced.getMatricola(),
                            "Conferma sostituzione",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if(choice == JOptionPane.YES_OPTION) {

                        //Caso in cui il tecnico da sostituire è il leader del team
                        if(selectedRows[0] == 0) {

                            myController.technicianUpdateSameTeamC(tecToBeReplaced, tecReplaced, currentTeam);

                            myController.updateTeamLeaderC(currentTeam, tecReplaced);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, currentManager);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }

                        } else {

                            myController.technicianUpdateSameTeamC(tecToBeReplaced, tecReplaced, currentTeam);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, currentManager);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }
                        }

                        dispose();

                    }

                } else {

                    JOptionPane.showMessageDialog(null, "Devi prima selezionare i tecnici.",
                            "Attenzione", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

    }


}

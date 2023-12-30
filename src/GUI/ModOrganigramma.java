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

    private final JTable tabDaSostituire;
    private final JTable tabDaSostituto;
    private Tecnico tecnicoDaSostituire;
    private Tecnico tecnicoSostituto;
    private final Team teamAttuale;
    private final Responsabile responsabileCorrente;
    Controller myController;

    private int[] righeSelez = new int[0];

    public ModOrganigramma(MainWindow mainWindow, Controller controller, Responsabile responsabile, List<Tecnico> tecnici, Team team) {

        super(mainWindow, "Modifica Organigramma", true);

        myController = controller;
        teamAttuale = team;
        responsabileCorrente = responsabile;

        setSize(500, 350);

        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        ////////////////////////////////PANNELLO SINISTRO/////////////////////////////
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        // Creazione della tabella sinistra
        TableModelTeam tableModelDaSostituire = new TableModelTeam(tecnici);
        tabDaSostituire = new JTable(tableModelDaSostituire) {

            //Permette di selezionare più righe senza l'uso di CTRL
            @Override
            public void changeSelection(int rowIndex, int columnIndex, boolean toggle, boolean extend) {
                ListSelectionModel selectionModel = getSelectionModel();
                boolean selected = selectionModel.isSelectedIndex(rowIndex);

                System.out.println("Righe selezionate: " + righeSelez.length);

                //Se la riga è già selezionata allora rimuoviamo la selezione
                if (selected) {

                    selectionModel.removeSelectionInterval(rowIndex, rowIndex);
                    getValueAt(rowIndex, columnIndex);

                    tabDaSostituto.setVisible(righeSelez.length < 2);

                } else {

                        if (righeSelez.length < 2) {

                            //Se ci sono due selezioni nella prima tabella la seconda scompare
                            tabDaSostituto.setVisible(righeSelez.length != 1);

                            //Togliamo le eventuali selezioni fatte in precedenza per evitare bug
                            tabDaSostituto.clearSelection();

                            selectionModel.addSelectionInterval(rowIndex, rowIndex);

                        } else {

                            JOptionPane.showMessageDialog(null, "Se intendi cambiare la gerarchia di due tecnici" +
                                    " dello stesso team, puoi selezionare solo i due tecnici interessati", "Errore selezione", JOptionPane.ERROR_MESSAGE);

                        }

                }

                tabDaSostituire.setRowSelectionAllowed(true);

            }

        };


        // Aggiungi la tabella a uno JScrollPane
        JScrollPane scrollPane = new JScrollPane(tabDaSostituire);

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
        List<Tecnico> listaCompletaTecnici = tecnicoDAO.recuperoTecniciNoTeam();

        TableModelTeam tableModelSostituto = new TableModelTeam(listaCompletaTecnici);

        tableModelSostituto.modificaNomeColonne();
        tabDaSostituto = new JTable(tableModelSostituto);
        tabDaSostituto.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Aggiungi la tabella a uno JScrollPane
        JScrollPane scrollPane2 = new JScrollPane(tabDaSostituto);

        // Inserimento JScrollPane nel pannello destro
        rightPanel.add(scrollPane2, BorderLayout.CENTER);

        gbc.gridx = 1;
        gbc.gridy = 0;
        add(rightPanel, gbc);

        ///////////////////////////////PANNELLO SOTTO//////////////////////////////
        JPanel footerPanel = new JPanel();
        footerPanel.setLayout(new BorderLayout());

        // Inserisci il pulsante "Conferma Sostituzione" nella parte inferiore
        BtnLayout confermaSostituzione = new BtnLayout("Conferma Sostituzione");
        footerPanel.add(confermaSostituzione, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(footerPanel, gbc);
        
        tabDaSostituire.getSelectionModel().addListSelectionListener(new ListSelectionListener() {


            @Override
            public void valueChanged(ListSelectionEvent e) {

                righeSelez = tabDaSostituire.getSelectedRows();

                    if (e.getValueIsAdjusting()) {

                        System.out.println(Arrays.toString(righeSelez));

                    }
            }

        });



        //Azione quando il bottone di conferma sostituzione viene premuto
        confermaSostituzione.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int selectedRows2 = tabDaSostituto.getSelectedRow();

                if (righeSelez.length > 0 && selectedRows2 != -1 ) {

                    tecnicoDaSostituire =  tableModelDaSostituire.getTecnicoAtRow(righeSelez[0]);
                    tecnicoSostituto = tableModelSostituto.getTecnicoAtRow(selectedRows2);


                    Object[] options = { "Sostituisci", "Annulla" };

                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Vuoi confermare l'operazione? \n"
                            + tecnicoDaSostituire.getNome() + " " + tecnicoDaSostituire.getCognome() + " " + tecnicoDaSostituire.getMatricola()
                                    + " --> " + tecnicoSostituto.getNome() + " " + tecnicoSostituto.getCognome() + " "  + tecnicoSostituto.getMatricola(),
                            "Conferma sostituzione",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if(choice == JOptionPane.YES_OPTION) {

                        //Caso in cui il tecnico da sostituire è il leader del team
                        if(tecnicoDaSostituire.getMatricola().equals(teamAttuale.getMatricolaL())) {

                            myController.updateTecnico1to1C(tecnicoDaSostituire, tecnicoSostituto, teamAttuale);

                            myController.updateTeamLeaderC(teamAttuale, tecnicoSostituto);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, responsabileCorrente);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }

                        } else {

                            myController.updateTecnico1to1C(tecnicoDaSostituire, tecnicoSostituto, teamAttuale);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, responsabileCorrente);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }
                        }

                        dispose();

                    }

                } else if(righeSelez.length == 2) {

                    tecnicoDaSostituire = tableModelDaSostituire.getTecnicoAtRow(righeSelez[0]);
                    tecnicoSostituto = tableModelDaSostituire.getTecnicoAtRow(righeSelez[1]);

                    Object[] options = { "Sostituisci", "Annulla" };

                    int choice = JOptionPane.showOptionDialog(
                            null,
                            "Vuoi confermare l'operazione? \n"
                                    + tecnicoDaSostituire.getNome() + " " + tecnicoDaSostituire.getCognome() + " " + tecnicoDaSostituire.getMatricola()
                                    + " --> " + tecnicoSostituto.getNome() + " " + tecnicoSostituto.getCognome() + " "  + tecnicoSostituto.getMatricola(),
                            "Conferma sostituzione",
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.PLAIN_MESSAGE,
                            null,
                            options,
                            options[0]);

                    if(choice == JOptionPane.YES_OPTION) {

                        //Caso in cui il tecnico da sostituire è il leader del team
                        if(righeSelez[0] == 0) {

                            myController.updateTecnicoSameTeamC(tecnicoDaSostituire, tecnicoSostituto, teamAttuale);

                            myController.updateTeamLeaderC(teamAttuale, tecnicoSostituto);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, responsabileCorrente);
                                mainWindow.addCardPanel(myteampage, "myTeamPage");

                            }

                        } else {

                            myController.updateTecnicoSameTeamC(tecnicoDaSostituire, tecnicoSostituto, teamAttuale);

                            if (mainWindow.containsCard("myTeamPage")) {

                                mainWindow.showCard("myTeamPage");

                            } else {

                                MyTeam myteampage = new MyTeam(myController, responsabileCorrente);
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

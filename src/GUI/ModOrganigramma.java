package GUI;

import DAO.TeamDAO;
import DAO.TecnicoDAO;
import DTO.Responsabile;
import DTO.Team;
import DTO.Tecnico;
import UTILITIES.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class ModOrganigramma extends JDialog {

    private final JPanel rightPanel = new JPanel();
    private final JPanel leftPanel = new JPanel();
    private final JPanel footerPanel = new JPanel();
    private JTable tabDaSostituire;
    private JTable tabDaSostituto;

    private Tecnico tecnicoDaSostituire;
    private Tecnico tecnicoSostituto;

    private Team teamAttuale;

    private Responsabile responsabileCorrente;
    private BtnLayout confermaSostituzione = new BtnLayout("Conferma Sostituzione");

    private List<Tecnico> listaTecnici;

    private MainWindow mainWindowR;
    Controller myController;

    public ModOrganigramma(MainWindow mainWindow, Controller controller, Responsabile responsabile, List<Tecnico> tecnici, Team team) {

        super(mainWindow, "Modifica Organigramma", true);

        myController = controller;
        listaTecnici = tecnici;
        teamAttuale = team;
        responsabileCorrente = responsabile;
        mainWindowR = mainWindow;

        setSize(500, 350);

        setLocationRelativeTo(null);
        setResizable(false);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        ////////////////////////////////PANNELLO SINISTRO/////////////////////////////
        leftPanel.setLayout(new BorderLayout());

        // Creazione della tabella sinistra
        TableModelTeam tableModelDaSostituire = new TableModelTeam(listaTecnici);
        tabDaSostituire = new JTable(tableModelDaSostituire);
        tabDaSostituire.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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
        rightPanel.setLayout(new BorderLayout());

        // Creazione della tabella di destra
        TecnicoDAO tecnicoDAO = new TecnicoDAO(controller);

        // Recuperiamo i tecnici senza un team
        List<Tecnico> listaCompletaTecnici = tecnicoDAO.recuperoTecniciNoTeam();

        TableModelTeam tableModelSostituto = new TableModelTeam(listaCompletaTecnici);

        tableModelSostituto.modificaNomeColonne("Possibili sostituti: ");
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
        footerPanel.setLayout(new BorderLayout());

        // Inserisci il pulsante "Conferma Sostituzione" nella parte inferiore
        footerPanel.add(confermaSostituzione, BorderLayout.SOUTH);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(footerPanel, gbc);

        //Azione quando il bottone di conferma sostituzione viene premuto
        confermaSostituzione.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int selectedRow = tabDaSostituire.getSelectedRow();
                int selectedRow2 = tabDaSostituto.getSelectedRow();
                TecnicoDAO tecnicoDAO = new TecnicoDAO(myController);

                tecnicoDaSostituire =  tableModelDaSostituire.getTecnicoAtRow(selectedRow);
                tecnicoSostituto = tableModelSostituto.getTecnicoAtRow(selectedRow2);


                if (selectedRow != -1 && selectedRow2 != -1) {


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

                } else {

                    JOptionPane.showMessageDialog(null, "Devi prima selezionare i tecnici.",
                            "Attenzione", JOptionPane.ERROR_MESSAGE);
                }

            }
        });

    }


}

package GUI;

import DAO.ResponsabileDAO;
import DTO.Responsabile;
import DTO.Team;
import DTO.Tecnico;
import UTILITIES.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MyTeam extends JPanel {

    Controller myController;
    private final Responsabile currentManager;
    private final JTable teamTable;
    private final String[] columnNames = {"teamCode", "Nome", "Descrizione", "Leader", "NumeroTecnici"};

    public MyTeam(Controller controller, Responsabile loggedInManager) {

        myController = controller;
        currentManager = loggedInManager;
        setLayout(new BorderLayout());

        // Creazione della tabella con i dati
        Object[][] data = {};
        teamTable = new JTable(data, columnNames);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Consente la selezione di una sola riga alla volta

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Pannello per i pulsanti

        JButton backButton = new JButton("Indietro");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        JButton addTeamButton = new JButton("Aggiungi Team");
        addTeamButton.setBackground(new Color(23,65,95));
        addTeamButton.setForeground(Color.WHITE);


        JButton viewOrgButton = new JButton("Visualizza Organigramma");
        viewOrgButton.setBackground(new Color(23,65,95));
        viewOrgButton.setForeground(Color.WHITE);

        JButton deleteTeamButton = new JButton("Elimina Team");
        deleteTeamButton.setBackground(Color.RED);
        deleteTeamButton.setForeground(Color.WHITE);


        buttonPanel.add(backButton);
        buttonPanel.add(Box.createHorizontalStrut(50)); // Aggiungi spazio tra i pulsanti
        buttonPanel.add(addTeamButton); // Aggiungi il pulsante "Aggiungi Team"
        buttonPanel.add(Box.createHorizontalStrut(50)); // Aggiungi altro spazio tra i pulsanti
        buttonPanel.add(viewOrgButton);
        buttonPanel.add(Box.createHorizontalStrut(50)); // Aggiungi altro spazio tra i pulsanti
        buttonPanel.add(deleteTeamButton);
        add(buttonPanel, BorderLayout.SOUTH); // Aggiunge il pannello dei pulsanti nella parte inferiore del frame

        addTeamButton.addActionListener(e -> {

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);

            if(mainWindow.containsCard("newteam")) {

                mainWindow.showCard("newteam");
            }

            else {

                NewTeam newTeam = new NewTeam(myController,currentManager);
                mainWindow.addCardPanel(newTeam, "newteam");

            }

        });

        // Aggiungi ActionListener per il pulsante "Visualizza Organigramma"
        viewOrgButton.addActionListener(e -> {

            int selectedRow = teamTable.getSelectedRow(); // Ottieni l'indice della riga selezionata

            if (selectedRow != -1) { // Verifica se una riga è stata selezionata
                String teamCode = (String) teamTable.getValueAt(selectedRow, 0); // Ottieni il nome del team dalla riga selezionata
                String teamName = (String) teamTable.getValueAt(selectedRow, 1);
                String teamDes = (String) teamTable.getValueAt(selectedRow, 2);
                String teamMat = (String) teamTable.getValueAt(selectedRow, 3);
                int nTec = (Integer) teamTable.getValueAt(selectedRow, 4);

                Team team = new Team(teamCode, teamName, teamDes, teamMat, nTec, currentManager);
                List<Tecnico> teamTechnicians = myController.technicianRecoveryC(team);

                if(teamTechnicians.size() == 5) {
                    OrganigramFiveTechnicians organigram5 = new OrganigramFiveTechnicians(myController,currentManager, teamTechnicians, team);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);
                    mainWindow.addCardPanel(organigram5, "Organigramma");

                } if(teamTechnicians.size() == 10) {

                    OrganigramTenTechnicians organigramTenTechnicians = new OrganigramTenTechnicians(myController,currentManager,teamTechnicians,team);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);
                    mainWindow.addCardPanel(organigramTenTechnicians, "Organigramma");

                }

            } else {

                JOptionPane.showMessageDialog(MyTeam.this, "Seleziona un team prima di visualizzare l'organigramma.", "Nessun team selezionato", JOptionPane.WARNING_MESSAGE);

            }

        });

        viewManagerTeams();

        //Azione per tornare indietro
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                ManagerHomePage managerHomePage = new ManagerHomePage(myController, currentManager);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);
                    mainWindow.addCardPanel(managerHomePage, "HomePageR");

            }
        });

        deleteTeamButton.addActionListener(e -> {

            int selectedRow = teamTable.getSelectedRow(); // Ottieni l'indice della riga selezionata

            if (selectedRow != -1) { // Verifica se una riga è stata selezionata
                int choice = JOptionPane.showConfirmDialog(MyTeam.this, "Sei sicuro di voler eliminare questo team?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {

                    // Chiamata al metodo del controller per eliminare il team dal database
                    Team selectedTeam = createTeamFromRow(selectedRow);
                    myController.deleteTeamC(selectedTeam); // Chiama il metodo deleteTeam del controller

                    // Rimuovi la riga dalla tabella
                    ((DefaultTableModel) teamTable.getModel()).removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Team eliminato con successo!");

                }

            } else {

                JOptionPane.showMessageDialog(MyTeam.this, "Seleziona un team prima di eliminare.", "Nessun team selezionato", JOptionPane.WARNING_MESSAGE);

            }

        });

    }

    private void viewManagerTeams() {

        if (myController != null && currentManager != null) {

            try {

                List<Team> teams = myController.dBTeamsRecovery(currentManager);

                    if (!teams.isEmpty()) {
                        Object[][] data = new Object[teams.size()][5]; // Array bidimensionale per i dati dei team

                        for (int i = 0; i < teams.size(); i++) {

                            Team team = teams.get(i);
                            data[i][0] = team.getCodTeam();
                            data[i][1] = team.getNome();
                            data[i][2] = team.getDescrizione();
                            data[i][3] = team.getMatricolaL();
                            data[i][4] = team.getN_tecnici();

                        }

                        DefaultTableModel model = new DefaultTableModel(data, columnNames) {

                            @Override
                            public boolean isCellEditable(int row, int column) {

                                return false; // Impedisce la modifica delle celle

                            }
                        };

                        this.teamTable.setModel(model);

                        JScrollPane scrollPane = new JScrollPane(this.teamTable); // Usa la JTable globale
                        add(scrollPane, BorderLayout.CENTER);

                    } else {

                        JLabel noTeamsLabel = new JLabel("Nessun team associato al responsabile");
                        add(noTeamsLabel, BorderLayout.CENTER);
                        noTeamsLabel.setForeground(Color.RED);
                        JPanel centerPanel = new JPanel(new GridBagLayout());
                        GridBagConstraints gbc = new GridBagConstraints();
                        gbc.gridx = 0;
                        gbc.gridy = 0;
                        gbc.weightx = 1.0;
                        gbc.weighty = 1.0;
                        gbc.anchor = GridBagConstraints.CENTER;

                        centerPanel.add(noTeamsLabel, gbc);

                        add(centerPanel, BorderLayout.CENTER);

                    }

            } catch (Exception e) {

                JLabel errorLabel = new JLabel("Recupero dei team non riuscito: " + e.getMessage());
                add(errorLabel, BorderLayout.CENTER);

            }

        }

    }

    private Team createTeamFromRow(int row) {

        DefaultTableModel model = (DefaultTableModel) teamTable.getModel();
        String teamCode = (String) model.getValueAt(row, 0);
        String nome = (String) model.getValueAt(row, 1);
        String description = (String) model.getValueAt(row, 2);
        String leaderMatriculation = (String) model.getValueAt(row, 3);
        Integer techniciansNumber = (Integer) model.getValueAt(row, 4);

        // Creazione dell'oggetto Team basato sui valori della riga selezionata
        return new Team(teamCode, nome, description, leaderMatriculation, techniciansNumber, null);

    }

}
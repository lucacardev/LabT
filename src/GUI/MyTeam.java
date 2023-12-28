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
import java.nio.channels.ScatteringByteChannel;
import java.util.Arrays;
import java.util.List;

public class MyTeam extends JPanel {

    Controller myController;
    Responsabile responsabileCorrente;
    //JTextArea teamTextArea;

    private JTable teamTable;
    private JButton backButton;
    private BtnLayout addTeamButton;
    private BtnLayout viewOrgButton;

    private BtnLayout deleteTeamButton;


    private String[] columnNames = {"CodiceTeam", "Nome", "Descrizione", "Leader", "NumeroTecnici"};
    Object[][] data = {};

    public MyTeam(Controller controller, Responsabile responsabileLoggato) {
        myController = controller;
        responsabileCorrente = responsabileLoggato;
        setLayout(new BorderLayout());

        // Creazione della tabella con i dati
        teamTable = new JTable(data, columnNames);
        teamTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Consente la selezione di una sola riga alla volta

       /* JScrollPane scrollPane = new JScrollPane(teamTable);
        // Aggiungi la JScrollPane al pannello principale
        add(scrollPane);*/

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Pannello per i pulsanti

        backButton = new JButton("Indietro");
        addTeamButton = new BtnLayout("Aggiungi Team");
        viewOrgButton = new BtnLayout("Visualizza Organigramma");
        deleteTeamButton = new BtnLayout("Elimina Team");

        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        deleteTeamButton.setBackground(Color.RED);
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

                NewTeam newTeam = new NewTeam(myController,responsabileCorrente);
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


                Team team = new Team(teamCode, teamName, teamDes, teamMat, nTec, responsabileCorrente);
                List<Tecnico> tecniciDelTeam = myController.recuperoTecniciC(team);

                if(tecniciDelTeam.size() == 5) {
                    Organigramma5Panel organigramma5 = new Organigramma5Panel(myController,responsabileCorrente,tecniciDelTeam, team);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);
                    mainWindow.addCardPanel(organigramma5, "Organigramma");

                } if(tecniciDelTeam.size() == 10) {

                    Organigramma10Panel organigramma10 = new Organigramma10Panel(myController,responsabileCorrente,tecniciDelTeam,team);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);
                    mainWindow.addCardPanel(organigramma10, "Organigramma");

                }

            } else {
                JOptionPane.showMessageDialog(MyTeam.this, "Seleziona un team prima di visualizzare l'organigramma.", "Nessun team selezionato", JOptionPane.WARNING_MESSAGE);
            }
        });

        visualizzaTeamsResponsabile();

        //Azione per tornare indietro
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                    HomePageR homepage = new HomePageR(myController, responsabileCorrente);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(MyTeam.this);
                    mainWindow.addCardPanel(homepage, "HomePageR");
            }
        });

        deleteTeamButton.addActionListener(e -> {
            int selectedRow = teamTable.getSelectedRow(); // Ottieni l'indice della riga selezionata

            if (selectedRow != -1) { // Verifica se una riga è stata selezionata
                int choice = JOptionPane.showConfirmDialog(MyTeam.this, "Sei sicuro di voler eliminare questo team?", "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    String teamCode = (String) teamTable.getValueAt(selectedRow, 0); // Ottieni il codice del team dalla riga selezionata

                    // Chiamata al metodo del controller per eliminare il team dal database
                    Team Teamselezionato = createTeamFromRow(selectedRow);
                    myController.deleteTeamC(Teamselezionato); // Chiama il metodo deleteTeam del controller

                    // Rimuovi la riga dalla tabella
                    ((DefaultTableModel) teamTable.getModel()).removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null, "Team eliminato con successo!");
                }
            } else {
                JOptionPane.showMessageDialog(MyTeam.this, "Seleziona un team prima di eliminare.", "Nessun team selezionato", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    /*private void visualizzaTeamsResponsabile() {
        if (myController != null && responsabileCorrente != null) {
            try {
                List<Team> teams = myController.recuperoTeamsDalDBC(responsabileCorrente);
                String[] informazioni = ResponsabileDAO.nomecognomeRecovery(responsabileCorrente.getMatricola(), responsabileCorrente.getPw());

                if (informazioni[0] != null && informazioni[1] != null) {
                    String nome = informazioni[0];
                    String cognome = informazioni[1];

                    if (!teams.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(nome).append(" ").append(cognome).append(" , matricola ").append(responsabileCorrente.getMatricola()).append(" , i tuoi teams sono:\n");

                        for (Team team : teams) {
                            sb.append("Nome: ").append(team.getNome()).append(", Descrizione: ").append(team.getDescrizione()).append("\n");
                            // Aggiungi altri dettagli del team se necessario
                        }

                        teamTextArea.setText(sb.toString());
                    } else {
                        teamTextArea.setText("Nessun team associato al responsabile");
                    }
                }
            } catch (Exception e) {
                teamTextArea.setText("Recupero dei team non riuscito: " + e.getMessage());
            }
        }*/
    //}
    private void visualizzaTeamsResponsabile() {
        if (myController != null && responsabileCorrente != null) {
            try {
                List<Team> teams = myController.recuperoTeamsDalDBC(responsabileCorrente);
                String[] informazioni = ResponsabileDAO.nomecognomeRecovery(responsabileCorrente.getMatricola(), responsabileCorrente.getPw());

                if (informazioni[0] != null && informazioni[1] != null) {
                    String nome = informazioni[0];
                    String cognome = informazioni[1];

                    if (!teams.isEmpty()) {
                        StringBuilder sb = new StringBuilder();
                        sb.append(nome).append(" ").append(cognome).append(" , matricola ").append(responsabileCorrente.getMatricola()).append(" , i tuoi teams sono:\n");

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
                    }
                }
            } catch (Exception e) {
                JLabel errorLabel = new JLabel("Recupero dei team non riuscito: " + e.getMessage());
                add(errorLabel, BorderLayout.CENTER);
            }
        }
    }

    private Team createTeamFromRow(int row) {
        DefaultTableModel model = (DefaultTableModel) teamTable.getModel();
        String codiceTeam = (String) model.getValueAt(row, 0);
        String nome = (String) model.getValueAt(row, 1);
        String descrizione = (String) model.getValueAt(row, 2);
        String matricolaLeader = (String) model.getValueAt(row, 3);
        Integer numeroTecnici = (Integer) model.getValueAt(row, 4);

        // Creazione dell'oggetto Team basato sui valori della riga selezionata
        return new Team(codiceTeam, nome, descrizione, matricolaLeader, numeroTecnici, null);
    }

}
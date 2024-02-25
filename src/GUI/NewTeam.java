package GUI;

import DAO.TecnicoDAO;
import DTO.*;
import UTILITIES.Controller;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewTeam extends JPanel {

    private final static JLabel codTeamText = new JLabel("Codice team");
    private final static JLabel textName = new JLabel("Nome");
    private final static JLabel descriptionText = new JLabel("Descrizione");
    private final static JLabel matriculationNumberText = new JLabel("Matricola Leader");
    private final static JLabel tecNumberText = new JLabel("Numero tecnici");
    private final static JLabel managerText = new JLabel("Responsabile del team");
    private final TextFieldBorderColor codTeamField;
    private final TextFieldBorderColor nameField;
    private final TextFieldBorderColor descriptionField;
    private final JComboBox matriculationNumberField;
    private final JComboBox<Integer> techniciansNumberComboBox;
    private Integer choice;
    private static BufferedImage newBackgroundImage;
    private static BufferedImage rightBackground;
    private final List<Tecnico> tecSelected = new ArrayList<>();
    Controller controller;
    private final Responsabile currentManager;

    public NewTeam(Controller myController, Responsabile loggedInManager) {

        controller = myController;
        currentManager = loggedInManager;
        setLayout(new GridLayout(0,2));

        JPanel rightPage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo con interpolazione bilineare
                if (rightBackground != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(rightBackground, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            rightBackground = ImageIO.read(new File("src/GUI/icon/sfondoR.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine nuovi team responsabile");
            ex.printStackTrace();

        }
        rightPage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Testo responsabile
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(managerText, gbc);

        //Testo codtean
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(codTeamText, gbc);
        codTeamText.setVisible(true);

        //Testo nome
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(textName, gbc);

        //Testo descrizione
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(descriptionText, gbc);

        //Testo matricola leader
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(matriculationNumberText, gbc);

        //Testo ntecnici
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(tecNumberText, gbc);

        //Campo responsabile
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        JLabel respField = new JLabel();
        respField.setText(currentManager.getMatricola());
        respField.setEnabled(false);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(respField, gbc);

        //Campo codteam
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        codTeamField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(codTeamField);
        codTeamField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                codTeamField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                codTeamField.setBorder(new LineBorder(Color.BLACK));
            }
        });
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(codTeamField, gbc);

        //Campo nome
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        nameField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(nameField);
        nameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                nameField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                nameField.setBorder(new LineBorder(Color.BLACK));
            }
        });
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(nameField, gbc);

        //Campo descrizione
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        descriptionField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(descriptionField);
        descriptionField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                descriptionField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                descriptionField.setBorder(new LineBorder(Color.BLACK));
            }
        });
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(descriptionField, gbc);

        //Campo matricola leader
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        matriculationNumberField = new JComboBox<>();
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement(" - ");
        matriculationNumberField.setModel(comboBoxModel);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(matriculationNumberField, gbc);

        //Campo ntecnici
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        Integer[] choices = {0,5,10};
        techniciansNumberComboBox = new JComboBox<>(choices);
        techniciansNumberComboBox.setBackground(Color.white);
        techniciansNumberComboBox.setBorder(new LineBorder(new Color(246, 183, 55),1));
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(techniciansNumberComboBox, gbc);

        techniciansNumberComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    choice = (Integer) event.getItem();

                    //In questo modo ogni volta che si seleziona il numero la lista si azzera
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                    model.addElement(" - ");
                    matriculationNumberField.setModel(model);

                    if (choice == 5 || choice == 10) {
                        selectedTec(choice);

                    }
                }
            }
        });

        //Bottone indietro
        gbc.gridy = 13;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(15,0,0,15);

        JButton backButton = new JButton("Indietro");
        backButton.setForeground(Color.WHITE);
        backButton.setBackground(new Color(23,65,95));

        rightPage.add(backButton, gbc);

        //Indirizzamento alla pagina di login

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myTeam = new MyTeam(myController,currentManager);
                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewTeam.this);
                mainWindow.addCardPanel(myTeam, "myteam");
            }
        });


        //Bottone inserisci
        gbc.gridy = 13;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(15,15,0,0);

        JButton insertButton = new JButton("Inserisci");
        insertButton.setBackground(new Color(23,65,95));
        insertButton.setForeground(Color.WHITE);
        rightPage.add(insertButton, gbc);

        //Azioni dopo che il bottone registrati viene premuto
        insertButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(getCodTeamNew().isEmpty() || getNomeNew().isEmpty() || matriculationNumberField.getSelectedItem().toString().isEmpty()) {

                    JOptionPane.showMessageDialog(null,"I campi non possono essere vuoti.");

                } else if (getCodTeamNew().length() != 4 || !getCodTeamNew().startsWith("T") || !getCodTeamNew().substring(1).matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "teamCode errato. Ti ricordiamo che il codice di un team inizia con 'T' seguito da 3 numeri.", "Errore Codice Team", JOptionPane.ERROR_MESSAGE);

                }
                else {

                    if (controller.teamRecoveryWithCodeC(getCodTeamNew())) {

                        JOptionPane.showMessageDialog(null, "Il codice del team è già presente nel database.");

                    } else {

                        //Salviamo solo la matricola
                        String matrAndName = matriculationNumberField.getSelectedItem().toString().substring(0, 7);

                        //Chiamo la classe DTO che incapsula le informations del nuovo team
                        Team newTeam = new Team(getCodTeamNew(), getNomeNew(), getNewDescription(), matrAndName, choice, currentManager);

                        boolean complete = myController.newTeamInsert(newTeam);

                        if (complete) {

                            teamCreationAndTechnicalAssignment();

                            JOptionPane.showMessageDialog(null, "L'inserimento di un nuovo team è avvenuto con successo!");

                            MyTeam myteam = new MyTeam(myController, currentManager);
                            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewTeam.this);
                            mainWindow.addCardPanel(myteam, "mieiteam");

                        }
                    }
                }

            }
        });

        JPanel leftPage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo
                if (newBackgroundImage != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(newBackgroundImage, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di sinistra

        try {

            newBackgroundImage = ImageIO.read(new File("src/GUI/icon/teamwork.jpg"));

        } catch (Exception ex) {

            System.out.println("Errore caricamento immagine background NewTeam");
            ex.printStackTrace();

        }

        add(leftPage);
        add(rightPage);

    }

    private String getCodTeamNew() {
        return codTeamField.getText().trim();
    }

    private String getNomeNew() {
        return nameField.getText().trim();
    }

    private String getNewDescription() {
        return descriptionField.getText().trim();
    }

    private void selectedTec(int numberTecToBeSelected) {

        // Crei un'istanza di TecnicoDAO
        TecnicoDAO tecnicoDAO = new TecnicoDAO(controller);

        // lista completa di tecnici che non fanno parte di un team
        List<Tecnico> tecFullList = tecnicoDAO.recuperoTecniciNoTeam();

        //finestra di dialogo per la selezione dei tecnici
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Seleziona Tecnici");
        dialog.setLayout(new BorderLayout());

        DefaultListModel<Tecnico> listModel = new DefaultListModel<>();

        JList<Tecnico> tecList = new JList<>(listModel);

        //Ci permette di selezionare tecnici multipli senza l'uso di CTRL

        tecList.setSelectionModel(new DefaultListSelectionModel() {

            int count = 0;
            @Override
            public void setSelectionInterval(int index0, int index1) {

                //Impediamo di selezionare più tecnici del previsto
                if(!super.isSelectedIndex(index0)) {

                    if(count < numberTecToBeSelected) {

                        count++;
                        super.addSelectionInterval(index0, index1);

                    } else {

                        JOptionPane.showMessageDialog(null, "Non puoi selezionare più di " +
                                numberTecToBeSelected + " tecnici", "Numero tecnici superato", JOptionPane.ERROR_MESSAGE);

                    }

                }

                else {

                    count--;
                    super.removeSelectionInterval(index0, index1);

                }

            }

        });

        //Aggiungiamo alla JList i tecnici recuperati dal DB che non hanno un Team
        for (Tecnico tecnico : tecFullList) {

            listModel.addElement(tecnico);

        }

        JScrollPane scrollPane = new JScrollPane(tecList);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Seleziona");
        dialog.add(selectButton, BorderLayout.SOUTH);

        //Reset JComboBox techniciansNumberComboBox quando il JDialog viene chiuso
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                techniciansNumberComboBox.setSelectedIndex(0);

            }
        });

        //Azione tasto quando i tecnici vengono selezionati per comporre il nuovo team
        selectButton.addActionListener(e -> {

            List<Tecnico> selectedTecnici = tecList.getSelectedValuesList();
            if (selectedTecnici.size() == numberTecToBeSelected) {
                tecSelected.addAll(selectedTecnici);

                //Ricavate le matricole le aggiungiamo al JComboBox per poi scegliere il leader
                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

                for (Tecnico tecnico : tecSelected) {
                    comboBoxModel.addElement(tecnico.getMatricola().trim() + " " + tecnico.getNome().trim() + " " + tecnico.getCognome().trim());
                }

                matriculationNumberField.setModel(comboBoxModel);

                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Seleziona esattamente " + numberTecToBeSelected + " tecnici.", "Selezione errata", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setSize(300, 400);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> tecList.requestFocusInWindow());
        dialog.setVisible(true);

        try {

            BufferedImage iconImage = ImageIO.read(new File("src/GUI/icon/icons8-laboratorio-64.png"));
            dialog.setIconImage(iconImage);

        } catch (IOException e) {

            e.printStackTrace();
        };

        SwingUtilities.invokeLater(() -> tecList.requestFocusInWindow());

    }

    private void teamCreationAndTechnicalAssignment() {

        String teamCode = getCodTeamNew(); // Codice del team appena creato
        Team teamCreated = controller.teamRecoveryC(teamCode);

        // Assegna il codice del team ai tecnici memorizzati temporaneamente
        for (Tecnico tecnico : tecSelected) {
            tecnico.setTeam(teamCreated);
            boolean updated = controller.technicianUpdateC(tecnico, teamCode);

            if (!updated) {
                System.out.println("Aggiornamento non avvenuto");
            }

        }

    }

}
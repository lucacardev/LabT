package GUI;


import DAO.TecnicoDAO;
import DTO.*;
import UTILITIES.Controller;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NewTeam extends JPanel {

    private final static JLabel codTeamText = new JLabel("Codice team");
    private final static JLabel nomeText = new JLabel("Nome");
    private final static JLabel descrizioneText = new JLabel("Descrizione");
    private final static JLabel matricolaLText = new JLabel("Matricola Leader");
    private final static JLabel ntecniciText = new JLabel("Numero tecnici");
    private final static JLabel respText = new JLabel("Responsabile del team");


    private final  BtnLayout backButton = new BtnLayout("Indietro");
    private final  BtnLayout insertButton = new BtnLayout("Inserisci");
    private final  BtnLayout tecniciButton = new BtnLayout("Seleziona membri");

    private TextFieldBorderColor codTeamField;
    private TextFieldBorderColor nomeField;
    private TextFieldBorderColor descrizioneField;
    private JComboBox matricolaLField;
    private JComboBox<Integer> ntecniciComboBox;

    Integer scelta;
    private JLabel respField = new JLabel();

    private static BufferedImage backgroundImageNew;

    private List<Tecnico> tecniciSelezionati = new ArrayList<>();

    Controller controller;
    Responsabile responsabileCorrente;

    public NewTeam(Controller myController, Responsabile responsabileLoggato) {
        controller = myController;
        responsabileCorrente = responsabileLoggato;
        setLayout(new GridLayout(0,2));


        JPanel rightPage = new JPanel();
        rightPage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        // Testo responsabile
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(respText, gbc);

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
        rightPage.add(nomeText, gbc);

        //Testo descrizione
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(descrizioneText, gbc);

        //Testo matricola leader
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(matricolaLText, gbc);

        //Testo ntecnici
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(ntecniciText, gbc);

        //Campo responsabile
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        respField.setText(responsabileCorrente.getMatricola());
        respField.setEnabled(false);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(respField, gbc);

        //Campo codteam
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        codTeamField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(codTeamField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(codTeamField, gbc);

        //Campo nome
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        nomeField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(nomeField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(nomeField, gbc);

        //Campo descrizione
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        descrizioneField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(descrizioneField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(descrizioneField, gbc);

        //Campo matricola leader
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        matricolaLField = new JComboBox<>();
        DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        comboBoxModel.addElement(" - ");
        matricolaLField.setModel(comboBoxModel);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(matricolaLField, gbc);

        //Campo ntecnici
        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        Integer[] scelte = {0,5,10};
        ntecniciComboBox = new JComboBox<>(scelte);
        ntecniciComboBox.setBackground(Color.white);
        ntecniciComboBox.setBorder(new LineBorder(new Color(35,171,144),1));
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(ntecniciComboBox, gbc);

        ntecniciComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                   scelta = (Integer) event.getItem();

                    //In questo modo ogni volta che si seleziona il numero la lista si azzera
                    DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>();
                    model.addElement(" - ");
                    matricolaLField.setModel(model);

                    if (scelta == 5 || scelta == 10) {
                        selectTecnici(scelta);

                    }
                }
            }
        });







        //Bottone indietro
        gbc.gridy = 12;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(15,0,0,15);
        rightPage.add(backButton, gbc);

        //Indirizzamento alla pagina di login

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myTeam = new MyTeam(myController,responsabileCorrente);
                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewTeam.this);
                mainWindow.addCardPanel(myTeam, "myteam");
            }
        });


        //Bottone inserisci
        gbc.gridy = 12;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(15,15,0,0);
        rightPage.add(insertButton, gbc);

        //Azioni dopo che il bottone registrati viene premuto
        insertButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(getCodTeamNew().isEmpty() || getNomeNew().isEmpty() || matricolaLField.getSelectedItem().toString().isEmpty()) {

                    JOptionPane.showMessageDialog(null,"I campi non possono essere vuoti.");

                } else if (getCodTeamNew().length() != 4 || !getCodTeamNew().startsWith("T") || !getCodTeamNew().substring(1).matches("\\d+")) {
                    JOptionPane.showMessageDialog(null, "CodiceTeam errato. Ti ricordiamo che il codice di un team inizia con 'T' seguito da 3 numeri.", "Errore Codice Team", JOptionPane.ERROR_MESSAGE);

                }
                else {

                    System.out.println(getCodTeamNew());
                    if (controller.recuperoTeamdaCodC(getCodTeamNew())) {

                        JOptionPane.showMessageDialog(null, "Il codice del team è già presente nel database.");

                    } else {

                        //Chiamo la classe DTO che incapsula le informazioni del nuovo team
                        Team nuovoTeam = new Team(getCodTeamNew(), getNomeNew(), getDescrizioneNew(), matricolaLField.getSelectedItem().toString(), scelta, responsabileCorrente);

                        boolean complete = myController.newTeamInsert(nuovoTeam);

                        if (complete) {

                            creazioneTeameAssegnamentoTecnici();

                            JOptionPane.showMessageDialog(null, "L'inserimento di un nuovo team è avvenuto con successo!");

                            MyTeam myteam = new MyTeam(myController, responsabileCorrente);
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

                // Disegna l'immagine di sfondo
                if (backgroundImageNew != null) {
                    g.drawImage(backgroundImageNew, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        //Impostazione sfondo background di sinistra

        try {
            backgroundImageNew = ImageIO.read(new File("src/GUI/icon/teamwork.jpg"));


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
        return nomeField.getText().trim();
    }

    private String getDescrizioneNew() {
        return descrizioneField.getText().trim();
    }

    /*
    private String getMatricolaLNew() {
        return matricolaLField.getText().trim();
    }
    */

    private List<Tecnico> selectTecnici(int numTecniciDaSelezionare) {
        // Crei un'istanza di TecnicoDAO
        TecnicoDAO tecnicoDAO = new TecnicoDAO(controller);

        // lista completa di tecnici che non fanno parte di un team
        List<Tecnico> listaCompletaTecnici = tecnicoDAO.recuperoTecniciNoTeam();

        //finestra di dialogo per la selezione dei tecnici
        JDialog dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle("Seleziona Tecnici");
        dialog.setLayout(new BorderLayout());


        DefaultListModel<Tecnico> listModel = new DefaultListModel<>();
        JList<Tecnico> tecniciList = new JList<>(listModel);

        //Ci permette di selezionare tecnici multipli senza l'uso di CTRL
        tecniciList.setSelectionModel(new DefaultListSelectionModel() {

            int count = 0;
            @Override
            public void setSelectionInterval(int index0, int index1) {

                //Impediamo di selezionare più tecnici del previsto
                if(!super.isSelectedIndex(index0)) {
                    if(count < numTecniciDaSelezionare) {
                        count++;
                        super.addSelectionInterval(index0, index1);
                    } else {
                        JOptionPane.showMessageDialog(null, "Non puoi selezionare più di " +
                                numTecniciDaSelezionare + " tecnici", "Numero tecnici superato", JOptionPane.ERROR_MESSAGE);

                    }

                }
                else {
                    count--;
                    super.removeSelectionInterval(index0, index1);

                }
            }
        });

        //Aggiungiamo alla JList i tecnici recuperati dal DB che non hanno un Team
        for (Tecnico tecnico : listaCompletaTecnici) {
            listModel.addElement(tecnico);
        }


        JScrollPane scrollPane = new JScrollPane(tecniciList);
        dialog.add(scrollPane, BorderLayout.CENTER);

        JButton selectButton = new JButton("Seleziona");
        dialog.add(selectButton, BorderLayout.SOUTH);

        selectButton.addActionListener(e -> {


            List<Tecnico> selectedTecnici = tecniciList.getSelectedValuesList();
            if (selectedTecnici.size() == numTecniciDaSelezionare) {
                tecniciSelezionati.addAll(selectedTecnici);

                //Ricavate le matricole le aggiungiamo al JComboBox per poi scegliere il leader
                DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();

                for (Tecnico tecnico : tecniciSelezionati) {
                    comboBoxModel.addElement(tecnico.getMatricola().trim() + " " + tecnico.getNome().trim() + " " + tecnico.getCognome().trim());
                }

                matricolaLField.setModel(comboBoxModel);

                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Seleziona esattamente " + numTecniciDaSelezionare + " tecnici.", "Selezione errata", JOptionPane.WARNING_MESSAGE);
            }
        });

        dialog.setSize(300, 400);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setLocationRelativeTo(null);
        SwingUtilities.invokeLater(() -> tecniciList.requestFocusInWindow());
        dialog.setVisible(true);
        try {
            BufferedImage iconImage = ImageIO.read(new File("src/GUI/icon/icons8-laboratorio-64.png"));
            dialog.setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        };

        SwingUtilities.invokeLater(() -> tecniciList.requestFocusInWindow());
        return tecniciSelezionati;
    }

    private void creazioneTeameAssegnamentoTecnici() {
        String codiceTeam = getCodTeamNew(); // Codice del team appena creato
        Team teamCreato = controller.recuperoTeamC(codiceTeam);

        // Assegna il codice del team ai tecnici memorizzati temporaneamente
        for (Tecnico tecnico : tecniciSelezionati) {
            tecnico.setTeam(teamCreato);
            boolean updated = controller.updateTecniciC(tecnico, codiceTeam);

            if (!updated) {
                System.out.println("Aggiornamento non avvenuto");
            }

        }

    }

}

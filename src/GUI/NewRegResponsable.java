package GUI;

import DAO.SedeDAO;
import DTO.Responsabile;
import DTO.Sede;
import UTILITIES.Controller;
import UTILITIES.EmailSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

public class NewRegResponsable extends JPanel {

    private final static JLabel matricolaText = new JLabel("Matricola");
    private final static JLabel nomeText = new JLabel("Nome");
    private final static JLabel cognomeText = new JLabel("Cognome");
    private final static JLabel passwordText = new JLabel("Password");
    private final static JLabel emailText = new JLabel("Email");
    private final static JLabel sedeText = new JLabel("Sede di appartenenza");


    private final static BtnLayout backButton = new BtnLayout("Indietro");
    private final static BtnLayout signInButton = new BtnLayout("Registrati");

    private TextFieldBorderColor matricolaField;
    private TextFieldBorderColor nomeField;
    private TextFieldBorderColor cognomeField;
    private TextFieldBorderColor emailField;
    private JPasswordField passwordField;
    private JComboBox sedeComboBox; // Dichiarazione della JComboBox per le sedi

    Controller myController;
    Sede sedeSelezionata;
    int codiceSede;

    private static BufferedImage backgroundImageNew;


    public NewRegResponsable(Controller controller) {

        myController = controller;
        setLayout(new GridLayout(0,2));

        JPanel rightPage = new JPanel();
        rightPage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        //Testo matricola
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(matricolaText, gbc);

        //Testo nome
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(nomeText, gbc);

        //Testo cognome
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(cognomeText, gbc);

        //Testo email
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(emailText, gbc);

        //Testo password
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(passwordText, gbc);

        // Testo per la sede
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(sedeText, gbc);

        //Campo matricola
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        matricolaField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(matricolaField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(matricolaField, gbc);

        //Campo nome
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        nomeField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(nomeField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(nomeField, gbc);

        //Campo cognome
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        cognomeField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(cognomeField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(cognomeField, gbc);

        //Campo email
        gbc.gridy = 7;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        emailField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(emailField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(emailField, gbc);

        //Campo password
        gbc.gridy = 9;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(15);
        TextFieldBorderColor.changeTextFieldBorderColor(passwordField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(passwordField, gbc);

        //Posizionamento occhio per visualizzare password

        JButton pwdEye = new JButton();
        try {
            BufferedImage eyeImage = ImageIO.read(new File("src/GUI/icon/eye (1).png"));
            pwdEye.setIcon(new ImageIcon(eyeImage));

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine occhio ");
        }

        //Nascondere il layout del pulsante (occhio password)
        pwdEye.setContentAreaFilled(false);
        pwdEye.setBorderPainted(false);
        GridBagConstraints pwdEyeGbc = new GridBagConstraints();

        //Chiamata al metodo per mostrare/nascondere la password
        pwdEye.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPassword();
            }
        });

        pwdEyeGbc.gridx = 2;
        pwdEyeGbc.gridy = 9;
        pwdEyeGbc.anchor = GridBagConstraints.LINE_END;  // Allinea il pulsante alla fine della colonna
        rightPage.add(pwdEye,pwdEyeGbc);



        // Recupera la lista delle sedi dal database
        SedeDAO sedeDAO = new SedeDAO(controller);
        List<Sede> sedi = sedeDAO.recuperaListaSediDalDB();

        // Crea una lista per i nomi delle sedi
        List<String> nomiSedi = new ArrayList<>();

        //sedeComboBox.addItem("");
        // Estrai i nomi delle sedi dalla lista di oggetti Sede
        for (Sede sede : sedi) {
            nomiSedi.add(sede.getNome());
        }

        // Converte la lista di nomi delle sedi in un array per il JComboBox
        String[] nomiSediArray = nomiSedi.toArray(new String[nomiSedi.size()]);

        // Crea e aggiungi il JComboBox con i nomi delle sedi
        sedeComboBox = new JComboBox<>(nomiSediArray);
        sedeComboBox.setBackground(Color.white);

        sedeComboBox.setBorder(new LineBorder(new Color(35,171,144),1));
        sedeComboBox.insertItemAt("",0);

        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(sedeComboBox, gbc);

        // Aggiungi un listener alla tua JComboBox

        sedeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String nomeSedeSelezionata = (String) event.getItem();
                    codiceSede = sedeDAO.codificaSedeDAO(nomeSedeSelezionata); // Ottieni il codice della sede selezionata dal database
                    sedeSelezionata = new Sede(codiceSede, nomeSedeSelezionata, null);
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

                ResponsibleAccess paginaAccesso = new ResponsibleAccess(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewRegResponsable.this);

                mainWindow.addCardPanel(paginaAccesso, "accesso");

            }
        });


        //Bottone registrati
        gbc.gridy = 12;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(15,15,0,0);
        rightPage.add(signInButton, gbc);

        //Azioni dopo che il bottone registrati viene premuto

        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(getMatricolaNew().isEmpty() || getNomeNew().isEmpty() || getCognomeNew().isEmpty() || getEmailNew().isEmpty() || getPasswordNew().length == 0) {

                    JOptionPane.showMessageDialog(null,"I campi non possono essere vuoti.");

                } else if  (getMatricolaNew().length() != 5 || !getMatricolaNew().startsWith("R") || !getMatricolaNew().substring(1).matches("\\d+")) //La condizione !matricola.substring(1).matches("\\d+") verifica che tutti i caratteri successivi al primo carattere  siano cifre. Ecco come funziona:

                    //matricola.substring(1) estrae una sottostringa da matricola partendo dal secondo carattere in poi //matches("\\d+") utilizza l'espressione regolare \\d+ per verificare se la sottostringa contiene solo cifre. \\d in un'espressione regolare corrisponde a una singola cifra da 0 a 9. + indica che la sequenza di cifre può essere lunga uno o più caratteri.
                    {
                    // Mostra la finestra di avviso
                    JOptionPane.showMessageDialog(null, "Matricola errata. Ti ricordiamo che la matricola dei responsabili inizia con 'R' seguito da 4 numeri.", "Errore Matricola", JOptionPane.ERROR_MESSAGE);}
                else {

                    //Chiamo la classe DTO che incapsula le informazioni del nuovo utente
                    Responsabile nuovoResponsabile = new Responsabile(getMatricolaNew(),getNomeNew(), getCognomeNew(),null,null,getEmailNew(),getPasswordNew(),sedeSelezionata);

                    String registerCode;
                    String verificationRegisterCode;


                    //Verifica che matricola e mailnon siano già usati nel database

                    Responsabile matricolaR = new Responsabile(getMatricolaNew(),null,null,null,null, getEmailNew(), null,null);

                    //Controlliamo prima che matricola e email non siano presenti e poi inviamo mail

                    if (!myController.verifyMatricolaMailResponsabileC(matricolaR)) {

                        registerCode = generateRandomCode();

                        //Invio del codice tramite mail

                        EmailSender.sendVerificationCode(getEmailNew(), registerCode, "Codice Registrazione", "Inserisci il codice " +
                                "per completare la registrazione: ");

                        //Finestra pop-up per inserire codice inviato tramite mail

                        verificationRegisterCode = JOptionPane.showInputDialog(null, "Ti abbiamo inviato una mail con un codice, inseriscilo qui sotto: ",
                                "Inserisci Codice", JOptionPane.PLAIN_MESSAGE);

                        if (verificationRegisterCode != null && verificationRegisterCode.equals(registerCode)) {

                            boolean complete = myController.newRespRegister(nuovoResponsabile);

                            if (complete) {

                                JOptionPane.showMessageDialog(null, "La tua registrazione è avvenuta correttamente!");

                                ResponsibleAccess paginaAccesso = new ResponsibleAccess(myController);

                                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewRegResponsable.this);

                                mainWindow.addCardPanel(paginaAccesso, "accesso");


                            }

                        } else if (verificationRegisterCode != null) {
                            JOptionPane.showMessageDialog(null, "Il codice inserito non è corretto, richiedi un nuovo codice");
                        }


                    } else {
                        JOptionPane.showMessageDialog(null, "Attenzione, la matricola e l'email  appartengono già ad un nostro responsabile! ");
                    }

                }

            }
        });


        //Impostazione Background

        /*Utilizzo della sottoclasse di JPanel (classe anonima) per evitare di creare una nuova classe
         * e quindi personalizziamo solo il Pannello sinistro per impostare il background altrimenti non avremmo
         * potuto impostarlo.*/


        JPanel leftSignInPage = new JPanel() {
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
            backgroundImageNew = ImageIO.read(new File("src/GUI/icon/sign.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background NewReh");
            ex.printStackTrace();
        }

        add(leftSignInPage);
        add(rightPage);


    }

    public String getMatricolaNew() {

        return matricolaField.getText().trim();
    }

    public String getNomeNew() {

        return nomeField.getText().trim();
    }

    public String getCognomeNew() {

        return cognomeField.getText().trim();
    }

    public String getEmailNew() {

        return emailField.getText().trim();
    }

    public char[] getPasswordNew() {

        return passwordField.getPassword();
    }

    private void viewPassword() {
        if (passwordField.getEchoChar() == '\u2022') {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('\u2022');
        }
    }

    //Metodo per generare un codice di verifica casuale
    public static String generateRandomCode() {

        String randomCode = null;

        try {
            UUID uuid = UUID.randomUUID();
            randomCode = uuid.toString().replaceAll("-", "").substring(0, 6); // Estrarre i primi sei caratteri

        } catch (Exception ex) {

            System.out.println("Errore nella generazione del codice");
            System.out.println(randomCode);

        }

        return randomCode;
    }


}

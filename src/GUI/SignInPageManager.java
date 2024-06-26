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

public class SignInPageManager extends JPanel {

    private final static JLabel matriculationNumberText = new JLabel("Matricola");
    private final static JLabel textName = new JLabel("Nome");
    private final static JLabel textSurname = new JLabel("Cognome");
    private final static JLabel passwordText = new JLabel("Password");
    private final static JLabel textMail = new JLabel("Email");
    private final static JLabel headQuartersText = new JLabel("Sede di appartenenza");

    private final TextFieldBorderColor matriculationNumberField;
    private final TextFieldBorderColor nameField;
    private final TextFieldBorderColor surnameField;
    private final TextFieldBorderColor emailField;
    private final JPasswordField passwordField;

    Controller myController;
    Sede selectedHeadquarter;
    int headQuarterCode;

    private static BufferedImage backgroundImageLeft;
    private static BufferedImage backgroundImageRight;


    public SignInPageManager(Controller controller) {

        myController = controller;
        setLayout(new GridLayout(0,2));

        JPanel rightPage =  new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo con interpolazione bilineare
                if (backgroundImageRight != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundImageRight, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            backgroundImageRight = ImageIO.read(new File("src/GUI/icon/sfondoR.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background responsabile");
            ex.printStackTrace();

        }

        rightPage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        //Testo matricola
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(matriculationNumberText, gbc);

        //Testo nome
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(textName, gbc);

        //Testo cognome
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(textSurname, gbc);

        //Testo email
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(textMail, gbc);

        //Testo password
        gbc.gridy = 8;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(passwordText, gbc);

        // Testo per la sede
        gbc.gridy = 10;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(headQuartersText, gbc);

        //Campo matricola
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        matriculationNumberField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(matriculationNumberField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(matriculationNumberField, gbc);

        //Campo nome
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        nameField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(nameField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(nameField, gbc);

        //Campo cognome
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        surnameField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(surnameField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightPage.add(surnameField, gbc);

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

        matriculationNumberField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                matriculationNumberField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                matriculationNumberField.setBorder(new LineBorder(Color.BLACK));
            }
        });

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

        surnameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                surnameField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                surnameField.setBorder(new LineBorder(Color.BLACK));
            }
        });

        emailField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                emailField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                emailField.setBorder(new LineBorder(Color.BLACK));
            }
        });

        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                passwordField.setBorder(new LineBorder(new Color(246, 183, 55), 2));
            }
            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(new LineBorder(Color.BLACK));
            }
        });




        //Posizionamento occhio per visualizzare password

        JButton pwdEye = new JButton();
        try {
            NoScalingIcon noScalingEye = new NoScalingIcon(new ImageIcon("src/GUI/icon/hide.png"));
            pwdEye.setIcon(noScalingEye);

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine occhio - pagina nuovo responsabile ");
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

        pwdEyeGbc.gridx = 1;
        pwdEyeGbc.gridy = 9;
        pwdEyeGbc.anchor = GridBagConstraints.LINE_END;
        pwdEyeGbc.insets = new Insets(0,0,0,0);
        rightPage.add(pwdEye,pwdEyeGbc);

        // Recupera la lista delle sedi dal database
        SedeDAO sedeDAO = new SedeDAO(controller);
        List<Sede> sedi = sedeDAO.recuperaListaSediDalDB();

        // Crea una lista per i nomi delle sedi
        List<String> headQuartersNames = new ArrayList<>();

        // Estrai i nomi delle sedi dalla lista di oggetti Sede
        for (Sede sede : sedi) {
            headQuartersNames.add(sede.getNome());
        }

        // Converte la lista di nomi delle sedi in un array per il JComboBox
        String[] arrayHeadquartersNames = headQuartersNames.toArray(new String[headQuartersNames.size()]);

        // Crea e aggiungi il JComboBox con i nomi delle sedi
        // Dichiarazione della JComboBox per le sedi
        JComboBox sedeComboBox = new JComboBox<>(arrayHeadquartersNames);
        sedeComboBox.setBackground(Color.white);

        gbc.gridy = 11;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightPage.add(sedeComboBox, gbc);

        // Aggiunta listener alla JComboBox
        sedeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent event) {
                if (event.getStateChange() == ItemEvent.SELECTED) {
                    String nameSelectedHeadquarter = (String) event.getItem();
                    headQuarterCode = sedeDAO.codificaSedeDAO(nameSelectedHeadquarter); // Ottieni il codice della sede selezionata dal database
                    selectedHeadquarter = new Sede(headQuarterCode, nameSelectedHeadquarter, null);
                }
            }
        });

        //Bottone indietro
        JButton backButton = new JButton("Indietro");
        backButton.setBackground(new Color(23,65,95));
        backButton.setForeground(Color.WHITE);
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

                ManagerLoginPage managerLoginPage = new ManagerLoginPage(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(SignInPageManager.this);

                mainWindow.addCardPanel(managerLoginPage, "accesso");

            }
        });

        //Bottone registrati
        JButton signInButton = new JButton("Registrati");
        signInButton.setBackground(new Color(23,65,95));
        signInButton.setForeground(Color.WHITE);

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

                } else if  (getMatricolaNew().length() != 5 || !getMatricolaNew().startsWith("R") || !getMatricolaNew().substring(1).matches("\\d+"))
                    /*La condizione !matricola.substring(1).matches("\\d+") verifica che tutti i caratteri successivi al primo carattere  siano cifre.
                    Ecco come funziona:matricola.substring(1) estrae una sottostringa da matricola partendo dal secondo carattere in poi
                    matches("\\d+") utilizza l'espressione regolare
                    d+ per verificare se la sottostringa contiene solo cifre.
                    d in un'espressione regolare corrisponde a una singola cifra da 0 a 9.
                    + indica che la sequenza di cifre può essere lunga uno o più caratteri.*/

                {
                    // Mostra la finestra di avviso
                    JOptionPane.showMessageDialog(null, "Matricola errata. Ti ricordiamo che la matricola dei responsabili inizia con 'R' seguito da 4 numeri.", "Errore Matricola", JOptionPane.ERROR_MESSAGE);}

                else {

                    //Chiamo la classe DTO che incapsula le informations del nuovo responsabile
                    Responsabile newManager = new Responsabile(getMatricolaNew(),getNomeNew(), getCognomeNew(),null,null,getEmailNew(),getPasswordNew(),selectedHeadquarter);

                    String registerCode;
                    String verificationRegisterCode;

                    //Verifica che matricola e mailnon siano già usati nel database
                    Responsabile matricolaR = new Responsabile(getMatricolaNew(),null,null,null,null, getEmailNew(), null,null);

                    //Controlliamo prima che matricola e email non siano presenti e poi inviamo mail
                    if (!myController.mailManagerMatriculationVerifyC(matricolaR)) {

                        registerCode = generateRandomCode();

                        //Invio del codice tramite mail

                        EmailSender.sendVerificationCode(getEmailNew(), registerCode, "Codice Registrazione", "Inserisci il codice " +
                                "per completare la registrazione: ");

                        //Finestra pop-up per inserire codice inviato tramite mail

                        verificationRegisterCode = JOptionPane.showInputDialog(null, "Ti abbiamo inviato una mail con un codice, inseriscilo qui sotto: ",
                                "Inserisci Codice", JOptionPane.PLAIN_MESSAGE);

                        if (verificationRegisterCode != null && verificationRegisterCode.equals(registerCode)) {

                            boolean complete = myController.newRespRegister(newManager);

                            if (complete) {

                                JOptionPane.showMessageDialog(null, "La tua registrazione è avvenuta correttamente!");

                                ManagerLoginPage managerLoginPage = new ManagerLoginPage(myController);

                                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(SignInPageManager.this);

                                mainWindow.addCardPanel(managerLoginPage, "accesso");

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

        JPanel leftSignInPage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (backgroundImageLeft != null) {
                    g.drawImage(backgroundImageLeft, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        //Impostazione sfondo background di sinistra
        try {
            backgroundImageLeft = ImageIO.read(new File("src/GUI/icon/sign.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background NewRegleft");
            ex.printStackTrace();
        }

        //Impostazione sfondo background di destra
        try {
            backgroundImageRight = ImageIO.read(new File("src/GUI/icon/SfondoR.png"));

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background NewRegright");
            ex.printStackTrace();
        }

        add(leftSignInPage);
        add(rightPage);

    }
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImageLeft != null) {
            g.drawImage(backgroundImageLeft, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public String getMatricolaNew() {

        return matriculationNumberField.getText().trim();
    }

    public String getNomeNew() {

        return nameField.getText().trim();
    }

    public String getCognomeNew() {

        return surnameField.getText().trim();
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

        try {
            UUID uuid = UUID.randomUUID();
            return uuid.toString().replaceAll("-", "").substring(0, 6); // Estrarre i primi sei caratteri

        } catch (Exception ex) {

            System.out.println("Errore nella generazione del codice");

        }

        return null;

    }

}
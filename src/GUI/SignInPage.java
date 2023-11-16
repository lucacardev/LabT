package GUI;

import DTO.Utente;
import UTILITIES.Controller;
import UTILITIES.EmailSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.sql.SQLException;
import java.util.UUID;

public class SignInPage extends JPanel {

    private final static JLabel usernameText = new JLabel("Username");
    private final static JLabel passwordText = new JLabel("Password");
    private final static JLabel emailText = new JLabel("Email");

    private final static BtnLayout backButton = new BtnLayout("Indietro");
    private final static BtnLayout signInButton = new BtnLayout("Registrati");

    private TextFieldBorderColor emailField;

    private TextFieldBorderColor usernameField;

    private JPasswordField passwordField;

    private static BufferedImage backgroundImageSignin;


    private JPanel leftSignInPage = new JPanel();

    Controller myController;



    public SignInPage(Controller controller) {

        myController = controller;

        setLayout(new GridLayout(0,2));

        JPanel rightSignInPage = new JPanel();
        rightSignInPage.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        //Testo username
        gbc.gridy = 0;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightSignInPage.add(usernameText, gbc);

        //Testo email
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightSignInPage.add(emailText, gbc);

        //Testo password
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        rightSignInPage.add(passwordText, gbc);

        //Campo username
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        usernameField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(usernameField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightSignInPage.add(usernameField, gbc);

        //Campo email
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        emailField = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(emailField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightSignInPage.add(emailField, gbc);

        //Campo password
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        passwordField = new JPasswordField(15);
        TextFieldBorderColor.changeTextFieldBorderColor(passwordField);
        gbc.anchor = GridBagConstraints.CENTER;
        rightSignInPage.add(passwordField, gbc);

        //Bottone indietro
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        gbc.insets = new Insets(15,0,0,15);
        rightSignInPage.add(backButton, gbc);

        //Indirizzamento alla pagina di login

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                PaginaLogin paginaLogin = new PaginaLogin(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(SignInPage.this);

                mainWindow.addCardPanel(paginaLogin, "login");

            }
        });

        //Bottone registrati
        gbc.gridy = 6;
        gbc.gridx = 1;
        gbc.gridwidth = 1;
        gbc.weightx = 0;
        gbc.weighty = 0;
        gbc.anchor = GridBagConstraints.FIRST_LINE_END;
        gbc.insets = new Insets(15,15,0,0);
        rightSignInPage.add(signInButton, gbc);

        //Azioni dopo che il bottone registrati viene premuto

        signInButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                if(getEmailSignIn().isEmpty() || getUsernameSignIn().isEmpty() || getPasswordSignIn().length == 0) {

                    JOptionPane.showMessageDialog(null,"I campi non possono essere vuoti.");

                } else {

                    //Chiamo la classe DTO che incapsula le informazioni del nuovo utente
                    Utente nuovoUtente = new Utente(getUsernameSignIn(), getEmailSignIn(), getPasswordSignIn());

                    String registerCode;
                    String verificationRegisterCode;


                    //Verifica che mail e username non siano già usati nel database

                    Utente mailUsername = new Utente(getUsernameSignIn(), getEmailSignIn(), null);

                    //Controlliamo prima che email e username non siano presenti e poi inviamo mail

                        if (!myController.verifyMailUsernameC(mailUsername)) {

                            registerCode = generateRandomCode();

                            //Invio del codice tramite mail

                            EmailSender.sendVerificationCode(getEmailSignIn(), registerCode, "Codice Registrazione", "Inserisci il codice " +
                                    "per completare la registrazione: ");

                            //Finestra pop-up per inserire codice inviato tramite mail

                            verificationRegisterCode = JOptionPane.showInputDialog(null, "Ti abbiamo inviato una mail con un codice, inseriscilo qui sotto: ",
                                    "Inserisci Codice", JOptionPane.PLAIN_MESSAGE);

                            if (verificationRegisterCode != null && verificationRegisterCode.equals(registerCode)) {


                                boolean complete = myController.newUserRegister(nuovoUtente);

                                if (complete) {

                                    JOptionPane.showMessageDialog(null, "La tua registrazione è avvenuta correttamente!");

                                    PaginaLogin paginaLogin = new PaginaLogin(myController);

                                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(SignInPage.this);

                                    mainWindow.addCardPanel(paginaLogin, "login");


                                }

                            } else if (verificationRegisterCode != null) {
                                JOptionPane.showMessageDialog(null, "Il codice inserito non è corretto, richiedi un nuovo codice");
                            }


                        } else {
                            JOptionPane.showMessageDialog(null, "Attenzione, email o username già appartengono ad un nostro utente! ");
                        }

                }

            }
        });

        //Impostazione Background

        /*Utilizzo della sottoclasse di JPanel (classe anonima) per evitare di creare una nuova classe
         * e quindi personalizziamo solo il Pannello sinistro per impostare il background altrimenti non avremmo
         * potuto impostarlo.*/

        leftSignInPage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (backgroundImageSignin != null) {
                    g.drawImage(backgroundImageSignin, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        //Impostazione sfondo background di sinistra

        try {
            backgroundImageSignin = ImageIO.read(new File("C:/Users/cardo/Desktop/LabT/src/GUI/icon/sign.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background signIn");
            ex.printStackTrace();
        }

        add(leftSignInPage);
        add(rightSignInPage);


    }

    public String getEmailSignIn() {

        return emailField.getText().trim();
    }

    public char[] getPasswordSignIn() {

        return passwordField.getPassword();
    }

    public String getUsernameSignIn() {

        return usernameField.getText().trim();

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
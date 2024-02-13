package GUI;

import DTO.Utente;
import UTILITIES.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class RightLoginAccess extends JPanel {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private BufferedImage rightLoginBackground;
    Controller myController;
    private Utente currentUser;

    public RightLoginAccess(Controller controller) {

        this.myController = controller;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        setBackground(new Color(229, 226, 226));

        //Spazio tra i componenti
        gbc.insets = new Insets(5, 5, 5, 5);

        //Impostazione background

        try {

            rightLoginBackground = ImageIO.read(new File("src/GUI/icon/background.png"));


        } catch (Exception ex) {

            System.out.println("Errore caricamento immagine background login page");

        }

        //Testo email
        JLabel textMail = new JLabel("Email: ");
        Font fonttextMail = textMail.getFont();
        int sizetextMail = fonttextMail.getSize() + 3;
        Font increasetextMail = fonttextMail.deriveFont((float) sizetextMail);
        textMail.setFont(increasetextMail);
        textMail.setForeground(Color.BLACK);

        add(textMail);

        //Campo email
        emailField = new TextFieldBorderColor(15);

        //Risalto colore dei bordi del campo email quando cliccato
        TextFieldBorderColor.changeTextFieldBorderColor(emailField);

        //Testo password
        JLabel pwdText = new JLabel("Password: ");
        pwdText.setFont(increasetextMail);
        pwdText.setForeground(Color.BLACK);

        //Posizionamento testo mail
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(textMail, gbc);

        //Posizionamento campo mail
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(emailField, gbc);

        //Posizionamento testo password
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(pwdText, gbc);

        //Posizionamento campo password
        passwordField = new JPasswordField(15);
        passwordField.setBorder(new LineBorder(Color.BLACK, 1));
        passwordField.setEchoChar('•');
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(passwordField, gbc);

        //Risalto colore dei bordi del campo password quando cliccato
        passwordField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                passwordField.setBorder(new LineBorder(new Color(35, 171, 144), 2));

            }

            @Override
            public void focusLost(FocusEvent e) {
                passwordField.setBorder(new LineBorder(Color.BLACK));
            }

        });

        //Posizionamento occhio per visualizzare password
        JButton pwdEye = new JButton();

        try {

            NoScalingIcon pwdEyeNoScale = new NoScalingIcon(new ImageIcon("src/GUI/icon/hide.png"));
            pwdEye.setIcon(pwdEyeNoScale);

        } catch (Exception ex) {

            System.out.println("Errore caricamento immagine occhio - pagina Login");

        }

        //Modo per cambiare il focus del cursore da emailField a passwordField
        Set<AWTKeyStroke> forwardKeys;
        forwardKeys = new HashSet<>(emailField.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.remove(KeyStroke.getKeyStroke("TAB"));
        emailField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        emailField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
                    passwordField.requestFocus();
                    evt.consume(); // Consuma l'evento Tab per evitare il comportamento predefinito
                }
            }
        });

        //Nascondere il layout del pulsante (occhio password)
        pwdEye.setContentAreaFilled(false);
        pwdEye.setBorderPainted(false);
        GridBagConstraints pwdEyeGbc = new GridBagConstraints();
        pwdEyeGbc.insets = new Insets(0, -60, 0, 0);

        //Chiamata al metodo per mostrare/nascondere la password

        pwdEye.addActionListener(e -> viewPassword());

        pwdEyeGbc.gridx = 2;
        pwdEyeGbc.gridy = 4;
        pwdEyeGbc.anchor = GridBagConstraints.LINE_START;  // Allinea il pulsante all'inizio della colonna
        add(pwdEye, pwdEyeGbc);

        //Posizionamento password dimenticata
        LinkMouseOn forgottenPassword = new LinkMouseOn("Hai dimenticato la password?");
        gbc.gridx = 0;
        gbc.gridy = 6;

        //Azione se il testo password dimenticata viene cliccato
        forgottenPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Apri finestra d'inserimento email per recupero credenziali

                PasswordRecovery passwordRecoveryPanel = new PasswordRecovery(myController);

                /*Questo metodo ci permettere di risalire al frame padre di RightLoginAccess
                * in questo modo quando cliccheremo sul testo non si aprirà una nuova finestra
                * ma avremo lo stesso frame*/

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

                mainWindow.addCardPanel(passwordRecoveryPanel, "passwordRecoveryPanel");

            }
        });

        forgottenPassword.ActiveLinkMouseOn(new Color(35, 171, 144), Color.black);

        add(forgottenPassword, gbc);

        //Inserimento bottone di accesso
        BtnLayout loginButton = new BtnLayout("Accedi");

        //Posizionamento bottone di accesso
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(loginButton, gbc);

        //Posizionamento registrazione nuovo utente
        LinkMouseOn userReg = new LinkMouseOn("Sei Nuovo? Registrati");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(userReg, gbc);

        //Indirizzamento pagina per la registrazione
        userReg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                SignInPage signInPage = new SignInPage(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

                mainWindow.addCardPanel(signInPage, "signInPage");
            }
        });

        //Cambio colore al passaggio del mouse
        userReg.ActiveLinkMouseOn(new Color(35, 171, 144), Color.BLACK);

        //Posizionamento accesso responsabile
        LinkMouseOn managerLogin = new LinkMouseOn("Sei un responsabile? Clicca qui");
        gbc.gridx = 0;
        gbc.gridy = 9;
        add(managerLogin, gbc);

        //Indirizzamento pagina per la registrazione
        managerLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                ResponsibleAccess responsibleAccess = new ResponsibleAccess(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

                mainWindow.addCardPanel(responsibleAccess, "responsableAccess");
            }
        });

        //Cambio colore al passaggio del mouse
        managerLogin.ActiveLinkMouseOn(new Color(35, 171, 144), Color.black);


        //Verifica credenziali quando il pulsante di accesso viene premuto

        loginButton.addActionListener(e -> {

            int msgError;

            msgError = credentialsVerification();
            showError(msgError);

        });

    }

    //Metodo per impostare l'immagine di background
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Disegna l'immagine di sfondo
        if (rightLoginBackground != null) {
            g.drawImage(rightLoginBackground, 0, 0, getWidth(), getHeight(), this);
        }

    }

    //Visualizzazione messaggio di errore
    public void showError(int errorMassage) {

        if(errorMassage == 1) {

        JOptionPane.showMessageDialog(emailField, "I campi email e " +
                "password non possono essere vuoti");

        }

        else if (errorMassage == 2) {

        JOptionPane.showMessageDialog(emailField, "Credenziali errate" +
                " riprova o registrati");

    }
        else if(errorMassage == 3) {

            HomePage homePage = new HomePage(myController, currentUser);

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

            mainWindow.addCardPanel(homePage, "homePage");

    }

    }

    //Metodo per visualizzare e nascondere la password
    private void viewPassword() {

        if (passwordField.getEchoChar() == '•') {

            passwordField.setEchoChar((char) 0);

        } else {

            passwordField.setEchoChar('•');

        }

    }

    public String getLoginEmail() {

        return this.emailField.getText().trim();

    }

    public char[] getLoginPassword() {

        return this.passwordField.getPassword();

    }


    //Verifica delle credenziali quando il bottone viene premuto
    public int credentialsVerification() {

        final boolean[] userFound = new boolean[1];

        if (this.emailField.getText().isEmpty() || this.passwordField.getPassword().length == 0) {

            return 1;

        } else if (!this.emailField.getText().isEmpty() && this.passwordField.getPassword().length > 0) {

            currentUser = new Utente(null, getLoginEmail(), getLoginPassword());

            String usernameRecovered = myController.usernameRecovery(currentUser);

            currentUser = new Utente(usernameRecovered, getLoginEmail(), getLoginPassword());


            userFound[0] = myController.userVerification(currentUser);

            if (userFound[0]) {

                return 3;

            }

            return 2;

        }

        return 0;

    }

}
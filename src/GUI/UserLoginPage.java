package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import DTO.Utente;
import UTILITIES.Controller;

public class UserLoginPage extends JPanel{

    Controller myController;
    private final JTextField emailField;
    private final JPasswordField passwordField;
    private BufferedImage rightLoginBackground;
    private Utente currentUser;

    public UserLoginPage(Controller controller)  {

        myController = controller;

        //Diviamo la pagina a metà (destra e sinistra)
        setLayout(new GridLayout(0,2));

        //////////////////////////////////////////////LEFT PANEL///////////////////////////////////////////
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new GridBagLayout());

        JPanel upPanel = new JPanel();
        GridBagConstraints leftGbc = new GridBagConstraints();

        upPanel.setBackground(Color.WHITE);
        JLabel logo = new JLabel(new NoScalingIcon(new ImageIcon("src/GUI/icon/LogoLabT.png")));

        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.weightx = 1;
        leftGbc.insets = new Insets(0, 0, 0, 0);
        leftGbc.anchor = GridBagConstraints.CENTER;
        leftGbc.fill = GridBagConstraints.NONE;

        upPanel.add(logo);
        leftPanel.add(upPanel, leftGbc);

        //////////////////////////////////////////////RIGHT PANEL///////////////////////////////////////////

        JPanel rightPanel = new BackgroundPanel();
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        setBackground(new Color(229, 226, 226));

        //Spazio tra i componenti
        rightGbc.insets = new Insets(5, 5, 5, 5);

        //Impostazione background

        try {
            rightLoginBackground = ImageIO.read(new File("src/GUI/icon/background.png"));
        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background login page");
            ex.printStackTrace();
        }

        //Testo email
        JLabel textMail = new JLabel("Email: ");
        Font fontTextMail = textMail.getFont();
        int sizeTextMail = fontTextMail.getSize() + 3;
        Font increaseTextMail = fontTextMail.deriveFont((float) sizeTextMail);
        textMail.setFont(increaseTextMail);
        textMail.setForeground(Color.BLACK);

        rightPanel.add(textMail);

        //Campo email
        emailField = new JTextField(15);

        //Risalto colore dei bordi del campo email quando cliccato
        emailField.setBorder(new LineBorder(Color.BLACK, 1));

        //Testo password
        JLabel pwdText = new JLabel("Password: ");
        pwdText.setFont(increaseTextMail);
        pwdText.setForeground(Color.BLACK);

        //Posizionamento testo mail
        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.anchor = GridBagConstraints.LINE_START;
        rightPanel.add(textMail, rightGbc);

        //Posizionamento campo mail
        rightGbc.gridx = 0;
        rightGbc.gridy = 2;
        rightPanel.add(emailField, rightGbc);

        //Posizionamento testo password
        rightGbc.gridx = 0;
        rightGbc.gridy = 3;
        rightPanel.add(pwdText, rightGbc);

        //Posizionamento campo password
        passwordField = new JPasswordField(15);
        passwordField.setBorder(new LineBorder(Color.BLACK, 1));
        passwordField.setEchoChar('•');
        rightGbc.gridx = 0;
        rightGbc.gridy = 4;
        rightPanel.add(passwordField, rightGbc);

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
        rightPanel.add(pwdEye, pwdEyeGbc);

        //Posizionamento password dimenticata
        LinkMouseOn forgottenPassword = new LinkMouseOn("Hai dimenticato la password?");
        rightGbc.gridx = 0;
        rightGbc.gridy = 6;

        //Azione se il testo password dimenticata viene cliccato
        forgottenPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Apri finestra d'inserimento email per recupero credenziali

                PasswordRecovery passwordRecoveryPanel = new PasswordRecovery(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserLoginPage.this);

                mainWindow.addCardPanel(passwordRecoveryPanel, "passwordRecoveryPanel");

            }
        });

        forgottenPassword.ActiveLinkMouseOn(new Color(35, 171, 144), Color.black);

        rightPanel.add(forgottenPassword, rightGbc);

        //Inserimento bottone di accesso
        JButton loginButton = new JButton("Accedi");
        loginButton.setBackground(new Color(35,171,144));
        loginButton.setForeground(Color.white);

        //Posizionamento bottone di accesso
        rightGbc.gridx = 0;
        rightGbc.gridy = 5;
        rightPanel.add(loginButton, rightGbc);

        //Posizionamento registrazione nuovo utente
        LinkMouseOn userReg = new LinkMouseOn("Sei Nuovo? Registrati");
        rightGbc.gridx = 0;
        rightGbc.gridy = 7;
        rightPanel.add(userReg, rightGbc);

        //Indirizzamento pagina per la registrazione
        userReg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                SignInPage signInPage = new SignInPage(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserLoginPage.this);

                mainWindow.addCardPanel(signInPage, "signInPage");
            }
        });

        //Cambio colore al passaggio del mouse
        userReg.ActiveLinkMouseOn(new Color(35, 171, 144), Color.BLACK);

        //Posizionamento accesso responsabile
        LinkMouseOn managerLogin = new LinkMouseOn("Sei un responsabile? Clicca qui");
        rightGbc.gridx = 0;
        rightGbc.gridy = 9;
        rightPanel.add(managerLogin, rightGbc);

        //Indirizzamento pagina per la registrazione
        managerLogin.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                ResponsibleAccess responsibleAccess = new ResponsibleAccess(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserLoginPage.this);

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

        add(leftPanel);
        add(rightPanel);
    }

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

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserLoginPage.this);

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

    private class BackgroundPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Disegna l'immagine di sfondo
            if (rightLoginBackground != null) {
                g.drawImage(rightLoginBackground, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

}
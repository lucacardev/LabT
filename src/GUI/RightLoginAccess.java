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

public class RightLoginAccess extends JPanel {

    private final JTextField campoEmail;
    private final JPasswordField campoPassword;
    private BufferedImage rightLoginBackground;
    Controller myController;
    private Utente utenteCorrente;

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
        JLabel emailText = new JLabel("Email: ");
        Font fontEmailText = emailText.getFont();
        int sizeEmailText = fontEmailText.getSize() + 3;
        Font increaseEmailText = fontEmailText.deriveFont((float) sizeEmailText);
        emailText.setFont(increaseEmailText);
        emailText.setForeground(Color.BLACK);

        add(emailText);

        //Campo email
        campoEmail = new TextFieldBorderColor(15);

        //Risalto colore dei bordi del campo email quando cliccato
        TextFieldBorderColor.changeTextFieldBorderColor(campoEmail);

        //Testo password
        JLabel pwdText = new JLabel("Password: ");
        pwdText.setFont(increaseEmailText);
        pwdText.setForeground(Color.BLACK);

        //Posizionamento testo mail
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        add(emailText, gbc);

        //Posizionamento campo mail
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(campoEmail, gbc);

        //Posizionamento testo password
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(pwdText, gbc);

        //Posizionamento campo password
        campoPassword = new JPasswordField(15);
        campoPassword.setBorder(new LineBorder(Color.BLACK, 1));
        campoPassword.setEchoChar('•');
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(campoPassword, gbc);

        //Risalto colore dei bordi del campo password quando cliccato
        campoPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                campoPassword.setBorder(new LineBorder(new Color(35, 171, 144), 2));

            }

            @Override
            public void focusLost(FocusEvent e) {
                campoPassword.setBorder(new LineBorder(Color.BLACK));
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
        LinkMouseOn passwordDimenticata = new LinkMouseOn("Hai dimenticato la password?");
        gbc.gridx = 0;
        gbc.gridy = 6;

        //Azione se il testo password dimenticata viene cliccato
        passwordDimenticata.addMouseListener(new MouseAdapter() {
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

        passwordDimenticata.ActiveLinkMouseOn(new Color(35, 171, 144), Color.black);

        add(passwordDimenticata, gbc);

        //Inserimento bottone di accesso
        BtnLayout loginButton = new BtnLayout("Accedi");

        //Posizionamento bottone di accesso
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(loginButton, gbc);

        //Posizionamento registrazione nuovo utente
        LinkMouseOn regUtente = new LinkMouseOn("Sei Nuovo? Registrati");
        gbc.gridx = 0;
        gbc.gridy = 7;
        add(regUtente, gbc);

        //Indirizzamento pagina per la registrazione
        regUtente.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                SignInPage signInPage = new SignInPage(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

                mainWindow.addCardPanel(signInPage, "signInPage");
            }
        });

        //Cambio colore al passaggio del mouse
        regUtente.ActiveLinkMouseOn(new Color(35, 171, 144), Color.BLACK);

        //Posizionamento accesso responsabile
        LinkMouseOn accResponsabile = new LinkMouseOn("Sei un responsabile? Clicca qui");
        gbc.gridx = 0;
        gbc.gridy = 9;
        add(accResponsabile, gbc);

        //Indirizzamento pagina per la registrazione
        accResponsabile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                ResponsibleAccess responsibleAccess = new ResponsibleAccess(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

                mainWindow.addCardPanel(responsibleAccess, "responsableAccess");
            }
        });

        //Cambio colore al passaggio del mouse
        accResponsabile.ActiveLinkMouseOn(new Color(35, 171, 144), Color.black);


        //Verifica credenziali quando il pulsante di accesso viene premuto

        loginButton.addActionListener(e -> {

            int msgError;

            msgError = verificaCredenziali();
            mostraErrore(msgError);

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
    public void mostraErrore(int messaggioErrore) {

        if(messaggioErrore == 1) {

        JOptionPane.showMessageDialog(campoEmail, "I campi email e " +
                "password non possono essere vuoti");

        }

        else if (messaggioErrore == 2) {

        JOptionPane.showMessageDialog(campoEmail, "Credenziali errate" +
                " riprova o registrati");

    }
        else if(messaggioErrore == 3) {

            HomePage homePage = new HomePage(myController, utenteCorrente);

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(RightLoginAccess.this);

            mainWindow.addCardPanel(homePage, "homePage");

    }

    }

    //Metodo per visualizzare e nascondere la password
    private void viewPassword() {

        if (campoPassword.getEchoChar() == '•') {

            campoPassword.setEchoChar((char) 0);

        } else {

            campoPassword.setEchoChar('•');

        }

    }

    public String getLoginEmail() {

        return this.campoEmail.getText().trim();

    }

    public char[] getLoginPassword() {

        return this.campoPassword.getPassword();

    }


    //Verifica delle credenziali quando il bottone viene premuto
    public int verificaCredenziali() {

        final boolean[] utenteTrovato = new boolean[1];

        if (this.campoEmail.getText().isEmpty() || this.campoPassword.getPassword().length == 0) {

            return 1;

        } else if (!this.campoEmail.getText().isEmpty() && this.campoPassword.getPassword().length > 0) {

            utenteCorrente = new Utente(null, getLoginEmail(), getLoginPassword());

            String usernameRecuperato = myController.usernameRecovery(utenteCorrente);

            utenteCorrente = new Utente(usernameRecuperato, getLoginEmail(), getLoginPassword());


            utenteTrovato[0] = myController.verificaUtente(utenteCorrente);

            if (utenteTrovato[0]) {

                return 3;

            }

            return 2;

        }

        return 0;

    }

}
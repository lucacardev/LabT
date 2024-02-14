package GUI;

import DTO.Responsabile;
import UTILITIES.Controller;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class ManagerLoginPage extends JPanel {

    private final JTextField matriculationNumberField  ;
    private final JPasswordField passwordField;
    private static BufferedImage backgroundRight;
    Controller myController;

    private Responsabile currentManager;

    public ManagerLoginPage(Controller controller) {

        this.myController = controller;

        setLayout(new GridLayout(0,2));

        // Disegna l'immagine di sfondo con interpolazione bilineare
        JPanel rightRApage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo con interpolazione bilineare
                if (backgroundRight != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(backgroundRight, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            backgroundRight = ImageIO.read(new File("src/GUI/icon/sfondoR.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background responsabile");
            ex.printStackTrace();

        }

        rightRApage.setLayout(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(5,5,5,5);

        ImageIcon loginImage = new ImageIcon("src/GUI/icon/avatarR.gif");
        JLabel imageLabel = new JLabel(loginImage);

        // Posiziona l'immagine al centro del pannello
        rightGbc.gridx = 1;
        rightGbc.gridy = 0;
        rightGbc.gridwidth = 2; // Occupa due colonne orizzontali
        rightRApage.add(imageLabel, rightGbc);

        //Testo matricola
        JLabel matriculationNumberText = new JLabel("Matricola: ");
        matriculationNumberText.setForeground(new Color(23,65,95));
        Font fontMatriculationNumberText = matriculationNumberText.getFont();
        int sizeMatriculationNumberText = fontMatriculationNumberText.getSize() + 4;
        Font increaseMatriculationNumberText = fontMatriculationNumberText.deriveFont((float) sizeMatriculationNumberText);
        matriculationNumberText.setFont(increaseMatriculationNumberText);
        matriculationNumberText.setForeground(Color.BLACK);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 1;
        rightGbc.anchor = GridBagConstraints.LINE_START;
        rightRApage.add(matriculationNumberText, rightGbc);

        //Campo matricola
        matriculationNumberField   = new TextFieldBorderColor(15);

        //Risalto colore dei bordi del campo matricola quando cliccato
        matriculationNumberField  .setBorder(new LineBorder(Color.BLACK, 1));
        TextFieldBorderColor.changeTextFieldBorderColor(matriculationNumberField  );

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 2;
        rightGbc.anchor = GridBagConstraints.LINE_START;
        rightRApage.add(matriculationNumberField  , rightGbc);

        //Testo password
        JLabel pwdText = new JLabel("Password: ");
        pwdText.setForeground(new Color(23,65,95));
        pwdText.setFont(increaseMatriculationNumberText);
        pwdText.setForeground(Color.BLACK);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 3;
        rightRApage.add(pwdText, rightGbc);

        //Posizionamento campo password
        passwordField = new JPasswordField(15);
        passwordField.setBorder(new LineBorder(Color.BLACK, 1));
        passwordField.setEchoChar('•');
        rightGbc.gridx = 1;
        rightGbc.gridy = 4;
        rightRApage.add(passwordField, rightGbc);

        //Modo per cambiare il focus del cursore da emailField a passwordField
        Set<AWTKeyStroke> forwardKeys;
        forwardKeys = new HashSet<>(matriculationNumberField.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS));
        forwardKeys.remove(KeyStroke.getKeyStroke("TAB"));
        matriculationNumberField.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS, forwardKeys);

        matriculationNumberField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_TAB) {
                    passwordField.requestFocus();
                    evt.consume(); // Consuma l'evento Tab per evitare il comportamento predefinito
                }
            }
        });

        matriculationNumberField  .addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                matriculationNumberField  .setBorder(new LineBorder(new Color(246, 183, 55), 2));

            }

            @Override
            public void focusLost(FocusEvent e) {

                matriculationNumberField  .setBorder(new LineBorder(Color.BLACK));

            }

        });

        //Risalto colore dei bordi del campo password quando cliccato
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

            System.out.println("Errore caricamento immagine occhio ");

        }

        //Nascondere il layout del pulsante (occhio password)
        pwdEye.setContentAreaFilled(false);
        pwdEye.setBorderPainted(false);
        GridBagConstraints pwdEyeGbc = new GridBagConstraints();

        //Chiamata al metodo per mostrare/nascondere la password
        pwdEye.addActionListener(e -> viewPassword());

        pwdEyeGbc.gridx = 2;
        pwdEyeGbc.gridy = 4;
        pwdEyeGbc.anchor = GridBagConstraints.LINE_END;
        pwdEyeGbc.insets = new Insets(0, 0, 0, -10);
        rightRApage.add(pwdEye,pwdEyeGbc);

        rightGbc.gridx = 1;
        rightGbc.gridy = 5;
        JButton loginButton = new JButton("Accedi");
        loginButton.setBackground(new Color(23,65,95));
        loginButton.setForeground(Color.WHITE);
        rightRApage.add(loginButton, rightGbc);


        //Posizionamento password dimenticata
        LinkMouseOn forgottenPassword = new LinkMouseOn("Hai dimenticato la password?");
        rightGbc.gridx = 1;
        rightGbc.gridy = 6;
        rightRApage.add(forgottenPassword, rightGbc);

        //Azione se il testo password dimenticata viene cliccato
        forgottenPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Apri finestra d'inserimento email per recupero credenziali

                PasswordRecoveryManager passwordRecoveryManager = new PasswordRecoveryManager(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ManagerLoginPage.this);

                mainWindow.addCardPanel(passwordRecoveryManager, "passwordRecoveryPanel");

            }
        });

        //Cambio colore al passaggio del mouse
        forgottenPassword.ActiveLinkMouseOn(new Color(246, 183, 55), new Color(23,65,95));

        //Posizionamento registrazione nuovo utente
        LinkMouseOn managerReg = new LinkMouseOn("Sei un nuovo responsabile? Registrati");
        rightGbc.gridx = 1;
        rightGbc.gridy = 7;
        rightRApage.add(managerReg, rightGbc);

        //Indirizzamento pagina per la registrazione
        managerReg.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                SignInPageManager signInPageManager = new SignInPageManager(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ManagerLoginPage.this);

                mainWindow.addCardPanel(signInPageManager, "newRegResponsable");

            }
        });

        //Cambio colore al passaggio del mouse
        managerReg.ActiveLinkMouseOn(new Color(246, 183, 55), new Color(23,65,95));

        //Verifica credenziali quando il pulsante di accesso viene premuto
        loginButton.addActionListener(e -> {

            int msgError;

            msgError = credentialsVerification();
            showError(msgError);

        });

        //Impostazione Background left
        JPanel leftRApage = new JPanel();
        leftRApage.setBackground(Color.WHITE);

        IncreaseFont welcomeText = new IncreaseFont("Benvenuto!");
        Font welcomeFont = welcomeText.getFont();
        int fontSize = welcomeFont.getSize() + 20;
        Font increaseFont = welcomeFont.deriveFont((float)fontSize);
        welcomeText.setFont(increaseFont);
        welcomeText.setForeground(Color.black);

        IncreaseFont Text1 = new IncreaseFont("Inserisci matricola e password ");
        Text1.increaseFont(Text1, 8);

        IncreaseFont Text2 = new IncreaseFont("per accedere alla tua area personale");
        Text2.increaseFont(Text2,8);

        JButton backButton = new JButton("Torna Indietro");
        backButton.setBackground(new Color(35,171,144));
        backButton.setForeground(Color.white);

        JLabel logo = new JLabel(new NoScalingIcon(new ImageIcon("src/GUI/icon/LogoLabTRit.png")));

        leftRApage.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(5, 0, 0, 0);

        leftRApage.add(welcomeText,leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy = 1;
        leftGbc.insets = new Insets(5, 0, 0, 0);
        leftRApage.add(Text1, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy = 2;
        leftGbc.insets = new Insets(5, 0, 0, 0);
        leftRApage.add(Text2, leftGbc);

        leftGbc.gridx = 0;
        leftGbc.gridy = 3;
        leftRApage.add(backButton, leftGbc);
        backButton.setBackground(Color.RED);

        leftGbc.gridx = 0;
        leftGbc.gridy = 4;
        leftRApage.add(logo, leftGbc);

        //Cambio pagina quando il pulsante indietro viene premuto

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int choice = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire?", "Uscita Responsabile", JOptionPane.OK_CANCEL_OPTION);
                UIManager.put("choice.cancelButtonTex", "Non uscire");

                if (choice == JOptionPane.OK_OPTION) {

                    UserLoginPage userLoginPage = new UserLoginPage(myController);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ManagerLoginPage.this);
                    mainWindow.addCardPanel(userLoginPage, "login");

                }

            }
        });

        add(leftRApage);
        add(rightRApage);

    }
    //Visualizzazione messaggio di errore
    public void showError(int errorMassage) {

        if(errorMassage == 1) {

            JOptionPane.showMessageDialog(matriculationNumberField  , "I campi matricola e " +
                    "password non possono essere vuoti");

        }

        else if (errorMassage == 2){

            JOptionPane.showMessageDialog(matriculationNumberField  , "Credenziali errate" +
                    " riprova o registrati");

        }

        else if(errorMassage == 3) {

            ManagerHomePage managerHomePage = new ManagerHomePage(myController, currentManager);

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ManagerLoginPage.this);

            mainWindow.addCardPanel(managerHomePage, "homePage");

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

    public String getLoginMatricola() {
        return this.matriculationNumberField  .getText().trim();
    }

    public char[] getLoginPassword() {
        return this.passwordField.getPassword();
    }


    //Verifica delle credenziali quando il bottone viene premuto
    public int credentialsVerification() {


        final boolean[] managerFound = new boolean[1];


        if (this.matriculationNumberField  .getText().isEmpty() || this.passwordField.getPassword().length == 0) {

            return 1; //campi vuoti


        } else if (!this.matriculationNumberField  .getText().isEmpty() && this.passwordField.getPassword().length > 0) {

            currentManager = new Responsabile(getLoginMatricola(), null,null,null,null,null, getLoginPassword(),null);
            managerFound[0] = myController.managerVerificationC(currentManager);


            if (managerFound[0]) {

                return 3; //credenziali corrette

            }

            return 2; //credenziali errate

        }

        return 0;

    }

}
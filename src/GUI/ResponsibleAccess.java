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

public class ResponsibleAccess extends JPanel {

    private final JTextField campoMatricola;
    private final JPasswordField campoPassword;
    private static BufferedImage backgroundRight;
    Controller myController;

    private Responsabile responsabileCorrente;

    public ResponsibleAccess(Controller controller) {

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

        rightRApage.setBackground(Color.WHITE);
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
        JLabel matricolaText = new JLabel("Matricola: ");
        matricolaText.setForeground(new Color(23,65,95));
        Font fontMatricolaText = matricolaText.getFont();
        int sizeMatricolaText = fontMatricolaText.getSize() + 4;
        Font increaseMatricolaText = fontMatricolaText.deriveFont((float) sizeMatricolaText);
        matricolaText.setFont(increaseMatricolaText);
        matricolaText.setForeground(Color.BLACK);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 1;
        rightGbc.anchor = GridBagConstraints.LINE_START;
        rightRApage.add(matricolaText, rightGbc);

        //Campo matricola
        campoMatricola = new TextFieldBorderColor(15);

        //Risalto colore dei bordi del campo matricola quando cliccato
        campoMatricola.setBorder(new LineBorder(Color.BLACK, 1));
        TextFieldBorderColor.changeTextFieldBorderColor(campoMatricola);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 2;
        rightGbc.anchor = GridBagConstraints.LINE_START;
        rightRApage.add(campoMatricola, rightGbc);

        //Testo password
        JLabel pwdText = new JLabel("Password: ");
        pwdText.setForeground(new Color(23,65,95));
        pwdText.setFont(increaseMatricolaText);
        pwdText.setForeground(Color.BLACK);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 3;
        rightRApage.add(pwdText, rightGbc);

        //Posizionamento campo password
        campoPassword = new JPasswordField(15);
        campoPassword.setBorder(new LineBorder(Color.BLACK, 1));
        campoPassword.setEchoChar('•');
        rightGbc.gridx = 1;
        rightGbc.gridy = 4;
        rightRApage.add(campoPassword, rightGbc);

        campoMatricola.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                campoMatricola.setBorder(new LineBorder(new Color(246, 183, 55), 2));

            }

            @Override
            public void focusLost(FocusEvent e) {

                campoMatricola.setBorder(new LineBorder(Color.BLACK));

            }

        });

        //Risalto colore dei bordi del campo password quando cliccato
        campoPassword.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {

                campoPassword.setBorder(new LineBorder(new Color(246, 183, 55), 2));

            }

            @Override
            public void focusLost(FocusEvent e) {

                campoPassword.setBorder(new LineBorder(Color.BLACK));

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
        pwdEyeGbc.insets = new Insets(0, 0, 0, 35);
        rightRApage.add(pwdEye,pwdEyeGbc);

        rightGbc.gridx = 1;
        rightGbc.gridy = 5;
        BtnLayout loginButton = new BtnLayout("Accedi");
        loginButton.setBackground(new Color(23,65,95));
        rightRApage.add(loginButton, rightGbc);


        //Posizionamento password dimenticata
        LinkMouseOn passwordDimenticata = new LinkMouseOn("Hai dimenticato la password?");
        rightGbc.gridx = 1;
        rightGbc.gridy = 6;
        rightRApage.add(passwordDimenticata, rightGbc);

        //Azione se il testo password dimenticata viene cliccato
        passwordDimenticata.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Apri finestra d'inserimento email per recupero credenziali

                PasswordRecoveryR passwordRecoveryPanel = new PasswordRecoveryR(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);

                mainWindow.addCardPanel(passwordRecoveryPanel, "passwordRecoveryPanel");

            }
        });

        //Cambio colore al passaggio del mouse
        passwordDimenticata.ActiveLinkMouseOn(new Color(246, 183, 55), new Color(23,65,95));

        //Posizionamento registrazione nuovo utente
        LinkMouseOn regResponsabile = new LinkMouseOn("Sei un nuovo responsabile? Registrati");
        rightGbc.gridx = 1;
        rightGbc.gridy = 7;
        rightRApage.add(regResponsabile, rightGbc);

        //Indirizzamento pagina per la registrazione
        regResponsabile.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                NewRegResponsable newRegResponsable = new NewRegResponsable(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);

                mainWindow.addCardPanel(newRegResponsable, "newRegResponsable");

            }
        });

        //Cambio colore al passaggio del mouse
        regResponsabile.ActiveLinkMouseOn(new Color(246, 183, 55), new Color(23,65,95));

        //Verifica credenziali quando il pulsante di accesso viene premuto
        loginButton.addActionListener(e -> {

            int msgError;

            msgError = verificaCredenziali();
            mostraErrore(msgError);

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

        BtnLayout backButton = new BtnLayout("Torna Indietro");

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

                    PaginaLogin paginaLogin = new PaginaLogin(myController);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);
                    mainWindow.addCardPanel(paginaLogin, "login");

                }

            }
        });

        add(leftRApage);
        add(rightRApage);

    }
    //Visualizzazione messaggio di errore
    public void mostraErrore(int messaggioErrore) {

        if(messaggioErrore == 1) {

            JOptionPane.showMessageDialog(campoMatricola, "I campi matricola e " +
                    "password non possono essere vuoti");

        }

        else if (messaggioErrore == 2){

            JOptionPane.showMessageDialog(campoMatricola, "Credenziali errate" +
                    " riprova o registrati");

        }

        else if(messaggioErrore == 3) {

            HomePageR homePage = new HomePageR(myController, responsabileCorrente);

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);

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

    public String getLoginMatricola() {
        return this.campoMatricola.getText().trim();
    }

    public char[] getLoginPassword() {
        return this.campoPassword.getPassword();
    }


    //Verifica delle credenziali quando il bottone viene premuto
    public int verificaCredenziali() {


        final boolean[] responsabileTrovato = new boolean[1];


        if (this.campoMatricola.getText().isEmpty() || this.campoPassword.getPassword().length == 0) {

            return 1; //campi vuoti


        } else if (!this.campoMatricola.getText().isEmpty() && this.campoPassword.getPassword().length > 0) {

            responsabileCorrente = new Responsabile(getLoginMatricola(), null,null,null,null,null, getLoginPassword(),null);
            responsabileTrovato[0] = myController.verificaResponsabile(responsabileCorrente);


            if (responsabileTrovato[0]) {

                return 3; //credenziali corrette

            }

            return 2; //credenziali errate

        }

        return 0;

    }

}
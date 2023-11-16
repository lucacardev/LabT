package GUI;

import DTO.Responsabile;
import UTILITIES.Controller;
import UTILITIES.EmailSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class ResponsibleAccess extends JPanel {
    private JTextField campoMatricola;
    private JPasswordField campoPassword;
    private BtnLayout loginButton = new BtnLayout("Accesso");

    private final BtnLayout backButton = new BtnLayout("Indietro");

    private LinkMouseOn passwordDimenticata;

    private JPanel leftRApage = new JPanel();
    private JPanel rightRApage = new JPanel();
    private static BufferedImage backgroundImage;
    private static BufferedImage backgroundRight;

    private LinkMouseOn regResponsabile;

    Controller myController;

    private Responsabile responsabileCorrente;

    public ResponsibleAccess(Controller controller) {
        this.myController = controller;

        setLayout(new GridLayout(0,2));
        rightRApage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (backgroundRight != null) {
                    g.drawImage(backgroundRight, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            backgroundRight = ImageIO.read(new File("src/GUI/icon/Immagine78.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background responsabile");
            ex.printStackTrace();
        }


        rightRApage.setBackground(Color.white);
        rightRApage.setLayout(new GridBagLayout());
        GridBagConstraints rightGbc = new GridBagConstraints();
        rightGbc.insets = new Insets(5,5,5,5);


        ImageIcon loginImage = new ImageIcon("src/GUI/icon/icons8-rete-aziendale.gif");
        JLabel imageLabel = new JLabel(loginImage);

        // Posiziona l'immagine al centro del pannello
        rightGbc.gridx = 1;
        rightGbc.gridy = 0;
        rightGbc.gridwidth = 2; // Occupa due colonne orizzontali
        rightRApage.add(imageLabel, rightGbc);


        //Testo matricola
        JLabel matricolaText = new JLabel("Matricola: ");
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
        TextFieldBorderColor.changeTextFieldBorderColor(campoMatricola);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 2;
        rightGbc.anchor = GridBagConstraints.LINE_START;
        rightRApage.add(campoMatricola, rightGbc);

        //Testo password
        JLabel pwdText = new JLabel("Password: ");
        pwdText.setFont(increaseMatricolaText);
        pwdText.setForeground(Color.BLACK);

        rightGbc.gridx = 1;  // Colonna a destra
        rightGbc.gridy = 3;
        rightRApage.add(pwdText, rightGbc);

        //Posizionamento campo password
        campoPassword = new JPasswordField(15);
        campoPassword.setEchoChar('\u2022');
        rightGbc.gridx = 1;
        rightGbc.gridy = 4;
        rightRApage.add(campoPassword, rightGbc);

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
        pwdEyeGbc.gridy = 4;
        pwdEyeGbc.anchor = GridBagConstraints.LINE_END;  // Allinea il pulsante alla fine della colonna
        rightRApage.add(pwdEye,pwdEyeGbc);

        rightGbc.gridx = 1;
        rightGbc.gridy = 5;
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

                PasswordRecovery passwordRecoveryPanel = new PasswordRecovery(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);

                mainWindow.addCardPanel(passwordRecoveryPanel, "passwordRecoveryPanel");
            }
        });

        //Cambio colore al passaggio del mouse
        passwordDimenticata.ActiveLinkMouseOn(passwordDimenticata);


        //Posizionamento registrazione nuovo utente
        regResponsabile = new LinkMouseOn("Sei un nuovo responsabile? Registrati");
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
        regResponsabile.ActiveLinkMouseOn(regResponsabile);

        //Verifica credenziali quando il pulsante di accesso viene premuto
        loginButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                int msgError;

                msgError = verificaCredenziali();
                mostraErrore(msgError);

            }
        });


    //Impostazione Background left

        leftRApage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        //Impostazione sfondo background di sinistra

        try {
            backgroundImage = ImageIO.read(new File("src/GUI/icon/backgroundRa.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background BackgroundRa");
            ex.printStackTrace();
        }


        int sizeFont = 17;
        int style = 10;
        String font = "Arial";

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

        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(5, 0, 0, 0);

        leftRApage.add(welcomeText,leftGbc);

        leftGbc.gridy = 1;
        leftGbc.insets = new Insets(5, 0, 0, 0);
        leftRApage.add(Text1, leftGbc);

        leftGbc.gridy = 2;
        leftGbc.insets = new Insets(5, 0, 0, 0);
        leftRApage.add(Text2, leftGbc);

        leftGbc.gridy =3;
        leftRApage.add(backButton, leftGbc);

        //Cambio pagina quando il pulsante indietro viene premuto

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int choice = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire?", "Uscita Account", JOptionPane.OK_CANCEL_OPTION);
                UIManager.put("choice.cancelButtonTex", "Non uscire");
                if (choice == JOptionPane.OK_OPTION) {

                    PaginaLogin paginaLogin = new PaginaLogin(myController);
                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);
                    mainWindow.addCardPanel(paginaLogin, "login");
                }
            }
        });


        //Immagine logo
        ImageIcon footImage = new ImageIcon("src/GUI/icon/icons8-organizzazione.gif");
        JLabel imagefLabel = new JLabel(footImage);

       //add(imagefLabel,BorderLayout.EAST);

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
        else if(messaggioErrore == 3){

            HomePageR homePage = new HomePageR(myController, responsabileCorrente);

            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ResponsibleAccess.this);

            mainWindow.addCardPanel(homePage, "homePage");

        }

    }

    //Metodo per visualizzare e nascondere la password
    private void viewPassword() {
        if (campoPassword.getEchoChar() == '\u2022') {
            campoPassword.setEchoChar((char) 0);
        } else {
            campoPassword.setEchoChar('\u2022');
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

            return 1;


        } else if (!this.campoMatricola.getText().isEmpty() && this.campoPassword.getPassword().length > 0) {


            responsabileCorrente = new Responsabile(getLoginMatricola(), null, getLoginPassword());

            String matricolaRecuperata = myController.MatricolaRecovery(responsabileCorrente);

            responsabileCorrente = new Responsabile(matricolaRecuperata, getLoginMatricola(), getLoginPassword());


            responsabileTrovato[0] = myController.verificaResponsabile(responsabileCorrente);


            if (responsabileTrovato[0]) {

                return 3;
            }
            return 2;
        }
        return 0;
    }
}


package GUI;

import UTILITIES.Controller;
import UTILITIES.EmailSender;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.UUID;

public class PasswordRecovery extends JPanel{
    private final JPanel rightPasswordRecovery;
    private final JPanel newPasswordPanel;
    private final TextFieldBorderColor emailRecovery;
    private BufferedImage leftRecoveryBackground;
    private BufferedImage rightRecoveryBackground;
    private BufferedImage newPasswordBackground;
    private final JPasswordField newPassword;
    private final JPasswordField repeatNewPassword;
    private String userEmail;
    Controller myController;

    public PasswordRecovery(Controller controller) {

        myController = controller;

        rightPasswordRecovery = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo con interpolazione bilineare
                if (rightRecoveryBackground != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(rightRecoveryBackground, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            rightRecoveryBackground = ImageIO.read(new File("src/GUI/icon/background.png"));

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background password utente destra");
            ex.printStackTrace();
        }

        //Metodo per impostare l'immagine di background
        // Disegna l'immagine di sfondo
        JPanel leftPasswordRecovery = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;


                // Disegna l'immagine di sfondo
                if (leftRecoveryBackground != null) {
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(leftRecoveryBackground, 0, 0, getWidth(), getHeight(), this);
                }

            }

        };

        ///////////////////PAGINA DIVISA IN DUE PARTI UGUALI//////////////

        setLayout(new GridLayout(0,2));

        rightPasswordRecovery.setLayout(new GridBagLayout());
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.insets = new Insets(7,7,7,7);

        /////////////////////LATO DESTRO/////////////////////////


        //Inserimento testo per recupero credenziali

        IncreaseFont textMailRecovery = new IncreaseFont("Inserisci l'email per recuperare la password:");
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.gridwidth = 2; //occupa due colonne
        gbcRight.weighty = 0;
        gbcRight.weightx = 0;
        textMailRecovery.increaseFont(textMailRecovery, 1);
        gbcRight.anchor = GridBagConstraints.CENTER;

        rightPasswordRecovery.add(textMailRecovery, gbcRight);

        //Inserimento campo email per recupero credenziali

        emailRecovery = new TextFieldBorderColor(15);
        gbcRight.gridx = 0;
        gbcRight.gridy = 1;
        gbcRight.gridwidth = 2;
        gbcRight.weighty = 0;
        gbcRight.weightx = 0;
        gbcRight.anchor = GridBagConstraints.CENTER;

        TextFieldBorderColor.changeTextFieldBorderColor(emailRecovery);
        rightPasswordRecovery.add(emailRecovery, gbcRight);

        //Inserimento bottone d'invio codice
        BtnLayout sendButtonCode = new BtnLayout("Recupera");
        gbcRight.gridx = 1;
        gbcRight.gridy = 3;
        gbcRight.gridwidth = 1;
        gbcRight.weighty = 0;
        gbcRight.weightx = 0;
        gbcRight.anchor = GridBagConstraints.FIRST_LINE_END;
        rightPasswordRecovery.add(sendButtonCode, gbcRight);

        /*Creazione Pannello per impostare la nuova Password (visibile solo dopo aver inserito il codice corretto) */

        newPasswordPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo con interpolazione bilineare
                if (newPasswordBackground != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(newPasswordBackground, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            newPasswordBackground = ImageIO.read(new File("src/GUI/icon/background.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine recupera password utente");
            ex.printStackTrace();

        }

        newPasswordPanel.setLayout(new GridBagLayout());
        GridBagConstraints NWPgbc = new GridBagConstraints();
        NWPgbc.insets = new Insets(5,5,5,5);

        //Inserimento testo nuova password
        JLabel textNewPassword = new JLabel("Nuova password: ");

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 0;
        newPasswordPanel.add(textNewPassword, NWPgbc);

        //Inserimento campo per nuova password
        newPassword = new JPasswordField(15);
        TextFieldBorderColor.changeTextFieldBorderColor(newPassword);

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 1;
        newPasswordPanel.add(newPassword, NWPgbc);

        //Posizionamento occhio per visualizzare password
        JButton pwdEyeN = new JButton();
        try {
            NoScalingIcon noScalingEye = new NoScalingIcon(new ImageIcon("src/GUI/icon/hide.png"));
            pwdEyeN.setIcon(noScalingEye);

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine occhio - pagina RECUPEROPASSWORD ");
        }

        //Nascondere il layout del pulsante (occhio password)
        pwdEyeN.setContentAreaFilled(false);
        pwdEyeN.setBorderPainted(false);
        GridBagConstraints pwdEyeGbcN = new GridBagConstraints();

        //Chiamata al metodo per mostrare/nascondere la password
        pwdEyeN.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPasswordNew();
            }
        });

        pwdEyeGbcN.gridx = 1;
        pwdEyeGbcN.gridy = 1;
        pwdEyeGbcN.anchor = GridBagConstraints.LINE_END;
        pwdEyeGbcN.insets = new Insets(0,0,0,-20);
        newPasswordPanel.add(pwdEyeN,pwdEyeGbcN);

        //Inserimento testo ripetizione nuova password
        JLabel repeatTextNewPassword = new JLabel("Ripeti la nuova password: ");

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 2;
        newPasswordPanel.add(repeatTextNewPassword, NWPgbc);

        //Inserimento campo per ripetizione nuova password
        repeatNewPassword = new JPasswordField(15);
        TextFieldBorderColor.changeTextFieldBorderColor(repeatNewPassword);

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 3;
        newPasswordPanel.add(repeatNewPassword, NWPgbc);

        //Posizionamento occhio per visualizzare password
        JButton pwdEyeR = new JButton();
        try {
            NoScalingIcon noScalingEye = new NoScalingIcon(new ImageIcon("src/GUI/icon/hide.png"));
            pwdEyeR.setIcon(noScalingEye);

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine occhio - pagina RECUPEROPASSWORD ");
        }

        //Nascondere il layout del pulsante (occhio password)
        pwdEyeR.setContentAreaFilled(false);
        pwdEyeR.setBorderPainted(false);
        GridBagConstraints pwdEyeGbcR = new GridBagConstraints();

        //Chiamata al metodo per mostrare/nascondere la password
        pwdEyeR.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                viewPasswordRepeat();
            }
        });

        pwdEyeGbcR.gridx = 1;
        pwdEyeGbcR.gridy = 3;
        pwdEyeGbcR.anchor = GridBagConstraints.LINE_END;
        pwdEyeGbcR.insets = new Insets(0,0,0,-20);
        newPasswordPanel.add(pwdEyeR,pwdEyeGbcR);

        //Bottone conferma nuova password
        BtnLayout confirmPassword = new BtnLayout("Invia");

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 4;
        newPasswordPanel.add(confirmPassword, NWPgbc);

        //Bottone conferma nuova password quando viene premuto

        confirmPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getNewPassword().isEmpty() || getRepeatNewPassword().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "I campi non possono essere vuoti.", "Errore", JOptionPane.ERROR_MESSAGE);
                } else {

                    //Se le password coincidono
                    if (passwordComparison()) {

                        //Errore nel caso si voglia inserire una nuova password già usata in passato
                        if (myController.userPasswordRecoveryC(userEmail).equals(getNewPassword())) {

                            JOptionPane.showMessageDialog(null, "Non puoi inserire una password che già usavi in passato");

                        } else {

                            if (myController.userPasswordUpdateC(userEmail, getNewPassword())) {
                                JOptionPane.showMessageDialog(null, "Password aggiornata correttamente!");

                                PaginaLogin loginPage = new PaginaLogin(myController);

                                //Ritorna alla pagina di login dopo aver confermato la nuova password

                                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(PasswordRecovery.this);

                                mainWindow.addCardPanel(loginPage, "login");


                            }

                        }

                    } else {

                        JOptionPane.showMessageDialog(null, "Le password inserite non corrispondono");

                    }
                }

            }
        });

        //Bottone recupera quando viene premuto

        sendButtonCode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                boolean mailFound;
                String verifiedCode;
                String randomCode2;

                userEmail = getEmailRecovery();


                mailFound = myController.userMailVerifyC(getEmailRecovery());


                if(mailFound) {

                    //Metodo generazione codice casuale

                    randomCode2 = generateRandomCode();

                    //Metodo per invio mail

                    EmailSender.sendVerificationCode(getEmailRecovery(), randomCode2, "Codice di verifica", "Il tuo codice di verifica è: ");

                    //Finestra pop-up per inserire codice inviato tramite mail

                    verifiedCode = JOptionPane.showInputDialog(null, "Ti abbiamo inviato una mail con un codice, inseriscilo qui sotto: ",
                            "Inserisci Codice", JOptionPane.PLAIN_MESSAGE);

                    if(verifiedCode != null && verifiedCode.equals(randomCode2)) {

                        //Rendo invisibile gli elementi del pannello di destra per aggiungere i nuovi
                        rightPasswordRecovery.setVisible(false);
                        remove(rightPasswordRecovery);
                        add(newPasswordPanel);

                    } else if (verifiedCode != null) {

                        JOptionPane.showMessageDialog(null, "Il codice inserito non è corretto, richiedi un nuovo codice");

                    }

                } else {

                    JOptionPane.showMessageDialog(emailRecovery, "La mail inserita non corrisponde a nessun utente registrato");

                }
            }
        });



        //Inserimento bottone per tornare indietro
        BtnLayout backButton = new BtnLayout("Indietro");
        gbcRight.gridx = 0;
        gbcRight.gridy = 3;
        gbcRight.gridwidth = 1;
        gbcRight.weighty = 0;
        gbcRight.weightx = 0;
        gbcRight.anchor = GridBagConstraints.FIRST_LINE_START;

        rightPasswordRecovery.add(backButton, gbcRight);

        //Bottone "indietro" quando viene cliccato

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                PaginaLogin loginPage = new PaginaLogin(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(PasswordRecovery.this);

                mainWindow.addCardPanel(loginPage, "login");

            }
        });

        ////////////////////////LATO SINISTRO//////////////////////////


        //Impostazione background
        try {

            leftRecoveryBackground = ImageIO.read(new File("src/GUI/icon/mailbackground.png"));

        } catch (Exception ex) {

            System.out.println("Errore caricamento immagine background recovery mail page");
            ex.printStackTrace();

        }

        add(leftPasswordRecovery);
        add(rightPasswordRecovery);
        setVisible(true);

    }

    //Metodo per recuperare la mail per il recupero delle credenziali
    public String getEmailRecovery() {

        return emailRecovery.getText().trim();

    }

    //Metodo per recuperare la nuova password inserita
    public String getNewPassword() {

        char[] passwordChars = newPassword.getPassword();
        return new String(passwordChars).trim();

    }

    //Metodo per recuperare la nuova password inserita
    public String getRepeatNewPassword() {

        char[] passwordChars = repeatNewPassword.getPassword();
        return new String(passwordChars).trim();

    }

    //Metodo per verificare che le due nuove password inserite siano uguali
    public boolean passwordComparison() {

        return getNewPassword().equals(getRepeatNewPassword());

    }

    private void viewPasswordNew() {
        if (newPassword.getEchoChar() == '•') {
            newPassword.setEchoChar((char) 0);
        } else {
            newPassword.setEchoChar('•');
        }
    }

    private void viewPasswordRepeat() {
        if (repeatNewPassword.getEchoChar() == '•') {
            repeatNewPassword.setEchoChar((char) 0);
        } else {
            repeatNewPassword.setEchoChar('•');
        }
    }

    //Metodo per generare un codice di verifica casuale
    public static String generateRandomCode() {

        //String randomCode = null;

        try {

            UUID uuid = UUID.randomUUID();

            return uuid.toString().replaceAll("-", "").substring(0, 6);

        } catch (Exception ex) {

            System.out.println("Errore nella generazione del codice");

        }

        return null;

    }

}

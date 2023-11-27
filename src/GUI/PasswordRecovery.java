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
    private final JPanel leftPasswordRecovery;

    private final JPanel newPasswordPanel;
    private final IncreaseFont emailTextRecovery;
    private final TextFieldBorderColor emailRecovery;
    private final BtnLayout sendButtonCode;
    private final BtnLayout backButton;
    private BufferedImage leftRecoveryBackground;

    private EmailSender sendEmailVerification;
    private final TextFieldBorderColor newPassword;
    private final TextFieldBorderColor repeatNewPassword;

    private final BtnLayout confirmPassword;

    private String emailUtente;

    Controller myController;

    public PasswordRecovery(Controller controller) {

        myController = controller;

        rightPasswordRecovery = new JPanel();

        /*Utilizzo della sottoclasse di JPanel (classe anonima) per evitare di creare una nuova classe
        * e quindi personalizziamo solo il Pannello sinistro per impostare il background altrimenti non avremmo
        * potuto impostarlo.*/

        leftPasswordRecovery = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (leftRecoveryBackground != null) {
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

        emailTextRecovery = new IncreaseFont("Inserisci l'email per recuperare la password:");
        gbcRight.gridx = 0;
        gbcRight.gridy = 0;
        gbcRight.gridwidth = 2; //occupa due colonne
        gbcRight.weighty = 0;
        gbcRight.weightx = 0;
        emailTextRecovery.increaseFont(emailTextRecovery, 1);
        gbcRight.anchor = GridBagConstraints.CENTER;

        rightPasswordRecovery.add(emailTextRecovery, gbcRight);

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
        sendButtonCode = new BtnLayout("Recupera");
        gbcRight.gridx = 1;
        gbcRight.gridy = 3;
        gbcRight.gridwidth = 1;
        gbcRight.weighty = 0;
        gbcRight.weightx = 0;
        gbcRight.anchor = GridBagConstraints.FIRST_LINE_END;
        rightPasswordRecovery.add(sendButtonCode, gbcRight);

        /*Creazione Pannello per impostare la nuova Password (visibile solo dopo aver inserito il codice corretto) */

        newPasswordPanel = new JPanel();

        newPasswordPanel.setLayout(new GridBagLayout());
        GridBagConstraints NWPgbc = new GridBagConstraints();
        NWPgbc.insets = new Insets(5,5,5,5);

        //Inserimento testo nuova password
        JLabel textNewPassword = new JLabel("Nuova password: ");

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 0;
        newPasswordPanel.add(textNewPassword, NWPgbc);

        //Inserimento campo per nuova password
        newPassword = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(newPassword);

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 1;
        newPasswordPanel.add(newPassword, NWPgbc);

        //Inserimento testo ripetizione nuova password
        JLabel repeatTextNewPassword = new JLabel("Ripeti la nuova password: ");

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 2;
        newPasswordPanel.add(repeatTextNewPassword, NWPgbc);

        //Inserimento campo per ripetizione nuova password
        repeatNewPassword = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(repeatNewPassword);

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 3;
        newPasswordPanel.add(repeatNewPassword, NWPgbc);

        //Bottone conferma nuova password
        confirmPassword = new BtnLayout("Invia");

        NWPgbc.gridx = 0;
        NWPgbc.gridy = 4;
        newPasswordPanel.add(confirmPassword, NWPgbc);

        //Bottone conferma nuova password quando viene premuto

        confirmPassword.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                //Se le password coincidono
                if(passwordComparison()) {

                    //Errore nel caso si voglia inserire una nuova password già usata in passato
                    if(myController.recuperoPasswordUtenteC(emailUtente).equals(getNewPassword())) {

                        JOptionPane.showMessageDialog(null, "Non puoi inserire una password che già usavi in passato");

                    } else {

                        if(myController.aggiornaPasswordUtenteC(emailUtente, getNewPassword())) {
                            JOptionPane.showMessageDialog(null, "Password aggiornata correttamente!");

                            PaginaLogin paginaLogin = new PaginaLogin(myController);

                            //Ritorna alla pagina di login dopo aver confermato la nuova password

                            MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(PasswordRecovery.this);

                            mainWindow.addCardPanel(paginaLogin, "login");


                        }

                    }

                } else {
                    JOptionPane.showMessageDialog(null, "Le password inserite non corrispondono");
                }

            }
        });


        //Bottone recupera quando viene premuto

        sendButtonCode.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                boolean mailPresente;
                String verifiedCode;
                String randomCode2;

                emailUtente = getEmailRecovery();


                mailPresente = myController.verificaMailUtente(getEmailRecovery());


                if(mailPresente) {

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
        backButton = new BtnLayout("Indietro");
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

                PaginaLogin paginaLogin = new PaginaLogin(myController);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(PasswordRecovery.this);

                mainWindow.addCardPanel(paginaLogin, "login");

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

        return newPassword.getText().trim();

    }

    //Metodo per recuperare la nuova password inserita
    public String getRepeatNewPassword() {

        return repeatNewPassword.getText().trim();

    }

    //Metodo per verificare che le due nuove password inserite siano uguali
    public boolean passwordComparison() {

        boolean equal = false;

        if(getNewPassword().equals(getRepeatNewPassword())) {

            equal = true;
        }

        return equal;
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

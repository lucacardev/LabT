package GUI;

import DTO.Responsabile;
import DTO.Utente;
import UTILITIES.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HomePage extends JPanel{
    private static BufferedImage leftHomePageBackground;
    Controller myController;
    Utente utenteLoggato;

    public HomePage(Controller controller, Utente utenteCorrente) {

        myController = controller;
        utenteLoggato = utenteCorrente;

        setLayout(new GridLayout(0,2));

        JPanel leftHomePage;
        JPanel rightHomePage = new JPanel();

        /////////////////////////////LEFT HOME PAGE//////////////////////////////////

        leftHomePage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                super.paintComponent(g);

                // Disegna l'immagine di sfondo
                if (leftHomePageBackground != null) {
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g.drawImage(leftHomePageBackground, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Layout della parte di sinistra
        leftHomePage.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5,5,5,5);

        //Testo di benvenuto
        IncreaseFont welcomeText = new IncreaseFont("Benvenuto "+ utenteCorrente.getUsername());
        welcomeText.increaseFont(welcomeText, 15);

        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(20,0,0,0);

        leftHomePage.add(welcomeText, leftGbc);

        //Email dell'utente registrato
        IncreaseFont userEmail = new IncreaseFont(utenteCorrente.getEmail());
        userEmail.increaseFont(userEmail,7);
        leftGbc.gridx = 0;
        leftGbc.gridy = 1;

        leftHomePage.add(userEmail, leftGbc);

        //Pulsante indietro
        BtnLayout backButton = new BtnLayout("Esci");
        leftGbc.gridx = 0;
        leftGbc.gridy = 3;
        leftGbc.weightx = 0.5;
        leftGbc.weighty = 0.5;
        backButton.setBackground(Color.RED);
        leftGbc.insets = new Insets(15,20,40,0);
        leftGbc.anchor = GridBagConstraints.NORTH;
        leftHomePage.add(backButton, leftGbc);

        //Cambio pagina quando il pulsante indietro viene premuto

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                int choice = JOptionPane.showConfirmDialog(null, "Sei sicuro di voler uscire?", "Uscita Account", JOptionPane.OK_CANCEL_OPTION);

                UIManager.put("choice.cancelButtonTex", "Non uscire");

                if (choice == JOptionPane.OK_OPTION) {

                    PaginaLogin paginaLogin = new PaginaLogin(myController);

                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(HomePage.this);

                    mainWindow.addCardPanel(paginaLogin, "login");


                }
            }


        });

        ////////////////////////////////RIGHT PAGE//////////////////////////////

        rightHomePage.setLayout(new GridBagLayout());

        GridBagConstraints rightGbc = new GridBagConstraints();

        rightGbc.insets = new Insets(10,10,10,10);

        int sizeFont = 17;
        int style = 10;
        String font = "Arial";

        //Bottone nuova prenotazione
        BtnLayout newBooking = new BtnLayout("Nuova prenotazione");
        newBooking.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(newBooking, rightGbc);

        //Cambio pagina quando nuova prenotazione viene premuto
        newBooking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                NewBooking newBooking  = new NewBooking(myController, utenteLoggato);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(HomePage.this);

                mainWindow.addCardPanel(newBooking, "newBooking");

            }
        });

        //Bottone mie prenotazioni
        BtnLayout myBooking = new BtnLayout("Le mie prenotazioni");
        myBooking.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(myBooking, rightGbc);

        myBooking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(HomePage.this);

                if(mainWindow.containsCard("myBookingPage")) {

                    mainWindow.showCard("myBookingPage");

                } else {

                    MyBooking myBookingPage = new MyBooking(myController, utenteLoggato);
                    mainWindow.addCardPanel(myBookingPage, "myBookingPage");
                }
            }
        });

        //Disegno Background
        try {
            // Carica l'immagine di sfondo dal file specificato
            leftHomePageBackground = ImageIO.read(new File("src/GUI/icon/19366.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(leftHomePage);
        add(rightHomePage);

    }
}
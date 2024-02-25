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

public class UserHomePage extends JPanel{
    private static BufferedImage leftHomePageBackground;
    Controller myController;
    private final Utente loggedUser;

    public UserHomePage(Controller controller, Utente currentUser) {

        myController = controller;
        loggedUser = currentUser;

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
        IncreaseFont welcomeText = new IncreaseFont("Benvenuto "+ currentUser.getUsername());
        welcomeText.increaseFont(welcomeText, 15);

        leftGbc.gridx = 0;
        leftGbc.gridy = 0;
        leftGbc.insets = new Insets(20,0,0,0);

        leftHomePage.add(welcomeText, leftGbc);

        //Email dell'utente registrato
        IncreaseFont userEmail = new IncreaseFont(currentUser.getEmail());
        userEmail.increaseFont(userEmail,7);
        leftGbc.gridx = 0;
        leftGbc.gridy = 1;

        leftHomePage.add(userEmail, leftGbc);

        //Pulsante indietro
        JButton backButton = new JButton("Esci");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        leftGbc.gridx = 0;
        leftGbc.gridy = 3;
        leftGbc.weightx = 0.5;
        leftGbc.weighty = 0.5;
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

                    UserLoginPage userLoginPage = new UserLoginPage(myController);

                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserHomePage.this);

                    mainWindow.addCardPanel(userLoginPage, "login");


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
        JButton newBooking = new JButton("Nuova prenotazione");
        newBooking.setBackground(new Color(35,171,144));
        newBooking.setForeground(Color.WHITE);
        newBooking.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(newBooking, rightGbc);

        //Cambio pagina quando nuova prenotazione viene premuto
        newBooking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                NewBooking newBooking  = new NewBooking(myController, loggedUser);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserHomePage.this);

                mainWindow.addCardPanel(newBooking, "newBooking");

            }
        });

        //Bottone mie prenotazioni
        JButton myBooking = new JButton("Le mie prenotazioni");
        myBooking.setBackground(new Color(35,171,144));
        myBooking.setForeground(Color.white);
        myBooking.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(myBooking, rightGbc);

        myBooking.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(UserHomePage.this);

                if(mainWindow.containsCard("myBookingPage")) {

                    mainWindow.showCard("myBookingPage");

                } else {

                    MyBooking myBookingPage = new MyBooking(myController, loggedUser);
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
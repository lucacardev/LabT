package GUI;

import DAO.ResponsabileDAO;
import DTO.Responsabile;
import DTO.Utente;
import UTILITIES.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class HomePageR extends JPanel {
    private static JPanel leftHomePage;
    private static JPanel rightHomePage;

    private BufferedImage leftHomePageBackground;

    Controller myController;
    Responsabile ResponsabileLoggato;



    public HomePageR(Controller controller, Responsabile responsabileCorrente) {

        myController = controller;
        ResponsabileLoggato = responsabileCorrente;

        setLayout(new GridLayout(0,2));

        leftHomePage = new JPanel();
        rightHomePage = new JPanel();


        /////////////////////////////LEFT HOME PAGE//////////////////////////////////

        leftHomePage = new JPanel() {
            @Override
            //Metodo per impostare l'immagine di background
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo
                if (leftHomePageBackground != null) {
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(leftHomePageBackground, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        //Disegno Background
        try {
            // Carica l'immagine di sfondo dal file specificato
            leftHomePageBackground = ImageIO.read(new File("src/GUI/icon/Immagine WhatsApp 2023-12-04 ore 10.17.59_b126edae.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        leftHomePage.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5,5,5,5);


        String[] informazioni = ResponsabileDAO.nomecognomeRecovery(responsabileCorrente.getMatricola(),responsabileCorrente.getPw());

        if (informazioni[0] != null && informazioni[1] != null) {
            String nome = informazioni[0];
            String cognome = informazioni[1];

            // Testo di benvenuto con nome e cognome
            IncreaseFont welcomeText = new IncreaseFont("Benvenuto/a \n");
            IncreaseFont nc = new IncreaseFont(nome + " " + cognome);
            Font welcomeFont = welcomeText.getFont();
            int fontSize = welcomeFont.getSize() + 20;
            Font increaseFont = welcomeFont.deriveFont((float)fontSize);
            welcomeText.setFont(increaseFont);
            welcomeText.setForeground(new Color(0, 0, 0));
            nc.setFont(increaseFont);
            nc.setForeground(new Color(0,0,0));
            leftGbc.gridx = 0;
            leftGbc.gridy = 0;
            leftGbc.insets = new Insets(20,0,0,0);
            leftHomePage.add(welcomeText, leftGbc);
            GridBagConstraints ncGbc = new GridBagConstraints();
            ncGbc.gridx = 0;
            ncGbc.gridy = 1;
            leftHomePage.add(nc,ncGbc);
        }

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

                    ResponsibleAccess paginaAccessR = new ResponsibleAccess(myController);

                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(HomePageR.this);

                    mainWindow.addCardPanel(paginaAccessR, "AccessoR");


                }
            }


        });

        ////////////////////////////////RIGHT PAGE//////////////////////////////

        rightHomePage.setLayout(new GridBagLayout());
        rightHomePage.setBackground(new Color(239,239,239));

        GridBagConstraints rightGbc = new GridBagConstraints();

        rightGbc.insets = new Insets(10,10,10,10);

        int sizeFont = 17;
        int style = 10;
        String font = "Arial";

        //Bottone I miei Team
        BtnLayout myTeams = new BtnLayout("I miei Teams");
        myTeams.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(myTeams, rightGbc);

        myTeams.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myteampage = new MyTeam(myController, ResponsabileLoggato);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(HomePageR.this);

                mainWindow.addCardPanel(myteampage, "myTeamPage");

            }
        });

        add(leftHomePage);
        add(rightHomePage);

    }
}
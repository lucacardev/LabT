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

public class ManagerHomePage extends JPanel {
    private static BufferedImage leftHomePageBackground;
    private static BufferedImage RightBackground;
    Controller myController;
    Responsabile loggedInManager;

    public ManagerHomePage(Controller controller, Responsabile currentManager) {

        myController = controller;
        loggedInManager = currentManager;

        setLayout(new GridLayout(0,2));

        JPanel leftHomePage;
        JPanel rightHomePage = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;

                // Disegna l'immagine di sfondo con interpolazione bilineare
                if (RightBackground != null) {

                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                    g2d.drawImage(RightBackground, 0, 0, getWidth(), getHeight(), this);

                }

            }
        };

        //Impostazione sfondo background di destra

        try {
            RightBackground = ImageIO.read(new File("src/GUI/icon/sfondoR.png"));


        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine homepage responsabile");
            ex.printStackTrace();

        }

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
            leftHomePageBackground = ImageIO.read(new File("src/GUI/icon/Immagine WhatsApp 2023-12-04 ore 10.17.59_b126edae.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        leftHomePage.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5,5,5,5);

        String[] informations = ResponsabileDAO.nameSurnameRecovery(currentManager.getMatricola(),currentManager.getPw());

        if (informations[0] != null && informations[1] != null) {
            String name = informations[0];
            String surname = informations[1];

            // Testo di benvenuto con name e surname
            IncreaseFont welcomeText = new IncreaseFont("Benvenuto/a \n");
            IncreaseFont nc = new IncreaseFont(name + " " + surname);
            Font welcomeFont = welcomeText.getFont();
            int fontSize = welcomeFont.getSize() + 20;
            Font increaseFont = welcomeFont.deriveFont((float)fontSize);
            welcomeText.setFont(increaseFont);
            welcomeText.setForeground(new Color(23, 65, 95));
            nc.setFont(increaseFont);
            nc.setForeground(new Color(0,0,0));

            //Aggiunta del testo di benvenuto
            leftGbc.gridx = 0;
            leftGbc.gridy = 0;
            leftGbc.insets = new Insets(20,0,0,0);
            leftHomePage.add(welcomeText, leftGbc);

            //Aggiunta del name e surname del responsabile
            GridBagConstraints ncGbc = new GridBagConstraints();
            ncGbc.gridx = 0;
            ncGbc.gridy = 1;
            leftHomePage.add(nc,ncGbc);
        }

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

                    ManagerLoginPage managerLoginPage = new ManagerLoginPage(myController);

                    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ManagerHomePage.this);

                    mainWindow.addCardPanel(managerLoginPage, "AccessoR");

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

        //Bottone I miei Team
        JButton myTeams = new JButton("I miei Teams");
        myTeams.setBackground(new Color(23,65,95));
        myTeams.setForeground(Color.WHITE);
        myTeams.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 1;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(myTeams, rightGbc);

        myTeams.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myteampage = new MyTeam(myController, loggedInManager);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(ManagerHomePage.this);

                mainWindow.addCardPanel(myteampage, "myTeamPage");

            }
        });

        add(leftHomePage);
        add(rightHomePage);

    }
}
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

                // Disegna l'immagine di sfondo
                if (leftHomePageBackground != null) {
                    g.drawImage(leftHomePageBackground, 0, 0, getWidth(), getHeight(), this);
                }

            }
        };

        leftHomePage.setLayout(new GridBagLayout());
        GridBagConstraints leftGbc = new GridBagConstraints();
        leftGbc.insets = new Insets(5,5,5,5);


        String[] informazioni = ResponsabileDAO.nomecognomeRecovery(responsabileCorrente.getMatricola(),responsabileCorrente.getPw());

        if (informazioni[0] != null && informazioni[1] != null) {
            String nome = informazioni[0];
            String cognome = informazioni[1];

            // Testo di benvenuto con nome e cognome
            IncreaseFont welcomeText = new IncreaseFont("Benvenuto/a \n"); //+ nome + " " + cognome);
            IncreaseFont nc = new IncreaseFont(nome + " " + cognome);
            Font welcomeFont = welcomeText.getFont();
            int fontSize = welcomeFont.getSize() + 25;
            Font increaseFont = welcomeFont.deriveFont((float)fontSize);
            welcomeText.setFont(increaseFont);
            welcomeText.setForeground(new Color(0,0,205));
            nc.setFont(increaseFont);
            nc.setForeground(new Color(0,0,205));
            leftGbc.gridx = 0;
            leftGbc.gridy = 0;
            leftGbc.insets = new Insets(20,0,0,0);
            leftHomePage.add(welcomeText, leftGbc);
            GridBagConstraints ncGbc = new GridBagConstraints();
            ncGbc.gridx = 0;
            ncGbc.gridy = 1;
            leftHomePage.add(nc,ncGbc);
        }

        /*JScrollPane scrollableLeftHomePage = new JScrollPane(leftHomePage);

        // Imposta la politica della barra di scorrimento
        scrollableLeftHomePage.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        scrollableLeftHomePage.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        // Aggiungi lo JScrollPane alla tua UI al posto di leftHomePage
        add(scrollableLeftHomePage);*/



        /*// matricola del responsabile registrato

        IncreaseFont responsabileMatricola = new IncreaseFont(responsabileCorrente.getMatricola());
        responsabileMatricola.increaseFont(responsabileMatricola,7);
        leftGbc.gridx = 0;
        leftGbc.gridy = 1;

        leftHomePage.add(responsabileMatricola, leftGbc);*/

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
        rightHomePage.setBackground(new Color(171,205,239));

        GridBagConstraints rightGbc = new GridBagConstraints();

        rightGbc.insets = new Insets(10,10,10,10);

        int sizeFont = 17;
        int style = 10;
        String font = "Arial";

        //Bottone nuovo Team
        BtnLayout newTeam = new BtnLayout("Le mie informazioni");
        newTeam.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 0;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(newTeam, rightGbc);

        //Cambio pagina quando nuova prenotazione viene premuto
        newTeam.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                NewTeam newTeam  = new NewTeam(myController, ResponsabileLoggato);

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(HomePageR.this);

                mainWindow.addCardPanel(newTeam, "newTeam");

            }
        });

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

        //Bottone Organigramma
        BtnLayout Organig = new BtnLayout("Organigramma Teams");
        Organig.setFont(new FontUIResource(font, style, sizeFont));

        rightGbc.gridx = 0;
        rightGbc.gridy = 3;
        rightGbc.fill = GridBagConstraints.HORIZONTAL;
        rightHomePage.add(Organig, rightGbc);

        //Disegno Background
        try {
            // Carica l'immagine di sfondo dal file specificato
            leftHomePageBackground = ImageIO.read(new File("src/GUI/icon/vecteezy_isometric-illustration-concept-team-discussion-about_9160177.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        add(leftHomePage);
        add(rightHomePage);

    }
}


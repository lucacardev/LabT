package GUI;

import UTILITIES.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MainWindow extends JFrame {

    private Controller myController;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel passwordRecoveryPanel;

    private MyBooking myBookingPage;

    private PaginaLogin paginaLogin;

    public MainWindow(Controller controller) {

        super("Lab-T");

        myController = controller;

        setSize(800,512);
        try {
            BufferedImage iconImage = ImageIO.read(new File("src/GUI/icon/favicon.png"));
            setIconImage(iconImage);
        } catch (IOException e) {
            e.printStackTrace();
        };


        //Posizionamento finestra centrale
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        add(cardPanel);

        //Inserimento nel CardPanel della pagina di Login
        paginaLogin = new PaginaLogin(myController);
        cardPanel.add(paginaLogin, "login");

        //Inserimento nel CardPanel della pagina di recupero credenziali
        passwordRecoveryPanel = new PasswordRecovery(myController);
        cardPanel.add(passwordRecoveryPanel, "recoveryPage");

        cardLayout.show(cardPanel, "login");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setVisible(true);


    }

    //Metodo per aggiungere e mostrare un Panel al CardPanel
    public void addCardPanel(JPanel classPanel, String keyPanel) {

        cardPanel.add(classPanel, keyPanel);
        cardLayout.show(cardPanel, keyPanel);

    }

    public void showCard(String keyPanel) {
        cardLayout.show(cardPanel, keyPanel);
    }

    public boolean containsCard(String keyPanel) {
        Component[] components = cardPanel.getComponents();
        for (Component component : components) {
            if (component instanceof JPanel && keyPanel.equals(component.getName())) {
                return true;
            }
        }
        return false;
    }
}

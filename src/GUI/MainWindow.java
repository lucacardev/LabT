package GUI;

import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private Controller myController;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private JPanel passwordRecoveryPanel;

    private PaginaLogin paginaLogin;

    public MainWindow(Controller controller) {

        super("Lab-T");

        myController = controller;

        setSize(800,512);

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
}

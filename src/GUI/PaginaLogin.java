package GUI;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

import UTILITIES.Controller;


public class PaginaLogin extends JPanel{

    private CardLayout cardLayout;
    private JPanel cardPanel;

    Controller myController;

    public PaginaLogin(Controller controller)  {


        myController = controller;


        //Diviamo la pagina a metà (destra e sinistra)
       setLayout(new GridLayout(0,2));

        LeftLoginImage leftPanel = new LeftLoginImage();
        add(leftPanel);

        RightLoginAccess rightPanel = new RightLoginAccess(myController);
        add(rightPanel);


    }


}

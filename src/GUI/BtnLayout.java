package GUI;

import javax.swing.*;
import javax.swing.plaf.ColorUIResource;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.Border;
import javax.swing.border.AbstractBorder;

public class BtnLayout extends JButton {

    public BtnLayout(String nameButton) {

        super(nameButton);

        this.setBackground(new Color(35,171,144));
        this.setForeground(ColorUIResource.white);

    }

    }
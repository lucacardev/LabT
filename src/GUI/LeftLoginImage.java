package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LeftLoginImage extends JPanel{


    public LeftLoginImage() {

        setLayout(new GridBagLayout());

        JPanel upPanel = new JPanel();
        GridBagConstraints gbc = new GridBagConstraints();


        upPanel.setBackground(Color.WHITE);
        JLabel logo = new JLabel(new NoScalingIcon(new ImageIcon("src/GUI/icon/LogoLabT.png")));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;


        upPanel.add(logo);
        add(upPanel, gbc);

    }
}
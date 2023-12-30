package GUI;

import javax.swing.*;
import java.awt.*;

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
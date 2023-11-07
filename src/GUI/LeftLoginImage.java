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

        setLayout(new GridLayout(2,1));

        // Pannello superiore
        JPanel upPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // Dimensioni del pannello superiore
                int width = getWidth();
                int height = getHeight();

                // Colori della sfumatura
                Color startColor = new Color(35, 171, 144); // Colore iniziale (verde)
                Color endColor = new Color(255, 255, 255); // Colore finale (bianco)

                // Punto iniziale e punto finale per la sfumatura
                Point2D startPoint = new Point2D.Float(0, 0); // Inizia dalla parte superiore sinistra
                Point2D endPoint = new Point2D.Float(0, height); // Finisce nella parte inferiore sinistra

                // Crea l'oggetto GradientPaint per la sfumatura
                GradientPaint gradientPaint = new GradientPaint(startPoint, startColor, endPoint, endColor);

                // Imposta il colore di sfondo del pannello come la sfumatura
                Graphics2D g2d = (Graphics2D) g;
                g2d.setPaint(gradientPaint);
                g2d.fillRect(0, 0, width, height);
            }
        };
        upPanel.setLayout(new GridBagLayout());

        // Pannello inferiore
        JPanel footPanel = new JPanel();
        footPanel.setLayout(new GridBagLayout());
        footPanel.setBackground(Color.white);

        add(upPanel);
        add(footPanel);


        //Immagine logo
        ImageIcon logoImage = new ImageIcon("src/GUI/icon/chemistry-8969_128.gif");
        JLabel imageLabel = new JLabel(logoImage);

        footPanel.add(imageLabel);


        //Testo sopra il logo
        JLabel logoText = new JLabel("Benvenuto in LAB-T");
        Font welcomeFont = logoText.getFont();
        int fontSize = welcomeFont.getSize() + 20;
        Font increaseFont = welcomeFont.deriveFont((float)fontSize);
        logoText.setFont(increaseFont);
        logoText.setForeground(Color.black);

        upPanel.add(logoText);

    /*
        //Testo secondario
        JLabel t2Text = new JLabel("Misurate ciò che è misurabile e " +
                "rendete misurabile ciò che non lo è.");

        Font t2Font = t2Text.getFont();
        int t2FontSize = t2Font.getSize() + 8;
        Font t2Increase = t2Font.deriveFont((float)t2FontSize);
        logoText.setFont(t2Increase);

        middlePanel.add(t2Text);
    */


    }
}
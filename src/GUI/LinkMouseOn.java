package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LinkMouseOn extends JLabel{
    
    public LinkMouseOn(String text) {
        super(text);
        
    }
    
    public void ActiveLinkMouseOn(Color color, Color firstColor) {
        addMouseListener(new MouseAdapter() {

            //Cambio colore del testo al passaggio del mouse
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(color);
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, color));

            }

            //Ripristino colore originale
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(firstColor);
                setBorder(null);
            }

        });
        
    }

}
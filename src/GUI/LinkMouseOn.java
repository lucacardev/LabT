package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.Component;
import javax.swing.JComponent;

public class LinkMouseOn extends JLabel{
    
    public LinkMouseOn(String text) {
        super(text);
        
    }
    
    public void ActiveLinkMouseOn(JLabel label) {

        addMouseListener(new MouseAdapter() {

            //Cambio colore del testo password dimenticata al passaggio del mouse
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(new Color(35, 171, 144));
                setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(35, 171, 144)));
            }

            //Ripristino colore originale
            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Color.BLACK);
                setBorder(null);
            }
        });
        
    }
}

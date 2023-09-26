package GUI;

import javax.swing.*;
import java.awt.*;

public class IncreaseFont extends JLabel {

    public IncreaseFont(String text) {

        super(text);

    }

    public void increaseFont(JLabel mainText, int increment ) {

        Font getFont = mainText.getFont();
        int fontSize = getFont.getSize() + increment;
        Font increaseFont = getFont.deriveFont((float)fontSize);
        mainText.setFont(increaseFont);


    }
}

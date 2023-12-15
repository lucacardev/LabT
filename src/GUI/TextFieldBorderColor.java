package GUI;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

public class TextFieldBorderColor extends JTextField {

    public TextFieldBorderColor(int columns) {
        super(columns);
    }

    public static void changeTextFieldBorderColor(JTextField textField)  {

        textField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                textField.setBorder(new LineBorder(new Color(35, 171, 144), 2));
            }

            @Override
            public void focusLost(FocusEvent e) {
                textField.setBorder(new LineBorder(Color.BLACK));
            }
        });


    }
    public static void disableTextField(JTextField textField) {
        textField.setEditable(false);
        textField.setBackground(Color.WHITE);
    }

}

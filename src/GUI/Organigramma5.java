package GUI;

import javax.swing.*;
import java.awt.*;

public class Organigramma5 extends JFrame {

    public Organigramma5() {
        setTitle("Organigramma");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);

        OrganigrammaPanel organigrammaPanel = new OrganigrammaPanel();
        add(organigrammaPanel);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Organigramma5());
    }
}

class OrganigrammaPanel extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        int leaderX = getWidth() / 2;
        int leaderY = 50;
        int coordinatoreX = getWidth() / 2;
        int coordinatoreY = 150;
        int responsabileY = 300;
        int responsabile1X = getWidth() / 2 - 150;
        int responsabile2X = getWidth() / 2;
        int responsabile3X = getWidth() / 2 + 150;

        // Disegna i nodi rettangolari
        disegnaNodo(g2d, leaderX, leaderY, "Leader");
        disegnaNodo(g2d, coordinatoreX, coordinatoreY, "Coordinatore");
        disegnaNodo(g2d, responsabile1X, responsabileY, "Responsabile 1");
        disegnaNodo(g2d, responsabile2X, responsabileY, "Responsabile 2");
        disegnaNodo(g2d, responsabile3X, responsabileY, "Responsabile 3");

        // Disegna i collegamenti tra i nodi
        disegnaCollegamento(g2d, leaderX, leaderY + 20, coordinatoreX, coordinatoreY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile1X, responsabileY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile2X, responsabileY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile3X, responsabileY - 20);
    }

    private void disegnaNodo(Graphics2D g2d, int x, int y, String testo) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 50, y - 20, 100, 40);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x - 50, y - 20, 100, 40);
        g2d.drawString(testo, x - 20, y + 5);
    }

    private void disegnaCollegamento(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d.drawLine(x1, y1, x2, y2);
    }
}




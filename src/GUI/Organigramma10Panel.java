package GUI;

import DTO.Responsabile;
import DTO.Team;
import DTO.Tecnico;
import UTILITIES.Controller;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class Organigramma10Panel extends JPanel {

    Controller myController;
    Responsabile responsabile;
    private final List<Tecnico> tecList;
    private BufferedImage backgroundImage;
    private final Team team;
    private ModOrganigramma modOrganigramma;

    public Organigramma10Panel (Controller controller,Responsabile loggedInManager,List<Tecnico> tecnici, Team t) {

        myController = controller;
        responsabile = loggedInManager;
        tecList = tecnici;
        this.team = t;

        JLabel teamLabel = new JLabel("Organigramma del " + team.getNome());
        teamLabel.setForeground(new Color(23, 65, 95));
        Font font = new Font("Helvetica", Font.BOLD, 25);
        teamLabel.setFont(font);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);

        //Impostazione background
        try {
            backgroundImage = ImageIO.read(new File("src/GUI/icon/SfondoR.png"));

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background organigramma10");
        }


        //Trova il leader nella lista e posizionalo come primo elemento
        Tecnico leader = leaderSearch(tecList, t);

        if (leader != null) {

            tecList.remove(leader);
            tecList.add(0, leader); // Inserisci il leader come primo elemento

        }

        BtnLayout modifyButton = new BtnLayout("Modifica Organigramma");
        modifyButton.setBackground(new Color(23,65,95));
        modifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma10Panel.this);
                modOrganigramma = new ModOrganigramma(mainWindow, myController, responsabile, tecList, team);
                modOrganigramma.setVisible(true);

            }
        });

        BtnLayout backButton = new BtnLayout("Indietro");
        backButton.setBackground(Color.RED);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myTeam = new MyTeam(myController,responsabile);
                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma10Panel.this);
                mainWindow.addCardPanel(myTeam, "myTeam");

            }
        });

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weighty = 0.50;
        gbc.weightx = 0.33;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        add(teamLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.50;
        gbc.weightx = 0.33;
        gbc.anchor = GridBagConstraints.SOUTH;
        add(modifyButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weighty = 0.50;
        gbc.weightx = 0.33;
        gbc.anchor = GridBagConstraints.SOUTHWEST;
        add(backButton, gbc);

    }

    private Tecnico leaderSearch(List<Tecnico> tecList, Team team) {

        for (Tecnico tecnico : tecList) {

            if (tecnico.getMatricola().equals(team.getMatricolaL())) {

                return tecnico; // Restituisce il tecnico se la matricola corrisponde al leader
            }

        }

        return null; // Se non viene trovato il leader

    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        // Disegna l'immagine di sfondo con interpolazione bilineare
        if (backgroundImage != null) {

            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

        }

        g2d.setColor(Color.BLACK);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        int leaderX = getWidth() / 2;
        int leaderY = 55;
        int coordinatoreX1 = getWidth() / 2 - 150;
        int coordinatoreX2 = getWidth() / 2 + 150;
        int coordinatoreY = 150;
        int responsabileY1 = 350;
        int responsabileY2 = 250;
        int responsabile1X = getWidth() / 2 + 20;
        int responsabile2X = getWidth() / 2 - 90;
        int responsabile3X = getWidth() / 2 - 210;
        int responsabile4X = getWidth() / 2 - 330;

        int responsabile5X = getWidth() / 2 + 10 ;
        int responsabile6X = getWidth() / 2 + 150;
        int responsabile7X = getWidth() / 2 + 300;

        // Disegna i nodi rettangolari e mostra i nomi dei tecnici
        drawsNode(g2d, leaderX, leaderY, tecList.get(0));
        drawsNode(g2d, coordinatoreX1, coordinatoreY, tecList.get(1));
        drawsNode(g2d, coordinatoreX2, coordinatoreY, tecList.get(2));
        drawsNode(g2d, responsabile1X, responsabileY1, tecList.get(3));
        drawsNode(g2d, responsabile2X, responsabileY1, tecList.get(4));
        drawsNode(g2d, responsabile3X, responsabileY1, tecList.get(5));
        drawsNode(g2d, responsabile4X, responsabileY1, tecList.get(6));
        drawsNode(g2d, responsabile5X, responsabileY2, tecList.get(7));
        drawsNode(g2d, responsabile6X, responsabileY2, tecList.get(8));
        drawsNode(g2d, responsabile7X, responsabileY2, tecList.get(9));

        // Disegna i collegamenti tra i nodi
        disegnaCollegamento(g2d, leaderX, leaderY + 20, coordinatoreX1, coordinatoreY - 20);
        disegnaCollegamento(g2d, leaderX, leaderY + 20, coordinatoreX2, coordinatoreY - 20);
        disegnaCollegamento(g2d, coordinatoreX1, coordinatoreY + 20, responsabile1X, responsabileY1 - 20);
        disegnaCollegamento(g2d, coordinatoreX1, coordinatoreY + 20, responsabile2X, responsabileY1 - 20);
        disegnaCollegamento(g2d, coordinatoreX1, coordinatoreY + 20, responsabile3X, responsabileY1 - 20);
        disegnaCollegamento(g2d, coordinatoreX1, coordinatoreY + 20, responsabile4X, responsabileY1 - 20);
        disegnaCollegamento(g2d, coordinatoreX2, coordinatoreY + 20, responsabile5X, responsabileY2 - 20);
        disegnaCollegamento(g2d, coordinatoreX2, coordinatoreY + 20, responsabile6X, responsabileY2 - 20);
        disegnaCollegamento(g2d, coordinatoreX2, coordinatoreY + 20, responsabile7X, responsabileY2 - 20);

    }


    private void drawsNode(Graphics2D g2d, int x, int y, Tecnico tecnico) {

        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 50, y - 20, 100, 40);

        //Imposta colore giallo bordi rettangoli
        g2d.setColor(new Color(246, 183, 55));
        g2d.drawRect(x - 50, y - 20, 100, 40);

        FontMetrics fm = g2d.getFontMetrics();
        String tecText = tecnico.getNome() + " " + tecnico.getCognome();
        String matricola = tecnico.getMatricola();

        int larghezzaTesto = fm.stringWidth(tecText);
        int larghezzaMatricola = fm.stringWidth(matricola);

        // Calcola la posizione per centrare il testo nel rettangolo
        int xTesto = x - larghezzaTesto / 2;
        int xMatricola = x - larghezzaMatricola / 2;
        int yMatricola = y + 15; // Spazio tra il testo e la matricola

        // Imposta il colore nero per il testo
        g2d.setColor(Color.black);

        // Disegna il testo del tecnico
        g2d.drawString(tecText, xTesto, y);

        // Disegna la matricola sotto il testo
        g2d.drawString(matricola, xMatricola, yMatricola);

    }

    private void disegnaCollegamento(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        int border = 2;
        g2d.setStroke(new BasicStroke(border));
        g2d.setColor(new Color(23, 65, 95));
        g2d.drawLine(x1, y1, x2, y2);
    }

}
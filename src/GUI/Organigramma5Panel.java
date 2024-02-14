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

public class Organigramma5Panel extends JPanel {

    Controller myController;
    Responsabile responsabile;
    private List<Tecnico> tecList;
    private Team team;
    private ModOrganigramma modOrganigramma;
    private BufferedImage backgroundImage;

    public Organigramma5Panel(Controller controller,Responsabile loggedInManager,List<Tecnico> Tecnici, Team t) {

        myController = controller;
        responsabile = loggedInManager;
        tecList = Tecnici;
        this.team = t;


        JLabel teamLabel = new JLabel("Organigramma del " + team.getNome());
        teamLabel.setForeground(new Color(23, 65, 95));
        Font font = new Font("Helvetica", Font.BOLD, 25);
        teamLabel.setFont(font);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);


        //Impostazione background
        try {
            backgroundImage = ImageIO.read(new File("src/GUI/icon/SfondoR.png"));

        } catch (Exception ex) {
            System.out.println("Errore caricamento immagine background organigramma5");
        }


        //Trova il leader nella lista e posizionalo come primo elemento

        Tecnico leader = leaderSearch(tecList, t);

        if (leader != null) {

            tecList.remove(leader);
            tecList.add(0, leader); // Inserisci il leader come primo elemento

        }

        JButton modifyButton = new JButton("Modifica Organigramma");
        modifyButton.setBackground(new Color(23,65,95));
        modifyButton.setForeground(Color.WHITE);
        modifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma5Panel.this);
                modOrganigramma = new ModOrganigramma(mainWindow, myController, responsabile, tecList, team);
                modOrganigramma.setVisible(true);

            }
        });

        JButton backButton = new JButton("Indietro");
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myTeam = new MyTeam(myController,responsabile);
                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma5Panel.this);
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
        int leaderY = 90;
        int coordinatoreX = getWidth() / 2;
        int coordinatoreY = 190;
        int responsabileY = 300;
        int responsabile1X = getWidth() / 2 - 150;
        int responsabile2X = getWidth() / 2;
        int responsabile3X = getWidth() / 2 + 150;


        // Disegna i nodi rettangolari e mostra i nomi dei tecnici
        drawsNode(g2d, leaderX, leaderY, tecList.get(0));
        drawsNode(g2d, coordinatoreX, coordinatoreY, tecList.get(1));
        drawsNode(g2d, responsabile1X, responsabileY, tecList.get(2));
        drawsNode(g2d, responsabile2X, responsabileY, tecList.get(3));
        drawsNode(g2d, responsabile3X, responsabileY, tecList.get(4));

        // Disegna i collegamenti tra i nodi (x,y punto partenza, x,y punto di arrivo )
        drawConnection(g2d, leaderX, leaderY + 20, coordinatoreX, coordinatoreY - 20);
        drawConnection(g2d, coordinatoreX, coordinatoreY + 20, responsabile1X, responsabileY - 20);
        drawConnection(g2d, coordinatoreX, coordinatoreY + 20, responsabile2X, responsabileY - 20);
        drawConnection(g2d, coordinatoreX, coordinatoreY + 20, responsabile3X, responsabileY - 20);
    }

    private void drawsNode(Graphics2D g2d, int x, int y, Tecnico tecnico) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 50, y - 20, 100, 40); //Rettangolo centrato in x,y

        //Imposta colore giallo bordi rettangoli
        g2d.setColor(new Color(246, 183, 55));
        g2d.drawRect(x - 50, y - 20, 100, 40);


        FontMetrics fm = g2d.getFontMetrics();
        String tecText = tecnico.getNome() + " " + tecnico.getCognome();
        String matriculationNumber = tecnico.getMatricola();

        int widthText = fm.stringWidth(tecText);
        int larghezzaMatricola = fm.stringWidth(matriculationNumber);

        // Calcola la posizione per centrare il testo nel rettangolo
        int xTesto = x - widthText / 2;
        int xMatricola = x - larghezzaMatricola / 2;
        int yMatricola = y + 15; // Spazio tra il testo e la matricola

        // Imposta il colore nero per il testo
        g2d.setColor(Color.black);

        // Disegna il testo del tecnico
        g2d.drawString(tecText, xTesto, y);

        // Disegna la matricola sotto il testo
        g2d.drawString(matriculationNumber, xMatricola, yMatricola);

    }

    private void drawConnection(Graphics2D g2d, int x1, int y1, int x2, int y2) {

        int border = 2;
        g2d.setStroke(new BasicStroke(border));
        g2d.setColor(new Color(23, 65, 95));
        g2d.drawLine(x1, y1, x2, y2);
    }

}
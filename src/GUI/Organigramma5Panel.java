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
    private List<Tecnico> listaTecnici;
    private Team team;
    private ModOrganigramma modOrganigramma;
    private BufferedImage backgroundImage;

    public Organigramma5Panel(Controller controller,Responsabile responsabileLoggato,List<Tecnico> Tecnici, Team t) {

        myController = controller;
        responsabile = responsabileLoggato;
        listaTecnici = Tecnici;
        this.team = t;
        BtnLayout modifyButton;


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

        Tecnico leader = trovaLeader(listaTecnici, t);

        if (leader != null) {

            listaTecnici.remove(leader);
            listaTecnici.add(0, leader); // Inserisci il leader come primo elemento

        }

        modifyButton = new BtnLayout("Modifica Organigramma");
        modifyButton.setBackground(new Color(23,65,95));
        modifyButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma5Panel.this);
                modOrganigramma = new ModOrganigramma(mainWindow, myController, responsabile, listaTecnici, team);
                modOrganigramma.setVisible(true);

            }
        });

        BtnLayout backButton = new BtnLayout("Indietro");
        backButton.setBackground(Color.RED);

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

    private Tecnico trovaLeader(List<Tecnico> listaTecnici, Team team) {

        for (Tecnico tecnico : listaTecnici) {

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
        disegnaNodo(g2d, leaderX, leaderY, listaTecnici.get(0));
        disegnaNodo(g2d, coordinatoreX, coordinatoreY, listaTecnici.get(1));
        disegnaNodo(g2d, responsabile1X, responsabileY, listaTecnici.get(2));
        disegnaNodo(g2d, responsabile2X, responsabileY, listaTecnici.get(3));
        disegnaNodo(g2d, responsabile3X, responsabileY, listaTecnici.get(4));

        // Disegna i collegamenti tra i nodi (x,y punto partenza, x,y punto di arrivo )
        disegnaCollegamento(g2d, leaderX, leaderY + 20, coordinatoreX, coordinatoreY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile1X, responsabileY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile2X, responsabileY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile3X, responsabileY - 20);
    }

    private void disegnaNodo(Graphics2D g2d, int x, int y, Tecnico tecnico) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 50, y - 20, 100, 40); //Rettangolo centrato in x,y

        //Imposta colore giallo bordi rettangoli
        g2d.setColor(new Color(246, 183, 55));
        g2d.drawRect(x - 50, y - 20, 100, 40);


        FontMetrics fm = g2d.getFontMetrics();
        String testoTecnico = tecnico.getNome() + " " + tecnico.getCognome();
        String matricola = tecnico.getMatricola();

        int larghezzaTesto = fm.stringWidth(testoTecnico);
        int larghezzaMatricola = fm.stringWidth(matricola);

        // Calcola la posizione per centrare il testo nel rettangolo
        int xTesto = x - larghezzaTesto / 2;
        int xMatricola = x - larghezzaMatricola / 2;
        int yMatricola = y + 15; // Spazio tra il testo e la matricola

        // Imposta il colore nero per il testo
        g2d.setColor(Color.black);

        // Disegna il testo del tecnico
        g2d.drawString(testoTecnico, xTesto, y);

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
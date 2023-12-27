package GUI;

import DTO.Responsabile;
import DTO.Team;
import DTO.Tecnico;
import UTILITIES.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Organigramma5Panel extends JPanel {

    Controller myController;
    Responsabile responsabile;
    private List<Tecnico> Tecnici;
    private Team team;
    private BtnLayout backButton = new BtnLayout("Indietro");

    public Organigramma5Panel(Controller controller,Responsabile responsabileLoggato,List<Tecnico> Tecnici, Team t) {

        myController = controller;
        responsabile = responsabileLoggato;
        this.Tecnici = Tecnici;
        this.team = t;

        setBackground(new Color(99, 178, 231, 255));
        JLabel teamLabel = new JLabel("Organigramma del " + team.getNome());
        teamLabel.setForeground(Color.BLACK);
        Font font = new Font("Helvetica", Font.BOLD, 20);

        teamLabel.setFont(font);

        setLayout(new BorderLayout());

        //Trova il leader nella lista e posizionalo come primo elemento
        Tecnico leader = trovaLeader(this.Tecnici, this.team);
        if (leader != null) {
            this.Tecnici.remove(leader);
            this.Tecnici.add(0, leader); // Inserisci il leader come primo elemento
        }

        backButton.setBackground(Color.RED);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myTeam = new MyTeam(myController,responsabile);
                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma5Panel.this);
                mainWindow.addCardPanel(myTeam, "myTeam");
            }
        });


        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(teamLabel, BorderLayout.NORTH);

        add(backButton, BorderLayout.SOUTH);
    }

    private Tecnico trovaLeader(List<Tecnico> listaTecnici, Team team) {
        String matricolaLeader = team.getMatricolaL();
        for (Tecnico tecnico : listaTecnici) {
            if (tecnico.getMatricola().equals(matricolaLeader)) {
                return tecnico; // Restituisce il tecnico se la matricola corrisponde al leader
            }
        }
        return null; // Se non viene trovato il leader
    }

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


        // Disegna i nodi rettangolari e mostra i nomi dei tecnici
        disegnaNodo(g2d, leaderX, leaderY, Tecnici.get(0));
        disegnaNodo(g2d, coordinatoreX, coordinatoreY, Tecnici.get(1));
        disegnaNodo(g2d, responsabile1X, responsabileY, Tecnici.get(2));
        disegnaNodo(g2d, responsabile2X, responsabileY, Tecnici.get(3));
        disegnaNodo(g2d, responsabile3X, responsabileY, Tecnici.get(4));

        // Disegna i collegamenti tra i nodi
        disegnaCollegamento(g2d, leaderX, leaderY + 20, coordinatoreX, coordinatoreY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile1X, responsabileY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile2X, responsabileY - 20);
        disegnaCollegamento(g2d, coordinatoreX, coordinatoreY + 20, responsabile3X, responsabileY - 20);
    }

    private void disegnaNodo(Graphics2D g2d, int x, int y, Tecnico tecnico) {
        ImageIcon imageIcon = new ImageIcon("src/GUI/icon/3317.jpg");
        Image backgroundImage = imageIcon.getImage();

        // Disegna l'immagine come sfondo dei riquadri dei nodi
        g2d.drawImage(backgroundImage, x - 50, y - 20, 100, 40, null);

        int border = 2;
        g2d.setStroke(new BasicStroke(border));

        g2d.drawRect(x - 50, y - 20, 100, 40);
        FontMetrics fm = g2d.getFontMetrics();
        String testoTecnico = tecnico.getNome() + " " + tecnico.getCognome();
        String matricola = tecnico.getMatricola();

        int larghezzaTesto = fm.stringWidth(testoTecnico);
        int larghezzaMatricola = fm.stringWidth(matricola);

        // Calcola la posizione per centrare il testo nel rettangolo
        int xTesto = x - larghezzaTesto / 2;
        int xMatricola = x - larghezzaMatricola / 2;
        int yTesto = y;
        int yMatricola = y + 15; // Spazio tra il testo e la matricola

        // Disegna il testo del tecnico
        g2d.drawString(testoTecnico, xTesto, yTesto);

        // Disegna la matricola sotto il testo
        g2d.drawString(matricola, xMatricola, yMatricola);
    }

    private void disegnaCollegamento(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        int border = 2;
        g2d.setStroke(new BasicStroke(border));
        g2d.setColor(new Color(5, 5, 75));
        g2d.drawLine(x1, y1, x2, y2);
    }
}

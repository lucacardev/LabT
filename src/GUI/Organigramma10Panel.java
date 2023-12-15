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

public class Organigramma10Panel extends JPanel {

    Controller myController;
    Responsabile responsabile;
    private List<Tecnico> Tecnici;
    private Team team;
    private BtnLayout backButton = new BtnLayout("Indietro");

    public Organigramma10Panel (Controller controller,Responsabile responsabileLoggato,List<Tecnico> Tecnici, Team t) {

        myController = controller;
        responsabile = responsabileLoggato;
        this.Tecnici = Tecnici;
        this.team = t;

        JLabel teamLabel = new JLabel("Organigramma del " + team.getNome());


        setLayout(new BorderLayout());

        //Trova il leader nella lista e posizionalo come primo elemento
        Tecnico leader = trovaLeader(this.Tecnici, t);
        if (leader != null) {
            this.Tecnici.remove(leader);
            this.Tecnici.add(0, leader); // Inserisci il leader come primo elemento
        }

        backButton.setBackground(Color.RED);
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MyTeam myTeam = new MyTeam(myController,responsabile);
                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(Organigramma10Panel.this);
                mainWindow.addCardPanel(myTeam, "myTeam");
            }
        });

        //JLabel teamLabel = new JLabel("Organigramma del " + t.getNome());
        teamLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(teamLabel, BorderLayout.NORTH);

        add(backButton, BorderLayout.SOUTH);
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
        g2d.setColor(Color.BLACK);

        int leaderX = getWidth() / 2;
        int leaderY = 50;
        int coordinatoreX1 = getWidth() / 2 - 150;
        int coordinatoreX2 = getWidth() / 2 + 150;
        int coordinatoreY = 150;
        int responsabileY1 = 350;
        int responsabileY2 = 250;
        int responsabile1X = getWidth() / 2 + 20;
        int responsabile2X = getWidth() / 2 - 90;
        int responsabile3X = getWidth() / 2 - 210;
        int responsabile4X = getWidth() / 2 - 330;

        // Responsabili del primo coordinatore un po' più in alto
        int responsabile5X = getWidth() / 2 + 10 ;
        int responsabile6X = getWidth() / 2 + 150;
        int responsabile7X = getWidth() / 2 + 300;

        // Disegna i nodi rettangolari e mostra i nomi dei tecnici
        disegnaNodo(g2d, leaderX, leaderY, Tecnici.get(0));
        disegnaNodo(g2d, coordinatoreX1, coordinatoreY, Tecnici.get(1));
        disegnaNodo(g2d, coordinatoreX2, coordinatoreY, Tecnici.get(2));
        disegnaNodo(g2d, responsabile1X, responsabileY1, Tecnici.get(3));
        disegnaNodo(g2d, responsabile2X, responsabileY1, Tecnici.get(4));
        disegnaNodo(g2d, responsabile3X, responsabileY1, Tecnici.get(5));
        disegnaNodo(g2d, responsabile4X, responsabileY1, Tecnici.get(6));
        disegnaNodo(g2d, responsabile5X, responsabileY2, Tecnici.get(7));
        disegnaNodo(g2d, responsabile6X, responsabileY2, Tecnici.get(8));
        disegnaNodo(g2d, responsabile7X, responsabileY2, Tecnici.get(9));

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


    private void disegnaNodo(Graphics2D g2d, int x, int y, Tecnico tecnico) {
        g2d.setColor(Color.WHITE);
        g2d.fillRect(x - 50, y - 20, 100, 40);
        g2d.setColor(Color.BLACK);
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
        g2d.drawLine(x1, y1, x2, y2);
    }
}

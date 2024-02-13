package GUI;

import DTO.Strumento;
import DTO.Utente;
import UTILITIES.Controller;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class NewBooking extends JPanel{

    private final JPanel searchPanel;
    private final JPanel showResultPanel;
    private final JPanel footerPanel;

    private BtnLayout bookingButton;
    private BtnLayout calendarButton;
    private BtnLayout summaryButton;
    private BookingFrame bookingFrame;
    private CalendarDialog calendarDialog;
    private SummaryWindow summaryWindow;
    private Strumento selectedTool;
    private TablePanel tablePanel;

    private final TextFieldBorderColor headQuarters;
    private final TextFieldBorderColor searchForDescription;
    Controller myController;
    Utente loggedUser;
    GridBagConstraints footerPanelGbc = new GridBagConstraints();
    List<Strumento> toolList;
    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewBooking.this);
    String uniToolCode;


    public NewBooking(Controller controller, Utente currentUser) {

        myController = controller;
        loggedUser = currentUser;


        setLayout(new GridBagLayout());
        GridBagConstraints mainGbc = new GridBagConstraints();


        searchPanel = new JPanel();
        mainGbc.gridx = 0;
        mainGbc.gridy = 0;
        mainGbc.weightx = 0;
        mainGbc.weighty = 0;
        mainGbc.fill = GridBagConstraints.HORIZONTAL; //Prendiamo tutto lo spazio in orizzontale
        searchPanel.setBackground(new Color(35,171,144));

        add(searchPanel, mainGbc);

        showResultPanel = new JPanel();
        mainGbc.gridx = 0;
        mainGbc.gridy = 1;
        mainGbc.weighty = 1;
        mainGbc.weightx = 1;
        mainGbc.fill = GridBagConstraints.BOTH;
        mainGbc.anchor = GridBagConstraints.CENTER;
        add(showResultPanel, mainGbc);

        footerPanel = new JPanel();
        mainGbc.gridx = 0;
        mainGbc.gridy = 2;
        mainGbc.weightx = 0;
        mainGbc.weighty = 0;
        mainGbc.ipady = 8;
        mainGbc.fill = GridBagConstraints.HORIZONTAL;
        mainGbc.anchor = GridBagConstraints.PAGE_END;
        footerPanel.setBackground(Color.WHITE);

        add(footerPanel, mainGbc);

        //////////////////////////////SEARCH PANEL///////////////////////

        //Impostiamo il layout per il pannello superiore
        searchPanel.setLayout(new GridBagLayout());
        GridBagConstraints searchPanelGbc = new GridBagConstraints();
        searchPanelGbc.insets = new Insets(8,8,8,5);

        //Gli aggiungiamo la ricerca della sede

        IncreaseFont textHeadQuarters = new IncreaseFont("Sede: ");
        textHeadQuarters.increaseFont(textHeadQuarters, 5);
        textHeadQuarters.setForeground(Color.white);


        searchPanelGbc.gridx = 0;
        searchPanelGbc.gridy = 0;
        searchPanel.add(textHeadQuarters, searchPanelGbc);

        headQuarters = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(headQuarters);

        searchPanelGbc.gridx = 1;
        searchPanelGbc.gridy = 0;
        searchPanel.add(headQuarters, searchPanelGbc);

        //Gli aggiungiamo la ricerca in base alla descrizione

        IncreaseFont textSearchForDescription = new IncreaseFont("Descrizione: ");
        textSearchForDescription.setForeground(Color.white);
        textSearchForDescription.increaseFont(textSearchForDescription,5);
        searchPanelGbc.gridx = 2;
        searchPanelGbc.gridy = 0;
        searchPanel.add(textSearchForDescription, searchPanelGbc);

        searchForDescription = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(searchForDescription);

        searchPanelGbc.gridx = 3;
        searchPanelGbc.gridy = 0;
        searchPanel.add(searchForDescription, searchPanelGbc);

        //////////////////////////SHOW RESULT PANEL///////////////////////////////

        showResultPanel.setLayout(new BorderLayout());

        //Recupero tutta la lista degli strumenti per aggiungerla alla tabella

        tablePanel = new TablePanel(myController.allToolsListRecovery(), this, null);
        showResultPanel.add(tablePanel, BorderLayout.CENTER);


        //////////////////////////FOOTER PANEL///////////////////////////////

        footerPanel.setLayout(new GridBagLayout());
        footerPanelGbc.insets = new Insets(5,5,0,5);

        //Creazione bottone per tornare indietro

        BtnLayout backButton = new BtnLayout("Indietro");

        footerPanelGbc.gridx = 0;
        footerPanelGbc.gridy = 0;
        footerPanelGbc.anchor = GridBagConstraints.LINE_START;
        footerPanelGbc.insets = new Insets(5,15,10,0);
        footerPanelGbc.weightx = 0.33;
        footerPanel.add(backButton, footerPanelGbc);

        //Azione quando il pulsante indietro viene premuto

        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewBooking.this);

                if(mainWindow.containsCard("homePage")) {

                    mainWindow.showCard("homePage");

                } else {

                    HomePage homePage = new HomePage(myController, loggedUser);
                    mainWindow.addCardPanel(homePage, "homePage");

                }
            }
        });



        //Creazione bottone per effettuare la ricerca

        BtnLayout searchButton = new BtnLayout("Cerca");

        footerPanelGbc.gridx = 4;
        footerPanelGbc.gridy = 0;
        footerPanelGbc.weightx = 0.33;
        footerPanelGbc.anchor = GridBagConstraints.LINE_END;
        footerPanelGbc.insets = new Insets(5,0,10,15);
        searchButton.setBackground(Color.BLUE);
        footerPanel.add(searchButton, footerPanelGbc);

        //Bottone Cerca quando viene premuto, controlla che i campi siano tutti non vuoti ed esegue la ricerca

        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {

                    if(!getSede().isEmpty() && !getDescrizione().isEmpty()) {


                        toolList = myController.toolsListRecovery(getSede(), getDescrizione());

                        //Se la lista strumento è maggiore di zero significa che la sede esiste altrimenti diamo errore

                        if(!toolList.isEmpty()) {

                            showResultPanel.removeAll();
                            tablePanel = new TablePanel(toolList, NewBooking.this, null);

                            showResultPanel.add(tablePanel);
                            showResultPanel.revalidate();
                            showResultPanel.repaint();

                            //Bottone per annullare la ricerca

                            BtnLayout annulla = new BtnLayout("Annulla");
                            annulla.setBackground(Color.RED);
                            annulla.setForeground(Color.WHITE);
                            searchPanelGbc.gridx = 6;
                            searchPanelGbc.gridy = 0;
                            searchPanel.add(annulla, searchPanelGbc);

                            annulla.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    //cercato = false;
                                    headQuarters.setText("");
                                    searchForDescription.setText("");

                                    //Aggiorniamo i dati della tabella
                                    tablePanel.setData(myController.allToolsListRecovery());
                                    tablePanel.setDataMyBooking(myController.myBookingRecoveryC(loggedUser));

                                    searchPanel.remove(annulla);

                                    searchPanel.revalidate();
                                    searchPanel.repaint();

                                }
                            });
                        } else {

                            JOptionPane.showMessageDialog(null, "ATTENZIONE! La sede e la descrizione scelti " +
                                    "non corrispondono a nessuno strumento. ");
                        }

                        }


                    /////////////////////////////////RICERCA PER SEDE O DESCRIZIONE/////////////////////////////////////
                    //Se è stata inserita la sede allora effettuiamo la ricerca per sede altrimenti per descrizione

                     else if(!getSede().isEmpty() || !getDescrizione().isEmpty()) {

                        //cercato = true;

                        toolList = myController.toolsListRecovery(getSede(), getDescrizione());

                        //Se la lista strumento è maggiore di zero significa che la sede esiste altrimenti diamo errore

                        if(!toolList.isEmpty()) {

                            showResultPanel.removeAll();
                            tablePanel = new TablePanel(toolList, NewBooking.this, null);

                            showResultPanel.add(tablePanel);
                            showResultPanel.revalidate();
                            showResultPanel.repaint();

                            //Bottone per annullare la ricerca

                            BtnLayout annulla = new BtnLayout("Annulla");
                            annulla.setBackground(Color.RED);
                            annulla.setForeground(Color.WHITE);
                            searchPanelGbc.gridx = 6;
                            searchPanelGbc.gridy = 0;
                            searchPanel.add(annulla, searchPanelGbc);

                            annulla.addMouseListener(new MouseAdapter() {
                                @Override
                                public void mouseClicked(MouseEvent e) {

                                    //cercato = false;
                                    headQuarters.setText("");
                                    searchForDescription.setText("");

                                    //Aggiorniamo i dati della tabella
                                    tablePanel.setData(myController.allToolsListRecovery());

                                    searchPanel.remove(annulla);

                                    searchPanel.revalidate();
                                    searchPanel.repaint();

                                }
                            });

                        } else {
                            JOptionPane.showMessageDialog(null,"ATTENZIONE! La sede selezionata o la descrizione non sono validi");
                        }


                        ///////////////////////////////////////RICERCA PER DESCRIZIONE/////////////////////////////////////

                    }

                     else {

                     if(getSede().isEmpty() || getDescrizione().isEmpty()) {

                        JOptionPane.showMessageDialog(null, "ATTENZIONE! I campi non possono essere vuoti");

                    }

                }
            }

        });

    }

    public String getSede() {

        return headQuarters.getText().trim();

    }

    public String getDescrizione() {

        return searchForDescription.getText().trim();
    }

    /*Bottone di verifica disponibilità della strumento in base all'orario scelto dall'utente che non supera quello massimo d'utilizzo dello strumento*/
    public void bookingButtonAvailability() {

        if (bookingButton == null) {
            bookingButton = new BtnLayout("Verifica Disponibilità");
            footerPanelGbc.gridx = 1;
            footerPanelGbc.gridy = 0;
            footerPanelGbc.weightx = 0.22;
            bookingButton.setBackground(new Color(224, 186, 6));
            footerPanel.add(bookingButton, footerPanelGbc);

            footerPanel.revalidate();
            footerPanel.repaint();

            bookingButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    bookingFrame = new BookingFrame(mainWindow, myController, loggedUser, selectedTool);
                    bookingFrame.setCodStrumento(uniToolCode);
                    bookingFrame.setStrumentoAttuale(selectedTool);
                    bookingFrame.setVisible(true);

                }
            });

        }

        }

    public void bookingButtonCalendar() {

        if (calendarButton == null) {
            calendarButton = new BtnLayout("Calendario Prenotazioni");
            footerPanelGbc.gridx = 2;
            footerPanelGbc.gridy = 0;
            footerPanelGbc.weightx = 0.22;
            calendarButton.setBackground(new Color(224, 186, 6));
            footerPanel.add(calendarButton, footerPanelGbc);

            footerPanel.revalidate();
            footerPanel.repaint();

            calendarButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                    //Solo quando clicchiamo il pulsante viene creato il calendario
                    //Lo strumento selezionato lo ricaviamo grazie alla chiamata del metodo setStrumSelezCalend
                    //Dalla classe TablePanel viene passato lo strumento selezionato

                    calendarDialog = new CalendarDialog(mainWindow, myController, selectedTool);

                    calendarDialog.setVisible(true);

                }
            });

        }

    }

    public void setSummaryButton() {

        if (summaryButton == null) {
            summaryButton = new BtnLayout("Riepilogo");
            footerPanelGbc.gridx = 3;
            footerPanelGbc.gridy = 0;
            footerPanelGbc.weightx = 0.22;
            summaryButton.setBackground(Color.RED);
            footerPanel.add(summaryButton, footerPanelGbc);

            footerPanel.revalidate();
            footerPanel.repaint();

            summaryButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                summaryWindow = new SummaryWindow(mainWindow, myController, selectedTool);
                summaryWindow.setVisible(true);
                }
            });

        }

    }

    public void removeSummaryButton() {
        if (summaryButton!= null) {
            footerPanel.remove(summaryButton);
            summaryButton = null; // Rimuovi il riferimento al pulsante
            footerPanel.revalidate();
            footerPanel.repaint();
        }

    }

    public void removeBookingButton() {
        if (bookingButton != null) {
            footerPanel.remove(bookingButton);
            bookingButton = null; // Rimuovi il riferimento al pulsante
            footerPanel.revalidate();
            footerPanel.repaint();
        }

    }

    public void removeCalendarButton() {
        if (calendarButton != null) {
            footerPanel.remove(calendarButton);
            calendarButton = null; // Rimuovi il riferimento al pulsante
            footerPanel.revalidate();
            footerPanel.repaint();
        }

    }

    public void setCodStrumentoBookingFrame(String toolCodeBookingFrame) {

        uniToolCode = toolCodeBookingFrame;

    }

    public void setStrumSelezCalend(Strumento tool) {

        selectedTool = tool;
    }

}

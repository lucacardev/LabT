package GUI;

import DAO.StrumentoDAO;
import DTO.Sede;
import DTO.Strumento;
import DTO.Utente;
import UTILITIES.Controller;
import com.toedter.calendar.JCalendar;
import com.toedter.calendar.JDateChooser;

import javax.naming.ldap.Control;
import javax.swing.*;
import javax.swing.text.DateFormatter;
import javax.swing.text.DefaultFormatterFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class NewBooking extends JPanel{

    private static JPanel searchPanel;
    private static JPanel showResultPanel;
    private static JPanel footerPanel;

    private BookingFrame bookingFrame;
    private CalendarDialog calendarDialog;

    private SummaryWindow summaryWindow;


    private Strumento strumentoSelezionato;

    private TablePanel tablePanel;

    private BtnLayout bookingButton;
    private BtnLayout calendarButton;
    private BtnLayout riepilogoButton;

    TextFieldBorderColor sedeSearch;
    TextFieldBorderColor descrizioneSearch;

    JDateChooser dateChooser;
    Controller myController;
    Utente utenteLoggato;

    GridBagConstraints mainGbc;
    GridBagConstraints footerPanelGbc = new GridBagConstraints();

    List<Strumento> listaStrumento;

    MainWindow mainWindow = (MainWindow) SwingUtilities.getWindowAncestor(NewBooking.this);

    String codStrumentoUni;



    public NewBooking(Controller controller, Utente utenteCorrente) {

        myController = controller;
        utenteLoggato = utenteCorrente;


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

        IncreaseFont textSedeSearch = new IncreaseFont("Sede: ");
        textSedeSearch.increaseFont(textSedeSearch, 5);
        textSedeSearch.setForeground(Color.white);


        searchPanelGbc.gridx = 0;
        searchPanelGbc.gridy = 0;
        searchPanel.add(textSedeSearch, searchPanelGbc);

        sedeSearch = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(sedeSearch);

        searchPanelGbc.gridx = 1;
        searchPanelGbc.gridy = 0;
        searchPanel.add(sedeSearch, searchPanelGbc);

        //Gli aggiungiamo la ricerca in base alla descrizione

        IncreaseFont textDescrizioneSearch = new IncreaseFont("Descrizione: ");
        textDescrizioneSearch.setForeground(Color.white);
        textDescrizioneSearch.increaseFont(textDescrizioneSearch,5);
        searchPanelGbc.gridx = 2;
        searchPanelGbc.gridy = 0;
        searchPanel.add(textDescrizioneSearch, searchPanelGbc);

        descrizioneSearch = new TextFieldBorderColor(15);
        TextFieldBorderColor.changeTextFieldBorderColor(descrizioneSearch);

        searchPanelGbc.gridx = 3;
        searchPanelGbc.gridy = 0;
        searchPanel.add(descrizioneSearch, searchPanelGbc);

        /*
        //Aggiungiamo la possibilità di scelta della data
        IncreaseFont textDateSearch = new IncreaseFont("Data: ");
        textDateSearch.setForeground(Color.white);
        textDateSearch.increaseFont(textDateSearch, 5);
        searchPanelGbc.gridx = 4;
        searchPanelGbc.gridy = 0;
        searchPanel.add(textDateSearch, searchPanelGbc);
        */
        /*
        dateChooser = new JDateChooser();
        JCalendar calendar = dateChooser.getJCalendar();
        calendar.setMinSelectableDate(new Date());
        dateChooser.setDateFormatString("dd/MM/yyyy");  // Imposta il formato della data

        searchPanelGbc.gridx = 5;
        searchPanelGbc.gridy = 0;
        searchPanel.add(dateChooser, searchPanelGbc);
        */

        //////////////////////////SHOW RESULT PANEL///////////////////////////////

        showResultPanel.setLayout(new BorderLayout());

        //Recupero tutta la lista degli strumenti per aggiungerla alla tabella

        tablePanel = new TablePanel(myController.recuperoTuttaListaStrumenti(), this, null);
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

                    HomePage homePage = new HomePage(myController, utenteLoggato);
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





                    if(getSede().length() > 0 && getDescrizione().length() > 0) {


                        listaStrumento = myController.recuperoListaStrumenti(getSede(), getDescrizione());

                        //Se la lista strumento è maggiore di zero significa che la sede esiste altrimenti diamo errore

                        if(listaStrumento.size() > 0 ) {

                            showResultPanel.removeAll();
                            tablePanel = new TablePanel(listaStrumento, NewBooking.this, null);

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
                                    sedeSearch.setText("");
                                    descrizioneSearch.setText("");

                                    //Aggiorniamo i dati della tabella
                                    tablePanel.setData(myController.recuperoTuttaListaStrumenti());
                                    tablePanel.setDataMiePrenotazioni(myController.recuperoMiePrenotazioniC(utenteLoggato));

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

                     else if(getSede().length() > 0 || getDescrizione().length() > 0) {

                        //cercato = true;

                        listaStrumento = myController.recuperoListaStrumenti(getSede(), getDescrizione());

                        //Se la lista strumento è maggiore di zero significa che la sede esiste altrimenti diamo errore

                        if(listaStrumento.size() > 0 ) {

                            showResultPanel.removeAll();
                            tablePanel = new TablePanel(listaStrumento, NewBooking.this, null);

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
                                    sedeSearch.setText("");
                                    descrizioneSearch.setText("");

                                    //Aggiorniamo i dati della tabella
                                    tablePanel.setData(myController.recuperoTuttaListaStrumenti());

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

        return sedeSearch.getText().trim();

    }

    public String getDescrizione() {

        return descrizioneSearch.getText().trim();
    }

    public Date getSelectedDate() {

        return dateChooser.getDate();

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

                    bookingFrame = new BookingFrame(mainWindow, myController, utenteLoggato, strumentoSelezionato);
                    bookingFrame.setCodStrumento(codStrumentoUni);
                    bookingFrame.setStrumentoAttuale(strumentoSelezionato);
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

                    calendarDialog = new CalendarDialog(mainWindow, myController, strumentoSelezionato);

                    calendarDialog.setVisible(true);

                }
            });

        }


    }

    public void setRiepilogoButton() {

        if (riepilogoButton == null) {
            riepilogoButton = new BtnLayout("Riepilogo");
            footerPanelGbc.gridx = 3;
            footerPanelGbc.gridy = 0;
            footerPanelGbc.weightx = 0.22;
            riepilogoButton.setBackground(Color.RED);
            footerPanel.add(riepilogoButton, footerPanelGbc);

            footerPanel.revalidate();
            footerPanel.repaint();

            riepilogoButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {

                summaryWindow = new SummaryWindow(mainWindow, myController, strumentoSelezionato);
                summaryWindow.setVisible(true);



                }
            });

        }


    }

    public void removeRiepilogoButton() {
        if (riepilogoButton!= null) {
            footerPanel.remove(riepilogoButton);
            riepilogoButton = null; // Rimuovi il riferimento al pulsante
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

    public void setCodStrumentoBookingFrame(String codStrumentoBookingFrame) {

        codStrumentoUni = codStrumentoBookingFrame;

    }

    public void setStrumSelezCalend(Strumento strumento) {

        strumentoSelezionato = strumento;
    }




}

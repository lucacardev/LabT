package UTILITIES;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {

    public static void sendVerificationCode(String recipientEmail, String verificationCode, String setSubject, String setText ) {

        /*Per permettere l'invio della mail sfruttando i server Google bisogna:
        * 1- Creare Account Google
        * 2- Attivare l'autenticazione a due passaggi
        * 3- E creare una password per App
        * 4- Inserire la password fornita da Google nel codice, così da accedere*/

        //Credenziali mail dell'applicazione (mittente )
        String username = "labTProject@gmail.com";
        String password = "";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                return new PasswordAuthentication(username, password);

            }
        });

        try {

            // Creazione del messaggio email
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject(setSubject);
            message.setText( setText + verificationCode);

            // Invio del messaggio email
            Transport.send(message);

            System.out.println("Email inviata con successo!");

        } catch (MessagingException e) {

            e.printStackTrace();
            System.out.println("Errore durante l'invio dell'email.");

        }

    }

}

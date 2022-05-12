
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

    // SMTP Credentials for a throwaway GMAIL account created for this project

    static String from = "javaprojectspring2022@gmail.com";
    static String username = "javaprojectspring2022@gmail.com";
    static String password = "javajavajava";


    public static void send(String to, String subject, String msg) {

        // host of the SMTP server
        String host = "smtp.gmail.com";


        // Other configurations as needed
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a new message and set the parameters

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set the email subject
            message.setSubject(subject);
            // Set the message text
            message.setText(msg);


            // Send the email message
            Transport.send(message);
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

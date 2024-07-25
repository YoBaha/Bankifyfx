package models;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class EmailSender {
    public static void sendEmail(String recipient, String subject, String body) throws MessagingException {
        // Set debug property for TLS debugging
        System.setProperty("javax.net.debug", "all");

        Properties properties = EmailConfig.getProperties();
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EmailConfig.USERNAME, EmailConfig.PASSWORD);
            }
        });

        // Enable debugging
        session.setDebug(true);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(EmailConfig.USERNAME));
        message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
        message.setSubject(subject);
        message.setText(body);

        Transport.send(message);
    }}
package com.example.login;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;

public class SendEmail {
    public static void sendEmail(String to, String subject, String body) {
        // Email details
        String from = "phandang30122003@gmail.com"; // Replace with your email
        String password = "bptk diop gfto howr"; // Replace with your email password
        String host = "smtp.gmail.com"; // Replace with your SMTP server (e.g., smtp.gmail.com)

        // SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587"); // Replace with your SMTP port (e.g., 587 for TLS)

        // Authenticate the session
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            // Create a new email message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}

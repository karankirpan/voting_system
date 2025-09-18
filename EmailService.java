package org.example;
import java.security.GeneralSecurityException;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;
import com.sun.mail.util.MailSSLSocketFactory; // New import

public class EmailService {

    public static void sendOtpEmail(String recipientEmail, String otp) {
        final String senderEmail = "kunalviveksoyane@gmail.com";
        final String senderPassword = "nabc vwiw clwj lvvi";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // --- START OF INSECURE FIX ---
        // This part bypasses the certificate check.
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.smtp.ssl.socketFactory", sf);
        } catch (GeneralSecurityException e) {
            throw new RuntimeException("Failed to create trusting SSL socket factory", e);
        }
        // --- END OF INSECURE FIX ---

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Your One-Time Password (OTP) for Verification");
            String emailBody = "<h2>Email Verification</h2><p>Your OTP is: </p><h3 style='color:blue;'>" + otp + "</h3>";
            message.setContent(emailBody, "text/html");
            Transport.send(message);
            System.out.println("OTP email sent successfully to " + recipientEmail);
        } catch (MessagingException e) {
            System.err.println("Error sending OTP email: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
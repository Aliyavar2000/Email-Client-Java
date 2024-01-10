package mailclient.com.EmailAPI.send;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Base64;
import java.util.Properties;

public class SendMessage {
    private String smtpHost;
    private int smtpPort;
    private String senderEmail;
    private String senderPassword;
    private String receiverEmail;
    private String ccEmail;

    public SendMessage(String smtpHost, int smtpPort,
            String senderEmail, String senderPassword, String receiverEmail, String ccEmail) {
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
        this.senderEmail = senderEmail;
        this.senderPassword = senderPassword;
        this.receiverEmail = receiverEmail;
        this.ccEmail = ccEmail;
    }

    public void sendEmail(String subject, String content) {
        Properties props = new Properties();
        if (smtpPort == 465) {
            props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.ssl.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);
            props.put("mail.debug", "true");
            props.put("mail.debug.auth", "true");
        } else if (smtpPort == 587) {
            props = new Properties();
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", smtpHost);
            props.put("mail.smtp.port", smtpPort);

        } else {
            throw new IllegalArgumentException("Invalid SMTP port: " + smtpPort);
        }

        try {
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    String decodedUsername = decodeBase64(senderEmail);
                    String decodedPassword = decodeBase64(senderPassword);
                    return new PasswordAuthentication(decodedUsername, decodedPassword);
                }
            });

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(senderEmail));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(receiverEmail));
            message.setSubject(subject);
            message.setText(content);

            message.setRecipient(Message.RecipientType.CC, new InternetAddress(ccEmail));

            Transport.send(message, senderEmail, senderPassword);

            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private static String decodeBase64(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        return new String(decodedBytes);
    }
}

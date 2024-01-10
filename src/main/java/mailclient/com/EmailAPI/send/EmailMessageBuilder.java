package mailclient.com.EmailAPI.send;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailMessageBuilder {
    private Session session;
    private String senderEmail;

    public EmailMessageBuilder(Session session, String senderEmail) {
        this.session = session;
        this.senderEmail = senderEmail;
    }

    public static MimeMessage createMessage(Session session, String senderEmail,
            String receiverEmail, String subject, String content) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverEmail));
        message.setSubject(subject);
        message.setText(content);
        return message;
    }
}

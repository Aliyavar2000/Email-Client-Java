package mailclient.com.EmailAPI.send;

import javax.mail.*;
import javax.mail.internet.MimeMessage;

public class EmailSender {

    Transport transport;
    MimeMessage message;

    public EmailSender(Transport transport, MimeMessage message) {
        this.transport = transport;
        this.message = message;
    }

    public void sendMessage(Transport transport, MimeMessage message) throws MessagingException {
        transport.sendMessage(message, message.getAllRecipients());
    }
}

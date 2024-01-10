package mailclient.com.EmailAPI.send;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

public class EmailTransportBuilder {
    Session session;
    String smtpHost;
    int smtpPort;

    public EmailTransportBuilder(Session session, String smtpHost, int smtpPort) {
        this.session = session;
        this.smtpHost = smtpHost;
        this.smtpPort = smtpPort;
    }

    public Transport createTransport(Session session, String smtpHost, int smtpPort, String senderEmail,
            String senderPassword) throws MessagingException {
        Transport transport = session.getTransport("smtp");
        transport.connect(smtpHost, smtpPort, senderEmail, senderPassword);
        return transport;
    }
}

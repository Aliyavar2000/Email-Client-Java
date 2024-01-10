package mailclient.com.EmailAPI;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Session;
import javax.mail.Store;

import mailclient.com.connectionData.ConnectionInfo;
import mailclient.com.credentials.UserCredentials;

public class Connection {
    private Session emailSession;
    private Store store;
    private String host;
    private int port;
    private String user;
    private String password;
    private Folder inbox;
    private String protocol;

    public Connection() {
        this.host = ConnectionInfo.getincomingHost();
        this.port = ConnectionInfo.getincomingPort();
        this.protocol = ConnectionInfo.getincomingProtocol();
        this.user = UserCredentials.getUsername();
        this.password = UserCredentials.getPassword();
    }

    public void buildEmailConnection() {
        Properties props = new Properties();
        try {
            if (protocol.equals("POP3")) {
                props.put("mail.pop3.host", host);
                props.put("mail.pop3.port", port);
                if (port == 995) {
                    props.put("mail.pop3.ssl.enable", "true");
                }
                emailSession = Session.getDefaultInstance(props);
                if (port == 995) {
                    store = emailSession.getStore("pop3s");
                } else {
                    store = emailSession.getStore("pop3");
                }
            } else if (protocol.equals("IMAP")) {
                props.setProperty("mail.store.protocol", "imap");
                props.put("mail.imap.host", host);
                props.put("mail.imap.port", port);
                if (port == 993) {
                    props.put("mail.imap.ssl.enable", "true");
                }
                emailSession = Session.getInstance(props);
                if (port == 993) {
                    // store = emailSession.getStore("imaps");
                } else {
                    // store = emailSession.getStore("imap");
                }
                store = emailSession.getStore();
            } else {
                throw new IllegalArgumentException("Invalid protocol: " + protocol);
            }

            store.connect(host, user, password);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Connection failed");
        }
    }

    public void closeEmailConnection() {
        try {
            inbox.close(false); // closes the inbox folder. The argument false assures that any changes which
                                // were made, will not be saved
            store.close(); // closes the connetion to the mail server
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Store getStore() {
        return store;
    }
}
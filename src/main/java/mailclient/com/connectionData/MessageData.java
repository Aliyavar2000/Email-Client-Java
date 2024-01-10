package mailclient.com.connectionData;

public class MessageData {
    private String emaiReceiver;
    private String emailCc;
    private String emailSubject;
    private String emailText;

    public MessageData(String emailReciever, String emailCc, String emailSubject, String emailText) {
        this.emaiReceiver = emailReciever;
        this.emailSubject = emailSubject;
        this.emailText = emailText;
        this.emailCc = emailCc;
    }

    public String getEmailReciever() {
        return emaiReceiver;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public String getEmailText() {
        return emailText;
    }

    public String getEmailCc() {
        return emailCc;
    }
}

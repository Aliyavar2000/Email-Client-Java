package mailclient.com.EmailAPI.receive.model;

import java.io.Serializable;

public class ReceivedMessageModel implements Serializable {
    private String sender;
    private String[] receivers;
    private String subject;
    private Object content;
    private String receivedDate;
    private String sentDate;

    public ReceivedMessageModel(String sender, String[] receivers, String subject, Object content, String receivedDate,
            String sentDate) {
        this.sender = sender;
        this.receivers = receivers;
        this.subject = subject;
        this.content = content;
        this.receivedDate = receivedDate;
        this.sentDate = sentDate;
    }

    @Override
    public String toString() {
        return "EmailMessage [sender=" + sender + ", receiver=" + receivers[0] + ", subject=" + subject + ", content="
                + content + "]";
    }

    public String getFrom() {
        return sender;
    }

    public String[] getTo() {
        return receivers;
    }

    public String getSubject() {
        return subject;
    }

    public Object getContent() {
        return content;
    }

    public String getReceivedDate() {
        return receivedDate;
    }

    public String getSentDate() {
        return sentDate;
    }

}
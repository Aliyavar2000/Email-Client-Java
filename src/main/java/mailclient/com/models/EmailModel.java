package mailclient.com.models;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;

public class EmailModel {
    private String from;
    private String[] to;
    private String subject;
    private Object content;
    private String dateSent;
    private String dateReceived;

    public EmailModel(String from, String[] to, String subject, Object content, String dateSent, String dateReceived) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.content = content;
        this.dateSent = dateSent;
        this.dateReceived = dateReceived;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        String receivers = "";
        for (String receiver : to) {
            receivers += receiver + ", ";
        }
        String receiversFormatted = getFormattedReceivers(receivers);
        if (receiversFormatted == "") {
            return receivers;
        }
        return receiversFormatted;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content.toString();
    }

    public void setContent(Object content) {
        if (content instanceof MimeMessage) {
            MimeMessage mimeMessage = (MimeMessage) content;
            try {
                if (mimeMessage.isMimeType("text/plain")) {
                    this.content = (String) mimeMessage.getContent();
                } else if (mimeMessage.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) mimeMessage.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            this.content = (String) bodyPart.getContent();
                            break;
                        }
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof String) {
            this.content = (String) content;
        }
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }

    public String getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(String dateReceived) {
        this.dateReceived = dateReceived;
    }

    private String getFormattedReceivers(String receivers) {
        try {
            String[] receiverArray = receivers.split(",");
            Pattern pattern = Pattern.compile("\\\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}\\\\b");
            StringBuilder formattedReceivers = new StringBuilder();

            for (String receiver : receiverArray) {
                Matcher matcher = pattern.matcher(receiver);
                if (matcher.find()) {
                    String extractedReceiver = matcher.group(1);
                    formattedReceivers.append(extractedReceiver).append(", ");
                }
            }

            // Remove trailing comma and space
            if (formattedReceivers.length() > 0) {
                formattedReceivers.setLength(formattedReceivers.length() - 2);
            }

            return formattedReceivers.toString();
        } catch (Exception e) {
            System.out.println("Error getting formatted receivers");
            throw new RuntimeException(e);
        }
    }
}

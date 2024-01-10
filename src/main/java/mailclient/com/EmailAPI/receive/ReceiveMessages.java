package mailclient.com.EmailAPI.receive;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Store;
import javax.mail.internet.MimeMessage;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import mailclient.com.EmailAPI.receive.model.ReceivedMessageModel;
import mailclient.com.credentials.UserCredentials;

public class ReceiveMessages {

    private Folder inbox;
    private Store store;
    private List<ReceivedMessageModel> messages;

    public ReceiveMessages(Store store) {
        messages = new ArrayList<ReceivedMessageModel>();
        this.store = store;
    }

    public int getMessageCount() {
        if (inbox == null) {
            getInbox();
        }
        try {
            return inbox.getMessageCount();
        } catch (MessagingException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not get message count");
        }
    }

    private List<ReceivedMessageModel> getMessageUniversal(int j) {
        String sender;
        Address[] receiversAddresses;
        String subject;
        Object content;
        String receivedDateString;
        String sentDateString;
        try {
            if (inbox == null) {
                getInbox();
            }
            for (int i = j + 1; i <= inbox.getMessageCount(); i++) {
                Message currentMessage = inbox.getMessage(i);
                sender = getSender(currentMessage);
                receiversAddresses = currentMessage.getAllRecipients();
                String[] receivers = { UserCredentials.getUserMail() };
                if (receiversAddresses != null) {
                    receivers = new String[receiversAddresses.length];
                    for (int l = 0; l < receiversAddresses.length; l++) {
                        receivers[l] = receiversAddresses[l].toString();
                    }
                }
                subject = currentMessage.getSubject();
                content = currentMessage.getContent();
                Date receivedDate = currentMessage.getReceivedDate();
                receivedDateString = receivedDate != null ? receivedDate.toString() : "N/A";
                Date sentDate = currentMessage.getSentDate();
                sentDateString = sentDate != null ? sentDate.toString() : "N/A";
                ReceivedMessageModel message = new ReceivedMessageModel(sender, receivers, subject, content,
                        receivedDateString, sentDateString);
                messages.add(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return messages;
    }

    public List<ReceivedMessageModel> getMessages() {

        return getMessageUniversal(1);
    }

    public List<ReceivedMessageModel> getMessages(int count) {
        int i = count;
        // try {
        // i = inbox.getMessageCount() - count;
        // } catch (MessagingException e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
        return getMessageUniversal(i);
    }

    private void getInbox() {
        try {
            inbox = store.getFolder("INBOX"); // returns inbox folder
            inbox.open(Folder.READ_ONLY); // opens the inbox folder in read only mode
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int countMessages() { // returns the quantity of the messages in the inbox folder
        try {
            getInbox();
            return inbox.getMessageCount();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting message count from POP3 server");
        }
    }

    private String getSender(Message message) {
        try {
            String sender = message.getFrom()[0].toString();
            Pattern pattern = Pattern.compile("\\\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,}\\\\b");
            Matcher matcher = pattern.matcher(sender); // extracts the email address from the sender's name
            return matcher.find() ? matcher.group(1) : sender;
        } catch (Exception e) {
            System.out.println("Error getting sender");
            throw new RuntimeException(e);
        }
    }

    private JSONArray readJsonFromFile(String filepath) {
        try (FileReader fileReader = new FileReader(filepath)) {
            return new JSONArray(new JSONTokener(fileReader));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new JSONArray();
    }

    private void archiveJson(String filepath, JSONArray jsonArray) {
        // Append a timestamp to the existing file name to create the archive file name
        String archiveFileName = getArchiveFileName(filepath);
        String archiveFilePath = getArchiveFilePath(filepath, archiveFileName);

        try (FileWriter fileWriter = new FileWriter(archiveFilePath)) {
            fileWriter.write(jsonArray.toString());
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getArchiveFileName(String filepath) {
        File file = new File(filepath);
        String originalName = file.getName();
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex != -1) {
            String nameWithoutExtension = originalName.substring(0, dotIndex);
            String extension = originalName.substring(dotIndex);
            return nameWithoutExtension + "_" + getTimeStamp() + extension;
        } else {
            return originalName + "_" + getTimeStamp();
        }
    }

    private String getArchiveFilePath(String filepath, String archiveFileName) {
        File file = new File(filepath);
        String parentDirectory = file.getParent();
        return parentDirectory + File.separator + archiveFileName;
    }

    private String getTimeStamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return sdf.format(new Date());
    }

    public void writeToJsonFile(List<ReceivedMessageModel> messages, String filepath) {

        JSONArray jsonArray = new JSONArray();
        // Read the existing JSON file
        JSONArray existingJsonArray = readJsonFromFile(filepath);

        // Archive the existing JSON file
        archiveJson(filepath, existingJsonArray);

        for (ReceivedMessageModel message : messages) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Sender", message.getFrom());
            String receivers = extractReceivers(message.getTo());
            jsonObject.put("Receivers", receivers);
            jsonObject.put("Subject", message.getSubject());
            jsonObject.put("Content", getTextContent(message.getContent()));
            jsonObject.put("ReceivedDate", message.getReceivedDate());
            jsonObject.put("SentDate", message.getSentDate());

            jsonArray.put(jsonObject);
        }

        try (FileWriter fileWriter = new FileWriter(filepath)) {
            fileWriter.write(jsonArray.toString(4)); // Indent with 4 spaces for better readability
            fileWriter.flush();
            System.out.println("Messages written to JSON file successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String extractReceivers(String[] to) {
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

    private String getTextContent(Object content) {
        String textContent = "";
        if (content instanceof MimeMessage) {
            MimeMessage mimeMessage = (MimeMessage) content;
            try {
                if (mimeMessage.isMimeType("text/plain")) {
                    textContent = (String) mimeMessage.getContent();
                } else if (mimeMessage.isMimeType("text/html")) {
                    textContent = (String) mimeMessage.getContent();
                } else if (mimeMessage.isMimeType("multipart/*")) {
                    Multipart multipart = (Multipart) mimeMessage.getContent();
                    for (int i = 0; i < multipart.getCount(); i++) {
                        BodyPart bodyPart = multipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            textContent = (String) bodyPart.getContent();
                            break;
                        } else if (bodyPart.isMimeType("text/html")) {
                            textContent = (String) bodyPart.getContent();
                            break;
                        }
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof Multipart) {
            Multipart multipart = (Multipart) content;
            try {
                for (int i = 0; i < multipart.getCount(); i++) {
                    BodyPart bodyPart = multipart.getBodyPart(i);
                    String contentType = bodyPart.getContentType();

                    // Check if the content type is text/plain or text/html
                    if (contentType.contains("text/plain")) {
                        textContent = (String) bodyPart.getContent();
                        break;
                    } else if (contentType.contains("text/html")) {
                        textContent = (String) bodyPart.getContent();
                        break;
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        } else if (content instanceof String) {
            textContent = (String) content;
        }
        return textContent;
    }

}
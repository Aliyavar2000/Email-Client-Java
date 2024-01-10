package mailclient.com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import mailclient.com.App;
import mailclient.com.EmailAPI.send.SendMessage;
import mailclient.com.connectionData.MessageData;
import mailclient.com.credentials.UserCredentials;
import mailclient.com.connectionData.ConnectionInfo;

public class SendController implements Initializable {

    @FXML
    private Button CancelSending;

    @FXML
    private TextField CcBox;

    @FXML
    private Button ContinueToSendSuccessfully;

    @FXML
    private TextField FromBox;

    @FXML
    private Button HomeButton;

    @FXML
    
    private TextArea MessageBox;

    @FXML
    private TextField SubjectBox;

    @FXML
    private TextField ToBox;

    @FXML
    private Button composeButton;

    @FXML
    private Button terminateBtn;

    @FXML
    private Button settingsButton;

    @FXML
    private Button syncButton;

    @FXML
    private AnchorPane AnchorSend;

    Stage stage;

    @FXML
    void switchToCompose(ActionEvent event) {
        try {
            App.setRoot("Send");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToSettings(ActionEvent event) {
        try {
            App.setRoot("SettingConfig");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void syncMessages(ActionEvent event) {
    }

    @FXML
    public void terminateApp(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Hambal! Are you sure you want to logout?");
        alert.setContentText("Press OK to continue");

        if (alert.showAndWait().get().equals(ButtonType.OK)) {
            stage = (Stage) AnchorSend.getScene().getWindow();
            stage.close();
        }
    }

    @FXML
    void backToHome(ActionEvent event) {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        FromBox.setPromptText(UserCredentials.getUserMail());
    }

    public void send(ActionEvent event) throws IOException {
        try {

            String to = ToBox.getText();
            // Error handling
            if (to.isEmpty() || to.contains("!") || to.contains(",") || to.contains(";") || to.contains(":")
                    || to.contains(" ") || !to.contains("@") || !to.contains(".")) {
                ToBox.clear();
                ToBox.setPromptText("Enter a valid recipient!");
                return;
            }

            String cc = CcBox.getText();
            // Error handling
            if (cc.isEmpty() || (cc.contains("!") || cc.contains(",") || cc.contains(";") || cc.contains(":")
                    || cc.contains(" ") || !cc.contains("@"))) {
                CcBox.clear();
                CcBox.setPromptText("Enter a valid recipient!");
                return;
            }

            String subject = SubjectBox.getText();
            // Error handling
            if (subject.isEmpty()) {
                SubjectBox.clear();
                SubjectBox.setPromptText("Enter a subject!");
                return;
            }

            String message = MessageBox.getText();
            // Error handling
            if (message.isEmpty()) {
                MessageBox.clear();
                MessageBox.setPromptText("Enter a message!");
                return;
            }

            // send
            MessageData messageData = new MessageData(to, cc, subject, message);
            String emailSubject = messageData.getEmailSubject();
            String emailText = messageData.getEmailText();
            SendMessage emailSender = new SendMessage(ConnectionInfo.getoutgoingHost(),
                    ConnectionInfo.getoutgoingPort(),
                    UserCredentials.getUserMail(), UserCredentials.getPassword(), messageData.getEmailReciever(),
                    messageData.getEmailCc());
            emailSender.sendEmail(emailSubject, emailText);

            System.out.println("Sent!");
            switchToSettings(event);

        } catch (Exception e) {
            System.out.println("Failed to send!");
        }
    }

    public String getFrom() {
        return FromBox.getText();
    }

    public String getTo() {
        return ToBox.getText();
    }

    public String getCc() {
        return CcBox.getText();
    }

    public String getSubject() {
        return SubjectBox.getText();
    }

    public String getMessage() {
        return MessageBox.getText();
    }
}

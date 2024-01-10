package mailclient.com.controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import mailclient.com.App;
import mailclient.com.connectionData.ConnectionInfo;

public class ServerSettingController {

    private String incomingProtocol;

    @FXML
    private Button cancelSetting;

    @FXML
    private Button continueToSetupProgress;

    @FXML
    private TextField incomingHostnameField;

    @FXML
    private TextField incomingPortField;

    @FXML
    private TextField outgoingHostnameField;

    @FXML
    private TextField outgoingPortField;

    @FXML
    private Button imapButton;

    @FXML
    private Button pop3Button;

    @FXML
    void pop3Selected(ActionEvent event) {
        pop3Button.setStyle("-fx-background-color: #3d848d");
        imapButton.setStyle("-fx-background-color: #2a9d8f");
        incomingProtocol = "POP3";
    }

    @FXML
    void imapSelected(ActionEvent event) {
        imapButton.setStyle("-fx-background-color: #3d848d");
        pop3Button.setStyle("-fx-background-color: #2a9d8f");
        incomingProtocol = "IMAP";
    }

    @FXML
    private void switchToSetupInProgress() throws IOException {
        String outgoingPort = outgoingPortField.getText();
        int outgoingPortInt = Integer.parseInt(outgoingPort);
        String outgoingHostname = outgoingHostnameField.getText();
        String incomingPort = incomingPortField.getText();
        int incomingPortInt = Integer.parseInt(incomingPort);
        String incomingHostname = incomingHostnameField.getText();
        if (incomingProtocol != "POP3" && incomingProtocol != "IMAP") {
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Please select a server type");
            alert.setContentText("Press OK to continue");
            alert.showAndWait();
        } else {
            ConnectionInfo.initialize(incomingHostname, incomingPortInt, incomingProtocol,
                    outgoingHostname, outgoingPortInt);
            App.setRoot("SetupInProgress");
        }

    }

    @FXML
    private void switchToLogin() throws IOException {
        App.setRoot("Login");
    }
}
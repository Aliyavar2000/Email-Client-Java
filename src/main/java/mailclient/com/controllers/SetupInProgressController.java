package mailclient.com.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.AnchorPane;
import mailclient.com.App;
import mailclient.com.EmailAPI.Connection;
import mailclient.com.EmailAPI.receive.ReceiveMessages;
import mailclient.com.EmailAPI.receive.model.ReceivedMessageModel;

public class SetupInProgressController implements Initializable {

    @FXML
    private ProgressBar progressBar;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private Label errorMessage;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        errorMessage.setText("Connecting to server...");
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                    Connection connection = App.buildEmailConnection();
                    updateProgress(0.2, 1.0);
                    Platform.runLater(() -> errorMessage.setText("Fetching messages..."));
                    List<ReceivedMessageModel> messages = App.fetchMessages(connection);
                    updateProgress(0.4, 1.0);
                    String filepath = "src/main/resources/mailclient/com/savedMessages/savedMessages.json";
                    updateProgress(0.6, 1.0);
                    if (messages != null) {
                        // Save received messages to json file
                        ReceiveMessages messageReceiver = new ReceiveMessages(connection.getStore());
                        Platform.runLater(() -> errorMessage.setText("Saving messages..."));
                        updateProgress(0.8, 1.0);
                        Platform.runLater(() -> errorMessage.setText("Finishing the setup..."));
                        messageReceiver.writeToJsonFile(messages, filepath);
                    }
                    updateProgress(1.0, 1.0);
                } catch (Exception e) {
                    e.printStackTrace();
                    updateProgress(0.0, 1.0);
                    Platform.runLater(() -> errorMessage.setText("Something went wrong. Please try again."));
                    throw new Exception();
                }

                return null;
            }
        };

        progressBar.progressProperty().bind(task.progressProperty());

        task.setOnSucceeded(event -> {
            switchToHomepage();
        });

        task.setOnFailed(event -> {
            errorMessage.setText("Something went wrong. Please try again.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                App.setRoot("ServerSetting");
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private void switchToHomepage() {
        try {
            App.setRoot("Homepage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

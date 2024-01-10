package mailclient.com.controllers;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import mailclient.com.App;
import mailclient.com.credentials.UserCredentials;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.event.ActionEvent;

public class LoginController {
    @FXML
    private TextField FullnameBox;
    @FXML
    private TextField EmailBox;
    @FXML
    private TextField UsernameBox;
    @FXML
    private TextField PasswordBox;
    @FXML
    private BorderPane LoginBorder;
    @FXML
    private Button cancelLogin;

    Stage stage;

    @FXML
    private void switchToServerSetting() throws IOException {
        App.setRoot("ServerSetting");
    }

    public void terminateApp(ActionEvent event) throws IOException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Logout");
        alert.setHeaderText("Hambal! Are you sure you want to logout?");
        alert.setContentText("Press OK to continue");

        if (alert.showAndWait().get().equals(ButtonType.OK)) {
            stage = (Stage) LoginBorder.getScene().getWindow();
            stage.close();
        }
    }

    public void continueBtn(ActionEvent event) throws IOException {
        try {
            String fullname = FullnameBox.getText();
            String email = EmailBox.getText();
            String username = UsernameBox.getText();
            String password = PasswordBox.getText();

            // Error handling
            if (fullname.isEmpty() || fullname.contains("!") || fullname.contains(",") || fullname.contains(";")
                    || fullname.contains(":") || fullname.contains("@") || fullname.contains(
                            ".")
                    || fullname.contains("#") || fullname.contains("$") || fullname.contains("%")
                    || fullname.contains("*")) {
                FullnameBox.clear();
                FullnameBox.setPromptText("Enter a valid name!");
                return;
            }

            // Error handling
            if (email.isEmpty() || email.contains("!") || email.contains(",") || email.contains(";")
                    || email.contains(":") || email.contains(" ") || !email.contains("@") || !email.contains(".")) {
                EmailBox.clear();
                EmailBox.setPromptText("Enter a valid email!");
                return;
            }

            // Error handling
            if (username.isEmpty() || username.contains("!") || username.contains(",") || username.contains(";")
                    || username.contains(":") || username.contains(" ")) {
                UsernameBox.clear();
                UsernameBox.setPromptText("Enter a valid username!");
                return;
            }
            // Error handling
            if (password.isEmpty() || password.contains(" ")) {
                PasswordBox.clear();
                PasswordBox.setPromptText("Enter a valid password!");
                return;
            }

            // Save user credentials
            UserCredentials.initialize(email, password, fullname, username);

            switchToServerSetting(); // switch to server setting page

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String getFullname() {
        return FullnameBox.getText();
    }

    public String getEmail() {
        return EmailBox.getText();
    }

    public String getUsername() {
        return UsernameBox.getText();
    }

    public String getPassword() {
        return PasswordBox.getText();
    }

}

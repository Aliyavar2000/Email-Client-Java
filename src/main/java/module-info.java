module mailclient.com {
    requires javafx.controls;
    requires javafx.fxml;
    requires transitive java.mail;
    requires org.json;
    requires transitive javafx.graphics;

    opens mailclient.com.controllers to javafx.fxml;

    opens mailclient.com to javafx.base, javafx.graphics;

    exports mailclient.com;

    exports mailclient.com.EmailAPI;

    exports mailclient.com.EmailAPI.receive.model;

    opens mailclient.com.models to javafx.base;

    opens mailclient.com.EmailAPI.receive to java.mail;

}

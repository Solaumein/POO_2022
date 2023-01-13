module ClavardageEntrePotes {
    //exports org.example.GUI;
    exports org.example.Controller;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens org.example;
    //opens org.example.GUI;
    exports org.example;
    exports org.example.Message;
    opens org.example.Message;
    exports org.example.Network;
    opens org.example.Network;
    exports org.example.User;
    opens org.example.User;
}
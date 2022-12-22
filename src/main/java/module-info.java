module ClavardageEntrePotes {
    exports org.example.GUI;
    exports org.example.Controller;
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens org.example;
    opens org.example.GUI;
}
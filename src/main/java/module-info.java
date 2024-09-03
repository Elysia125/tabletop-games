module org.example.tabletopgames {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens org.example.tabletopgames to javafx.fxml;
    exports org.example.tabletopgames;
}
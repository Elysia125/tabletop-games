module org.example.tabletopgames {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.tabletopgames to javafx.fxml;
    exports org.example.tabletopgames;
}
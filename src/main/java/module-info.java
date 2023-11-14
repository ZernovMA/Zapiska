module com.example.zapiska {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.zapiska to javafx.fxml;
    exports com.example.zapiska;
}
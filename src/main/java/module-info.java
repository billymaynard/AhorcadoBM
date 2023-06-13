module com.example.ahorcadobm {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens com.example.ahorcadobm to javafx.fxml;
    exports com.example.ahorcadobm;
}
package com.example.ahorcadobm;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.Objects;

public class MenuController {
    @FXML
    private Label title;
    @FXML
    private ComboBox<String> comboBox;
    @FXML
    private ImageView settings;
    @FXML
    private Button botonJugar;

    public void initialize(){
        Font newFont = Font.loadFont(MenuController.class.getResourceAsStream("style/myfont.ttf"),40);
        Platform.runLater(()->{
            title.setFont(newFont);
            title.getStyleClass().add("title");
            comboBox.getItems().setAll("Clasico","Custom");
            settings.setFitWidth(60);
            settings.setFitHeight(60);
        });
        settings.setOnMouseEntered(event -> {
            settings.setFitWidth(70);
            settings.setFitHeight(70);
            GridPane.setMargin(settings,new Insets(0,0,10,10));
        });
        settings.setOnMouseExited(event -> {
            settings.setFitWidth(60);
            settings.setFitHeight(60);
            GridPane.setMargin(settings,new Insets(0,0,15,15));
        });
        comboBox.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue) ->{
            if (newValue != null){
                botonJugar.setDisable(false);
            }else{
                botonJugar.setDisable(true);
            }
        });
        botonJugar.setOnMouseClicked((event) -> {
            try {
                FXMLLoader loader = new FXMLLoader(MenuController.class.getResource("player-select.fxml"));
                Scene scene = new Scene(loader.load());
                JugadorSelectController jugadorSelectController = loader.getController();
                jugadorSelectController.setDosJugadores(comboBox.getSelectionModel().getSelectedItem().equals("Custom"));
                Button temp = ((Button) event.getSource());
                Scene currentScene = temp.getScene();
                Stage currentStage = (Stage) currentScene.getWindow();
                currentStage.setScene(scene);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

}
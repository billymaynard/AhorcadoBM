package com.example.ahorcadobm;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.scene.control.Button;


public class WordSelectController {
    @FXML
    private Label jugadorQueElige;
    @FXML
    private Button botonElegir;
    @FXML
    private TableView<Palabra> tableView;
    @FXML
    private TableColumn<Palabra, String> palabraColumn;
    @FXML
    private TableColumn<Palabra, String> categoriaColumn;
    @FXML
    private TableColumn<Palabra, Integer> puntosColumn;
    private Palabra palabraElegida;
    private String ficheroPalabras;
    private Palabras words;
    public void initializer(){
        words = new Palabras(ficheroPalabras);
        palabraColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Palabra, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Palabra, String> cellData) {
                return new SimpleStringProperty(cellData.getValue().getPalabra());
            }
        });
        categoriaColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Palabra, String>, ObservableValue<String>>() {
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Palabra, String> cellData) {
                return new SimpleStringProperty(cellData.getValue().getCategoria());
            }
        });
        puntosColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Palabra, Integer>, ObservableValue<Integer>>() {
            public ObservableValue<Integer> call(TableColumn.CellDataFeatures<Palabra, Integer> cellData) {
                return new SimpleIntegerProperty(cellData.getValue().getPuntos()).asObject();
            }
        });
        ObservableList<Palabra> items = FXCollections.observableArrayList(words.getPalabras());
        tableView.setItems(items);
        tableView.getSelectionModel().selectedItemProperty().addListener((obs,oldValue,newValue) ->{
            if (newValue != null){
                botonElegir.setDisable(false);
            }else{
                botonElegir.setDisable(true);
            }
        });
        tableView.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && !botonElegir.isFocused()) {
                tableView.getSelectionModel().clearSelection();
            }
        });
        botonElegir.setOnAction(event -> {
            palabraElegida = tableView.getSelectionModel().getSelectedItem();
            Button temp = (Button) event.getSource();
            Scene estsEscena = temp.getScene();
            Stage currentStage = (Stage) estsEscena.getWindow();
            currentStage.hide();
        });
    }
    public Palabra getPalabraElegida(){
        return palabraElegida;
    }
    public void setDatos(String fichero, String nombreJugador){
        this.ficheroPalabras = fichero;
        this.jugadorQueElige.setText(nombreJugador+" tiene que elegir una palabra.");
        Font newFont = Font.loadFont(MenuController.class.getResourceAsStream("style/myfont.ttf"),40);
        this.jugadorQueElige.setFont(newFont);
    }
}

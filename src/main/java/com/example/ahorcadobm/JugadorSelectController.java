package com.example.ahorcadobm;

import javafx.animation.ScaleTransition;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class JugadorSelectController {

    @FXML
    private TableView<Jugador> tableView;

    @FXML
    private TableColumn<Jugador, String> nombreColumn;

    @FXML
    private TableColumn<Jugador, Integer> partidasJugadasColumn;

    @FXML
    private TableColumn<Jugador, Integer> partidasGanadasColumn;

    @FXML
    private TableColumn<Jugador, Integer> partidasPerdidasColumn;

    @FXML
    private TableColumn<Jugador, String> perfilColumn;

    @FXML
    private Button botonEligeJugador1;

    @FXML
    private Button botonEligeJugador2;

    @FXML
    private Button botonModificaJugador;

    @FXML
    private Button botonBorrarJugador;

    @FXML
    private Button botonaddJugador;

    @FXML
    private Button botonJugar;
    private Jugador jugador1;
    private Jugador jugador2;
    private boolean dosJugadores = true;
    private int jugadoresSelecionados=0;

    private ObservableList<Jugador> jugadores;
    private ListaJugadores listaJugadores;

    public void initialize() {
        // Create sample players
        listaJugadores = new ListaJugadores();
        jugadores = FXCollections.observableArrayList(listaJugadores.getLista());
        nombreColumn.getStyleClass().add("centerLeft");
        partidasJugadasColumn.getStyleClass().add("centerLeft");
        partidasGanadasColumn.getStyleClass().add("centerLeft");
        partidasPerdidasColumn.getStyleClass().add("centerLeft");
        perfilColumn.getStyleClass().add("centerContent");
        nombreColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));
        partidasJugadasColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getPartidasJugadas()).asObject());
        partidasGanadasColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getVictorias()).asObject());
        partidasPerdidasColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getDerrotas()).asObject());
        perfilColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPerfil()));
        perfilColumn.setCellFactory(columna -> {
            return new TableCell<Jugador,String>(){
                private ImageView imageView = new ImageView();
                @Override
                protected void updateItem(String item,boolean empty){
                    super.updateItem(item,empty);
                    if (item == null || empty){
                        setGraphic(null);
                    }else{
                        Image image = new Image(JugadorSelectController.class.getResourceAsStream("images/perfiles/"+item),40,40,true,true);
                        imageView.setImage(image);
                        setGraphic(imageView);
                    }
                }
            };

        });
        tableView.setItems(jugadores);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(1.5), botonJugar);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(1.2);
        scaleTransition.setToY(1.2);
        scaleTransition.setCycleCount(ScaleTransition.INDEFINITE);
        scaleTransition.setAutoReverse(true);

        tableView.getSelectionModel().selectedIndexProperty().addListener(
                (var, oldValue, newValue) -> {
                    if (newValue.intValue()==-1){
                        botonModificaJugador.setDisable(true);
                        botonBorrarJugador.setDisable(true);
                        botonEligeJugador1.setDisable(true);
                        botonEligeJugador2.setDisable(true);
                    }else{
                        botonModificaJugador.setDisable(false);
                        botonBorrarJugador.setDisable(false);
                        if (jugador1==null){
                            botonEligeJugador1.setDisable(false);
                        }
                        if (jugador2==null){
                            botonEligeJugador2.setDisable(false);
                        }
                    }
        }
        );
        botonEligeJugador1.setOnAction(event -> {
            jugador1 = tableView.getSelectionModel().getSelectedItem();
            if (jugador1 != null) {
                jugadores.remove(jugador1);
                tableView.setItems(jugadores);
                botonEligeJugador1.setDisable(true);
                jugadoresSelecionados++;
                if (!this.dosJugadores||jugadoresSelecionados==2){
                    scaleTransition.play();
                    botonJugar.setDisable(false);
                }
            }
        });

        botonEligeJugador2.setOnAction(event -> {
            jugador2 = tableView.getSelectionModel().getSelectedItem();
            if (jugador2 != null) {
                jugadores.remove(jugador2);
                tableView.setItems(jugadores);
                botonEligeJugador2.setDisable(true);
                jugadoresSelecionados++;
                if (!this.dosJugadores||jugadoresSelecionados==2){
                    scaleTransition.play();
                    botonJugar.setDisable(false);
                }
            }
        });
        botonaddJugador.setOnAction(event -> {
            // Open a dialog or perform an action to add a new player
        });
        botonJugar.setOnAction(event -> {
            Jugador[] jugadores;
            if (jugador1==null) {
                jugadores = new Jugador[]{jugador2};
            }else if (jugador2==null){
                jugadores = new Jugador[]{jugador1};
            }else{
                jugadores = new Jugador[]{jugador1, jugador2};
                List<Jugador> jugadorList = Arrays.asList(jugadores);
                Collections.shuffle(jugadorList);
                jugadores = jugadorList.toArray(new Jugador[2]);
            }
            try {
                FXMLLoader loader = new FXMLLoader(JugadorSelectController.class.getResource("game.fxml"));
                Scene scene = new Scene(loader.load());
                GameController gameController = loader.getController();
                Button temp = ((Button) event.getSource());
                Scene currentScene = temp.getScene();
                Stage currentStage = (Stage) currentScene.getWindow();
                currentStage.setScene(scene);
                gameController.setValoresDeJuego(!dosJugadores,jugadores);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
    public void setDosJugadores(boolean valor){
        this.dosJugadores=valor;
    }
}

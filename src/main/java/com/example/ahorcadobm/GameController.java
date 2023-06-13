package com.example.ahorcadobm;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javafx.event.ActionEvent;
import java.io.File;
import java.io.IOException;

public class GameController {
    @FXML
    private Label title;
    @FXML
    private Label word;
    @FXML
    private Label turnText;
    @FXML
    private ImageView hangMan;
    @FXML
    private Pane hiddenPane;
    @FXML
    private ImageView gameOver;
    @FXML
    private Label winnerLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Button volverButton;
    @FXML
    private Button nuevaPartidaButton;

    @FXML
    public void pulsarBotonLetra(ActionEvent e){
        Button button = (Button) e.getSource();
        button.setDisable(true);
        String letra = button.getText();
        if (juegoEnCurso.play(letra.toLowerCase())){
            button.getStyleClass().add("usedgood");
            juegoEnCurso.updatePantalla();
            try {
                if (!juegoEnCurso.comprobarGanar()){
                    placeaudioInputStream = AudioSystem.getAudioInputStream(new File(GameController.class.getResource("sounds/correct.wav").toURI()));
                }else{
                    placeaudioInputStream = AudioSystem.getAudioInputStream(new File(GameController.class.getResource("sounds/win.wav").toURI()));
                    isgameVictory=true;
                }
                clip = AudioSystem.getClip();
                clip.open(placeaudioInputStream);
                clip.start();
                if (juegoEnCurso.comprobarGanar()) finDeJuego();
            } catch (Exception ignored) {}
        }else {
            button.getStyleClass().add("usedbad");
            avanzaAhorca();
            if (contadorErrores==6){
                isgameVictory=false;
                finDeJuego();
            }else{
                juegoEnCurso.swapPlayer();
            }
        }
    }
    private AudioInputStream placeaudioInputStream;
    private Ahorcado juegoEnCurso;
    private Clip clip;
    private Palabra palabra;
    int prepCounter;
    private int contadorErrores;
    private boolean isgameVictory;
    private boolean isClasico;
    private Jugador[] jugadores;
    private String[] hangManImages={
            "fase1.png",
            "fase2.png",
            "fase3.png",
            "fase4.png",
            "error1.png",
            "error2.png",
            "error3.png",
            "error4.png",
            "error5.png",
            "error6.png"
    };
    public void initialize(){
        contadorErrores=0;
        Font newFont = Font.loadFont(MenuController.class.getResourceAsStream("style/myfont.ttf"),50);
        Font playerFont = Font.loadFont(MenuController.class.getResourceAsStream("style/myfont.ttf"),35);
        Platform.runLater(()->{
            title.setFont(newFont);
            title.getStyleClass().add("title");
            word.setFont(playerFont);
            word.getStyleClass().add("word");
            turnText.setFont(playerFont);
            turnText.getStyleClass().add("title");
            winnerLabel.setFont(newFont);
            nameLabel.setFont(newFont);
            hangMan.setImage(new Image(GameController.class.getResourceAsStream("images/Ahorca/"+hangManImages[0])));
        });
        volverButton.setOnAction(event -> {
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("menu.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Button temp = (Button) event.getSource();
                Scene x = temp.getScene();
                Stage stage = (Stage) x.getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        nuevaPartidaButton.setOnAction(event -> {
            Button temp = (Button) event.getSource();
            Scene x = temp.getScene();
            Stage stage = (Stage) x.getWindow();
            launchIdentical(stage);
        });
    }
    private void prepImage(){
        prepCounter=0;
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(800), event -> {
            prepCounter++;
            cambiarFoto(prepCounter);
        }));
        timeline.setCycleCount(3);
        timeline.play();
    }
    private void avanzaAhorca(){
        contadorErrores++;
        cambiarFoto(contadorErrores+3);
    }
    private void cambiarFoto(int valor){
        hangMan.setImage(new Image(GameController.class.getResourceAsStream("images/Ahorca/"+hangManImages[valor])));
        try {
            if (valor<4){
                placeaudioInputStream = AudioSystem.getAudioInputStream(new File(GameController.class.getResource("sounds/place.wav").toURI()));
            }else if (valor<9){
                placeaudioInputStream = AudioSystem.getAudioInputStream(new File(GameController.class.getResource("sounds/mistake.wav").toURI()));
            }else{
                placeaudioInputStream = AudioSystem.getAudioInputStream(new File(GameController.class.getResource("sounds/lose.wav").toURI()));
            }
            clip = AudioSystem.getClip();
            clip.open(placeaudioInputStream);
            clip.start();
        } catch (Exception ignored) {}
    }
    public void setPlayer(String nombreJugador){
        turnText.setText("turno de: "+nombreJugador);
    }
    public void setValoresDeJuego(boolean esClasico, Jugador[] jugadores) throws IOException {
        this.isClasico=esClasico;
        this.jugadores=jugadores;
        new Ajustes();
        if (!esClasico){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("word-select.fxml"));
            Scene scene = new Scene(loader.load());
            WordSelectController wordSelectController = loader.getController();
            wordSelectController.setDatos(Ajustes.getFicheroDePalabras(),jugadores[0].getNombre());
            wordSelectController.initializer();
            Stage selectPalabra = new Stage();
            selectPalabra.setScene(scene);
            selectPalabra.initModality(Modality.APPLICATION_MODAL);
            selectPalabra.showAndWait();
            palabra=wordSelectController.getPalabraElegida();
            if (palabra==null)System.exit(0);
            juegoEnCurso = new Ahorcado(esClasico, jugadores[1], palabra);
        }else{
            new Ajustes();
            Palabras palabras = new Palabras(Ajustes.getFicheroDePalabras());
            palabra = palabras.getPalabraRandom();
            juegoEnCurso = new Ahorcado(esClasico,jugadores,jugadores[0],palabra);
        }
        juegoEnCurso.setController(this);
        juegoEnCurso.updatePantalla();
        try {
            placeaudioInputStream = AudioSystem.getAudioInputStream(new File(GameController.class.getResource("sounds/place.wav").toURI()));
            clip = AudioSystem.getClip();
            clip.open(placeaudioInputStream);
            clip.start();
        } catch (Exception ignored){}
        prepImage();
    }
    public void setWord(String aDescubrir){
        word.setText(aDescubrir+"     Categoria: "+palabra.getCategoria());
    }
    private void finDeJuego(){
        juegoEnCurso.actualizarDatosJugadores();
        juegoEnCurso.guardarPartida();
        hiddenPane.setVisible(true);
        if (juegoEnCurso.comprobarGanar()){
            winnerLabel.setVisible(true);
            nameLabel.setVisible(true);
            nameLabel.setText(juegoEnCurso.getCurrentPlayerName());
        }else{
            gameOver.setVisible(true);
        }
    }
    public boolean isgameVictory(){
        return isgameVictory;
    }
    private void launchIdentical(Stage stage){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("game.fxml"));
            Scene newScene = new Scene(loader.load());
            GameController gameController = loader.getController();
            stage.setScene(newScene);
            if (!isClasico){
                Jugador temp = jugadores[0];
                jugadores[0] = jugadores[1];
                jugadores[1] = temp;
            }else {
                if (jugadores.length!=1){
                    if (isgameVictory){
                        if (jugadores[0]!=juegoEnCurso.getCurrentPlayer()){
                            Jugador temp = jugadores[0];
                            jugadores[0] = juegoEnCurso.getCurrentPlayer();
                            jugadores[1] = temp;
                        }
                    }
                }
            }
            gameController.setValoresDeJuego(this.isClasico,this.jugadores);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

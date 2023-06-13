package com.example.ahorcadobm;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Clase que controla la partida y otros elementos.
 * @author william
 */
public class Ahorcado {
    boolean isclasico;
    private Jugador[] jugadores;
    private Palabra word;
    private Jugador jugadoractivo;
    private int fallos=0;
    private String mostrarEnPantalla;
    private GameController gameController;
    public Ahorcado(boolean isclasico, Jugador[] jugadores, Jugador jugador, Palabra word){
        this.isclasico=isclasico;
        this.jugadores=jugadores;
        this.jugadoractivo=jugador;
        this.word=word;
        buildMostrarEnPantalla();
        jugadores[0].setHasPlayed(false);
        if (jugadores.length!=1){
            jugadores[1].setHasPlayed(false);
        }
        jugador.setHasPlayed(false);
    }
    public Ahorcado(boolean isclasico, Jugador jugador, Palabra word){
        this.isclasico=isclasico;
        jugadoractivo=jugador;
        this.word=word;
        buildMostrarEnPantalla();
    }
    public void buildMostrarEnPantalla(){
        StringBuilder temp = new StringBuilder();
        for (int i=0;i<(word.getPalabraLength()-1);i++){
            if (word.getPalabra().charAt(i)!=' '){
                temp.append("_ ");
            }else {
                temp.append("   ");
            }
        }
        temp.append("_");
        mostrarEnPantalla = temp.toString();
    }
    public void setController(GameController controller){
        this.gameController=controller;
    }
    /**
     * Método que lanza un modo de juego o otro dependiendo de la variable clasico.
     * Esta variable se fijó en el método local: "selectjugadoresymodo".
     */
    public boolean play(String letra){
        jugadoractivo.setHasPlayed(true);
        if (isclasico){
            return playclassic(letra);
        }else {
            return playcustom(letra);
        }
    }
    /**
     * Método para jugar modo clasico.
     * Hay dos jugadores, uno propone una palabra de una lista donde se le ofrece la opción de añadir uno, y el otro
     * intentará adivinar la palabra
     */
    public boolean playclassic(String letra){
        if (updateMostrarEnPantalla(letra)){
            return true;
        }else {
            return false;
        }
    }
    public boolean comprobarGanar(){
        for(int i = 0; i<mostrarEnPantalla.length(); i++){
            char bar = '_';
            if (mostrarEnPantalla.charAt(i)==bar){
                return false;
            }
        }
        return true;
    }
    private boolean updateMostrarEnPantalla(String letra){
        char letraa=letra.charAt(0);
        int counter = 0;
        boolean encontrado = false;
        StringBuilder updated = new StringBuilder(mostrarEnPantalla);
        for (int i = 0; i < word.getPalabraLength();i++){
            if (word.getPalabra().toLowerCase().charAt(i)==letraa){
                updated.setCharAt((counter),letraa);
                encontrado = true;
            }
            counter=counter+2;
            if (word.getPalabra().toLowerCase().charAt(i)==' '){
                counter=counter+1;
            }
        }
        this.mostrarEnPantalla=updated.toString();
        return encontrado;
    }
    public void updatePantalla(){
        gameController.setWord(mostrarEnPantalla);
        gameController.setPlayer(jugadoractivo.getNombre());
    }

    /**
     * Método para jugar Ahorcado en modo especial.
     */
    public boolean playcustom(String letra){
        if (updateMostrarEnPantalla(letra)){
            return true;
        }else {
            return false;
        }
    }
    public void swapPlayer(){
        if (jugadores!=null && jugadores.length!=1){
            if (Objects.equals(jugadoractivo.getNombre(), jugadores[0].getNombre())){
                jugadores[0]=jugadoractivo;
                jugadoractivo=jugadores[1];
            }else {
                jugadores[1]=jugadoractivo;
                jugadoractivo=jugadores[0];
            }
        }
        gameController.setPlayer(jugadoractivo.getNombre());
    }
    public void actualizarDatosJugadores(){
        ListaJugadores listaJugadores = new ListaJugadores();
        if (gameController.isgameVictory()){
            int winner;
            if (jugadores!=null && jugadores.length!=1){
                if (jugadores[0].getNombre()==jugadoractivo.getNombre()){
                    winner=0;
                }else{
                    winner=1;
                }
                int altPlayer = (winner==0) ? 1 : 0;
                jugadores[winner].addPartidasJugadas();
                jugadores[winner].addVictorias();
                jugadores[winner].addPuntuacion(word.getPuntos());
                listaJugadores.buscaYActualizarJugador(jugadores[winner]);
                if (jugadores[altPlayer].isHasPlayed()){
                    jugadores[altPlayer].addPartidasJugadas();
                    jugadores[altPlayer].addDerrotas();
                    listaJugadores.buscaYActualizarJugador(jugadores[altPlayer]);
                }
            }else {
                jugadoractivo.addVictorias();
                jugadoractivo.addPartidasJugadas();
                jugadoractivo.addPuntuacion(word.getPuntos());
                listaJugadores.buscaYActualizarJugador(jugadoractivo);
            }
        }else {
            int loser;
            if (jugadores!=null && jugadores.length!=1){
                if (jugadores[0].getNombre()==jugadoractivo.getNombre()){
                    loser=0;
                }else{
                    loser=1;
                }
                int altPlayer = (loser==0) ? 1 : 0;
                jugadores[loser].addPartidasJugadas();
                jugadores[loser].addDerrotas();
                listaJugadores.buscaYActualizarJugador(jugadores[loser]);
                if (jugadores[altPlayer].isHasPlayed()){
                    jugadores[altPlayer].addPartidasJugadas();
                    jugadores[altPlayer].addDerrotas();
                    listaJugadores.buscaYActualizarJugador(jugadores[altPlayer]);
                }
            }else {
                jugadoractivo.addDerrotas();
                jugadoractivo.addPartidasJugadas();
                listaJugadores.buscaYActualizarJugador(jugadoractivo);
            }
        }
        listaJugadores.actualizajugadores();
    }
    public void guardarPartida(){
        String player1;
        String player2;
        String winnerr;
        if (jugadores!=null && jugadores.length!=1){
            player1=jugadores[0].getNombre();
            player2=jugadores[1].getNombre();
        }else{
            player1=jugadoractivo.getNombre();
            player2=null;
        }
        if (gameController.isgameVictory()){
            winnerr=jugadoractivo.getNombre();
        }else {
            winnerr=null;
        }
        Partida estaPartida = new Partida(isclasico, player1, player2, word.getPalabra(), word.getPuntos() ,winnerr);
        Partida.appendToFile(estaPartida.toString());
    }
    public String getCurrentPlayerName(){
        return jugadoractivo.getNombre();
    }
    public Jugador getCurrentPlayer(){
        return jugadoractivo;
    }
}


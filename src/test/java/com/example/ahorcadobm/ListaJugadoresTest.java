package com.example.ahorcadobm;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ListaJugadoresTest {

    @Test
    public void Test1(){
        ListaJugadores listaJugadores = new ListaJugadores();
        ArrayList<Jugador> array = listaJugadores.getLista();
        for (Jugador x:
             array) {
            System.out.println(x.toString());
            System.out.println();
        }
        Jugador temp = array.get(2);
        temp.addVictorias();
        listaJugadores.buscaYActualizarJugador(temp);
        array = listaJugadores.getLista();
        for (Jugador x:
                array) {
            System.out.println(x.toString());
            System.out.println();
        }
    }
    @Test
    public void Test2(){
        ListaJugadores listaJugadores = new ListaJugadores();
        ArrayList<Jugador> array = listaJugadores.getLista();
        for (Jugador x:
                array) {
            System.out.println(x.toString());
            System.out.println();
        }
        Jugador temp = array.get(2);
        temp.addVictorias();
        listaJugadores.buscaYActualizarJugador(temp);
        listaJugadores.actualizajugadores();
        listaJugadores = new ListaJugadores();
        array = listaJugadores.getLista();
        for (Jugador x:
                array) {
            System.out.println(x.toString());
            System.out.println();
        }
    }
}
package com.example.ahorcadobm;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

import exeptions.*;

/**
 * Clase que guarda un objeto lista que guarda los jugadores grabados de la hoja Jugadores.txt y los
 * que actualmente están en juego (los jugadores en juego se guardan en forma de nodo para poder fácilmente acceder
 * al siguiente de la lista.)
 * @author william
 */
public class ListaJugadores {
    private ArrayList<Jugador> jugadores;

    /**
     * Constructor que saca los jugadores actualmente grabados en la hoja Jugadores.txt y añade sus datos a un Objeto
     * Jugador. Se utilizan rutas relativas que solo funcionan con la ejecución dentro de un IDE (no funciona en cmd y
     * no se ha probado en otros IDE aparte de Intellij Idea)
     */
    ListaJugadores(){
        this.jugadores=new ArrayList<>();
        try {
            //Utilizamos Buffered Reader y Writer por comodidad.
            URL resourceUrl = ListaJugadores.class.getResource("datos/jugadores.txt");
            String filePath = resourceUrl.toURI().getPath();
            BufferedReader f = new BufferedReader(new FileReader(filePath));

            String currentline = f.readLine();
            int nline=1;

            //Bucle que sigue hasta llegar al final del archivo.
            while (currentline != null){
                String[] linea = currentline.split(";");
                if(linea.length!=6){
                    throw new InvalidFileContent("Error reading data on line: "+nline);
                }

                //Asignamos los datos de la linea a una serie de variables.
                String name = linea[0];
                String jugadas = linea[1];
                String victorias = linea[2];
                String derrotas = linea[3];
                String score = linea[4];
                String perfil = linea[5];

                //Creamos un objeto jugador con esos datos mediante el método "addplayer".
                addplayer(name, Integer.parseInt(jugadas), Integer.parseInt(victorias),Integer.parseInt(derrotas),Integer.parseInt(score), perfil);
                currentline=f.readLine();
                nline++;
            }
            f.close();

        } catch (IOException | InvalidFileContent | URISyntaxException e) {
            System.out.println("Error finding or reading file... closing program.");
            System.exit(1);
        }
    }

    /**
     * Método encargado de actualizar el fichero de jugadores
     */
    public void actualizajugadores(){

        //Los ficheros se actualizan de la siguiente manera: Primero creamos un reader sobre el archivo existente.
        //Luego generamos un archivo nuevo con el mismo nombre, pero con la extension .tmp, este archivo esta vació por ahora,
        //Vamos línea por línea comprobando si debemos escribir los datos que ya están en Jugadores.txt o datos nuevos.
        //Una vez llegamos al final del archivo borramos el viejo y cambiamos el nombre al nuevo para llamarse como el original.
        try {
            String filetargetLocation = ListaJugadores.class.getResource("datos/jugadores.txt").getPath();
            String temp[] = filetargetLocation.split("target/classes/");
            String realFile = temp[0]+"src/main/resources/"+temp[1];
            realFile=realFile.replace("%20"," ");
            BufferedWriter fw = new BufferedWriter(new FileWriter(realFile+".tmp"));
            //Bucle que recorre la hoja Jugadores.txt entera.
            for (Jugador x:
                    jugadores) {
                fw.write(x.getNombre()+";"+x.getPartidasJugadas()+";"+x.getVictorias()+";"+x.getDerrotas()+";"+x.getPuntuacion()+";"+x.getPerfil()+System.lineSeparator());
            }
            fw.close();
            File oldFile = new File(realFile);
            if (oldFile.delete()){
                File newFile = new File(realFile+".tmp");
                if (!newFile.renameTo(oldFile)){
                    throw new CannotReNameFile("No se ha podido renombrar el archivo");
                }
            }else {
                throw new UnableToDeleteFile("No se ha podido borrar el archivo");
            }
        }catch (Exception e){
            System.out.printf(e.getMessage());
        }
    }
    /**
     * Método que crea el objeto jugador (usado para añadir los jugadores sacados del archivo al arraylist)
     * @param nombre Codigo del jugador (String)
     * @param puntuacion Puntuación del jugador (int)
     * @param victorias Victorias que tiene el jugador (int)
     * @param derrotas Derrotas que tiene el jugador (int)
     */
    public void addplayer(String nombre, int jugadas, int victorias, int derrotas, int puntuacion, String perfil){
        Jugador temp = new Jugador(nombre, jugadas , victorias, derrotas, puntuacion, perfil);
        this.jugadores.add(temp);
    }
    public void buscaYActualizarJugador(Jugador jugador) {
        for (int i = 0; i < jugadores.size(); i++) {
            Jugador x = jugadores.get(i);
            if (x.getNombre().equals(jugador.getNombre())) {
                jugadores.set(i, jugador);
                break;
            }
        }
    }
    public ArrayList<Jugador> getLista(){
        return this.jugadores;
    }


}

/**
 * Clase que contiene el objeto Jugador, usado para guardar ordenadamente los datos de los jugadores.
 * @author william
 */
class Jugador{
    private int puntuacion;
    private String nombre;
    private int victorias;
    private int derrotas;
    private int partidasJugadas;
    private String perfil;
    private boolean hasPlayed=false;

    /**
     * Constructor del objeto Jugador recibe como entrada un código, puntuación, número de victorias y número de derrotas.
     * El nombre del jugador se saca a base del código.
     * @param nombre Código de jugador.
     * @param puntuacion Puntuación de jugador.
     * @param victorias Victorias del Jugador.
     * @param derrotas Derrotas del Jugador.
     */
    Jugador(String nombre, int partidasJugadas, int victorias, int derrotas, int puntuacion, String perfil){
        this.nombre = nombre;
        this.puntuacion =puntuacion;
        this.victorias =victorias;
        this.derrotas =derrotas;
        this.partidasJugadas=partidasJugadas;
        this.perfil=perfil;
    }
    /**
     * Getter de la variable nombre.
     * @return Devuelve el nombre.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Getter de la puntuación del jugador.
     * @return Devuelve la puntuación.
     */
    public int getPuntuacion() {
        return puntuacion;
    }

    /**
     * Adder que suma una cantidad especificada a la puntuación del jugador.
     * @param puntuacion Puntuación a sumar.
     */
    public void addPuntuacion(int puntuacion) {
        this.puntuacion = this.puntuacion + puntuacion;
    }
    public int getPartidasJugadas(){
        return this.partidasJugadas;
    }
    public void addPartidasJugadas(){
        this.partidasJugadas++;
    }


    /**
     * Getter de las victorias de un jugador.
     * @return Devuelve las victorias que tiene el jugador en cuestión.
     */
    public int getVictorias() {
        return victorias;
    }

    /**
     * Adder de Victorias del jugador, suma 1 a las victorias actuales del jugador en cuestión.
     */
    public void addVictorias(){
        this.victorias++;
    }

    /**
     * Getter de las Derrotas de un jugador.
     * @return Devuelve las Derrotas del jugador en cuestión.
     */
    public int getDerrotas() {
        return derrotas;
    }
    public String getPerfil(){
        return perfil;
    }
    public void setHasPlayed(boolean value){
        this.hasPlayed=value;
    }
    public boolean isHasPlayed(){
        return hasPlayed;
    }

    /**
     * Adder de Derrotas de un jugador, suma 1 a las derrotas actuales del jugador en cuestión.
     */
    public void addDerrotas(){
        this.derrotas++;
    }
    @Override
    public String toString(){
        return "Name: " + getNombre() +
                " Partidas Jugadas: " + getPartidasJugadas() +
                " Partidas Ganadas: " + getVictorias() +
                " Partidas Perdidas: " + getDerrotas() +
                " Puntos: " + getPuntuacion() +
                " Perfil: " + getPerfil();
    }
}

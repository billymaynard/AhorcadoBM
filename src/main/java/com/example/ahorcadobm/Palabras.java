package com.example.ahorcadobm;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class Palabras{
    private ArrayList<Palabra> palabras = new ArrayList<>();
    public Palabras(String archivoDePalabras){
        int cantidadDeLineas=0;
        try {
            BufferedReader f = new BufferedReader(new FileReader(archivoDePalabras));
            String currentline = f.readLine();
            while (currentline!=null){
                String[] temp = currentline.split("-");
                temp[0].toLowerCase();
                Palabra palabra = new Palabra(temp[0],temp[1]);
                palabras.add(palabra);
                cantidadDeLineas++;
                currentline=f.readLine();
            }
            f.close();
            if (cantidadDeLineas==0){
                throw new IOException();
            }
        } catch (IOException e) {
            System.out.println("Error finding or reading file... closing program.");
            System.exit(1);
        }
    }
    public Palabra getPalabraRandom(){
        if (palabras.isEmpty()) {
            return null;
        }
        Random random = new Random();
        int index = random.nextInt(palabras.size());
        return palabras.get(index);
    }
    public ArrayList<Palabra> getPalabras() {
        return palabras;
    }
}
class Palabra{
    private String palabra;
    private String categoria;
    private int puntos;
    public Palabra(String palabra, String categoria){
        this.palabra=palabra;
        this.categoria=categoria;
        puntos = palabra.length()/2;
    }
    public String getPalabra() {
        return palabra;
    }
    public String getCategoria(){
        return categoria;
    }
    public int getPuntos() {
        return puntos;
    }
    public int getPalabraLength(){
        return palabra.length();
    }
}

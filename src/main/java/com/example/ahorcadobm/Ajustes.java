package com.example.ahorcadobm;

public class Ajustes {
    private static String ficheroDePalabras;
    public Ajustes(String ficheroDePalabras){
        Ajustes.ficheroDePalabras = ficheroDePalabras;
    }
    public Ajustes(){
        String fileTargetLocation = getClass().getResource("datos/palabras.txt").getPath();
        String temp[] = fileTargetLocation.split("target/classes/");
        String realFile = temp[0]+"src/main/resources/"+temp[1];
        Ajustes.ficheroDePalabras=realFile.replace("%20"," ");
    }
    public static String getFicheroDePalabras(){
        return ficheroDePalabras;
    }
    public static void setFicheroDePalabras(String ruta){
        Ajustes.ficheroDePalabras=ruta;
    }
}

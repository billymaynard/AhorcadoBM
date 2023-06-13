package com.example.ahorcadobm;

import exeptions.CannotReNameFile;
import exeptions.UnableToDeleteFile;
import javafx.scene.control.Button;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Objects;

public class Partida {
    private String player1;
    private String player2;
    private String word;
    private int points;
    private String winner;
    private boolean isClasic;

    public Partida(boolean isClasic, String player1, String player2, String word, int points, String winner) {
        this.isClasic = isClasic;
        this.player1 = player1;
        this.player2 = player2;
        this.word = word;
        this.points = points;
        this.winner = winner;
    }
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        if (isClasic){
            stringBuilder.append("Classic#$#");
        }else {
            stringBuilder.append("Custom#$#");
        }
        if (Objects.equals(player1, winner)){
            stringBuilder.append("W#$#").append(player1);
        }else {
            stringBuilder.append("L#$#").append(player1);
        }
        if (player2!=null){
            if (Objects.equals(player2, winner)){
                stringBuilder.append("#$#W#$#").append(player2);
            }else {
                stringBuilder.append("#$#L#$#").append(player2);
            }
        }
        stringBuilder.append("#$#").append(word);
        stringBuilder.append("#$#").append(points);
        return stringBuilder.toString();
    }
    static void appendToFile(String partida) {
        try{
            String filetargetLocation = ListaJugadores.class.getResource("datos/partidas.txt").getPath();
            String temp[] = filetargetLocation.split("target/classes/");
            String realFile = temp[0]+"src/main/resources/"+temp[1];
            realFile=realFile.replace("%20"," ");
            BufferedWriter fw = new BufferedWriter(new FileWriter(realFile+".tmp"));
            BufferedReader fr = new BufferedReader(new FileReader(realFile));
            int lineCounter = 0;
            String currentLine;
            currentLine=fr.readLine();
            while (currentLine!=null){
                fw.write(currentLine+System.lineSeparator());
                lineCounter++;
                currentLine=fr.readLine();
            }
            fw.write(lineCounter +"#$#"+partida);
            fr.close();
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
        } catch (IOException e) {
            System.out.println("An error occurred while appending to the file: " + e.getMessage());
        } catch (UnableToDeleteFile e) {
            throw new RuntimeException(e);
        } catch (CannotReNameFile e) {
            throw new RuntimeException(e);
        }
    }
}


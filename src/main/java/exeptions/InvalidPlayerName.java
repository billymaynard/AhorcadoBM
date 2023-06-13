package exeptions;

public class InvalidPlayerName extends Exception{
    /**
     * Excepción para cuando se busca un jugador no existente
     * @param error Un string que guarda el mensaje de error adecuado
     */
    public InvalidPlayerName(String error){
        super(error);
    }
}

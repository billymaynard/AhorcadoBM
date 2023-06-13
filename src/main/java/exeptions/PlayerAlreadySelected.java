package exeptions;

public class PlayerAlreadySelected extends Exception{
    /**
     * Excepción para cuando se intenta seleccionar un jugador ya en juego.
     * @param error Un string que guarda el mensaje de error adecuado
     */
    public PlayerAlreadySelected(String error){
        super(error);
    }
}

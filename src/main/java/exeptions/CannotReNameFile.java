package exeptions;

public class CannotReNameFile extends Exception{
    /**
     * Excepción para cuando no se pueda renombrar un archivo
     * @param error Un string que guarda el mensaje de error adecuado
     */
    public CannotReNameFile(String error){
        super(error);
    }
}

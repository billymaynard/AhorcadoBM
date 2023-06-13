package exeptions;

public class UnableToDeleteFile extends Exception{
    /**
     * Excepción para cuando no se pueda borrar un archivo.
     * @param error Un string que guarda el mensaje de error adecuado
     */
    public UnableToDeleteFile(String error){
        super(error);
    }
}

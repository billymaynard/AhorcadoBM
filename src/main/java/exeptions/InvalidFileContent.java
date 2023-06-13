package exeptions;

public class InvalidFileContent extends Exception{
    /**
     * Excepción para cuando el archivo contiene datos inesperados.
     * @param error Un string que guarda el mensaje de error adecuado
     */
    public InvalidFileContent(String error){
        super(error);
    }
}

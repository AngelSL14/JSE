package us.gonet.nabhi.jse.codesatm;

public enum SIUError {

    INVALID_PORT( -801L, "El intento de habilitar o deshabilitar eventos en un puerto que no existe" ),
    SYNTAX( -802L, "El comando fue invocado con datos de entrada incorrectos" ),
    PORT_ERROR( -803L, "Se produjo un error de xfs al ejecutar el comando" ),
    POWERSAVETOOSHORT( -804L, "El modo de ahorro de energía no se ha activado porque el dispositivo no puede reanudar desde el modo de ahorro de energía dentro del valor especificado" );

    private final long value;
    private final String message;

    SIUError( final long value, String message ) {
        this.value = value;
        this.message = message;
    }

    public String getMessageByCode( long code ) {
        for ( SIUError d : values() ) {
            if ( d.getValue() == code ) {
                return d.message;
            }
        }
        return "Unkown error!";
    }

    public long getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}

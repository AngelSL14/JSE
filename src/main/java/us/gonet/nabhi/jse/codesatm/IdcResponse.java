package us.gonet.nabhi.jse.codesatm;


public enum IdcResponse implements AtmResponse {

    /*
     * @since v3.00
     */
    MEDIAJAM( -200, "Se encuentra atrapada una tarjeta dentro de la lectora", "-2" ),
    /*
     * @since v3.00
     */
    NOMEDIA( -201, "Se ejecutó el comando de eyectar sin ninguna tarjeta dentro o se retiró antes de ser leída", "-1" ),
    /*
     * @since v3.00
     */
    MEDIARETAINED( -202, "La tarjeta ha sido retenida durante los intentos de expulsarla. El dispositivo esta limpio y puede ser utilizado.", "-1" ),
    /*
     * @since v3.00
     */
    RETAINBINFULL( -203, "La bandeja de retención está llena y no se pueden retener más tarjetas. La tarjeta actual todavía está en el dispositivo.", "-2" ),
    /*
     * @since v3.00
     */
    INVALIDDATA( -204, "No se pudo leer la banda o el chip de la tarjeta o se encuentra dañada", "0" ),
    /*
     * @since v3.00
     */
    INVALIDMEDIA( -205, "La tarjeta no pudo ser leída porque no hay track o chip, se insertó mal o está dañada", "0" ),
    /*
     * @since v3.00
     */
    FORMNOTFOUND( -206, "No se encontró el formato para el track", "-1" ),
    /*
     * @since v3.00
     */
    FORMINVALID( -207, "Formato invalido para el track", "-1" ),
    /*
     * @since v3.00
     */
    DATASYNTAX( -208, "La sintaxis de los datos del Track tiene un error o no se ajusta a la definición del formulario", "-1" ),
    /*
     * @since v3.00
     */
    SHUTTERFAIL( -209, "Fallo al abrir el shooter", "-2" ),
    /*
     * @since v3.00
     */
    SECURITYFAIL( -210, "El módulo de seguridad falló al leer la firma de seguridad de las tarjetas", "-2" ),
    /*
     * @since v3.00
     */
    PROTOCOLNOTSUPP( -211, "El protocolo usado para leer el chip no es soportado por el dispositivo", "-1" ),
    /*
     * @since v3.00
     */
    ATRNOTOBTAINED( -212, "No se pudo obtener el ATR", "-1" ),
    /*
     * @since v3.00
     */
    INVALIDKEY( -213, "La llave ingresada no es válida, no se pudo configurar la lectura sin contacto", "-1" ),
    /*
     * @since v3.00
     */
    WRITE_METHOD( -214, "El método de escritura no se encuentra en las capacidades del dispositivo", "-1" ),
    /*
     * @since v3.00
     */
    CHIPPOWERNOTSUPP( -215, "El comando cChipPower no es soportado", "-1" ),
    /*
     * @since v3.00
     */
    CARDTOOSHORT( -216, "La tarjeta introducida es muy corta", "-1" ),
    /*
     * @since v3.00
     */
    CARDTOOLONG( -217, "La tarjeta introducida es muy larga", "-1" ),
    /*
     * @since v3.10
     */
    INVALID_PORT( -218, "Se intentó configurar una luz guía que no existe", "-1" ),
    /*
     * @since v3.10
     */
    POWERSAVETOOSHORT( -219, "El modo de ahorro de energía no se ha activado porque el dispositivo no puede reanudar desde el modo de ahorro de energía dentro del valor especificado", "-1" ),
    /*
     * @since v3.10
     */
    POWERSAVEMEDIAPRESENT( -220, "El modo de ahorro de energía no se ha activado porque no hay medios presentes en el dispositivo", "-1" ),
    /*
     * @since v3.20
     */
    CARDPRESENT( -221, "Se encuentra insertada una tarjeta", "-1" ),
    /*
     * @since v3.20
     */
    POSITIONINVALID( -222, "La posición para ejectar la tarjeta no es soportada", "-1" ),
    /*
     * @since v3.30
     */
    INVALIDTERMINALDATA( -223, "La información de entrada de la terminal no es válida y no se configuro el uso sin contacto correctamente", "-1" ),
    /*
     * @since v3.30
     */
    INVALIDAIDDATA( -224, "El AID de entrada no es válido y no se configuro el uso sin contacto correctamente", "-1" ),
    /*
     * @since v3.30
     */
    INVALIDKEYDATA( -225, "La llave de entrada es invalida y no se configuro el uso sin contacto correctamente", "-1" ),
    /*
     * @since v3.30
     */
    READERNOTCONFIGURED( -226, "El lector de tarjeta no pudo ser configurado", "-2" ),
    /*
     * @since v3.30
     */
    TRANSACTIONNOTINITIATED( -227, "El orden de ejecución de comando es incorrecto", "-1" ),
    /*
     * @since v3.30
     */
    COMMANDUNSUPP( -228, "El comando no es soportado por la lectora", "-1" ),
    /*
     * @since v3.30
     */
    SYNCHRONIZEUNSUPP( -229, "El dispositivo no soporta la sincronización", "-1" ),

    ;

    private final int value;
    private final String message;
    private final String severity;

    private IdcResponse( final int value, String message, String severity ) {
        this.value = value;
        this.message = message;
        this.severity = severity;
    }

    public static IdcResponse getResponseByCode( int code ) {
        for ( IdcResponse d : values() ) {
            if ( d.getValue() == code ) {
                return d;
            }
        }
        throw new UnsupportedOperationException();
    }

    public int getValue() {
        return value;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String getSeverity() {
        return severity;
    }
}
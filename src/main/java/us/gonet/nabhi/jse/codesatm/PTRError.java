package us.gonet.nabhi.jse.codesatm;

public enum PTRError {

    /*
     * @since v3.00
     */
    FORMNOTFOUND( -100L, "El formato especificado no puede ser encontrado", "-1" ),
    /*
     * @since v3.00
     */
    FIELDNOTFOUND( -101L, "El campo especificado no existe o no  pudo ser encontrado", "-1" ),
    /*
     * @since v3.00
     */
    NOMEDIAPRESENT( -102L, "No hay ningún medio presente en la retractación, " +
            "o bien no hubo ningún medio cuando se llamó el comando o se retiró el medio durante la retracción.", "-1" ),
    /*
     * @since v3.00
     */
    READNOTSUPPORTED( -103L, "El dispositivo no tiene la capacidad de lectura", "-1" ),
    /*
     * @since v3.00
     */
    FLUSHFAIL( -104L, "El dispositivo no pudo vaciar los datos", "-1" ),
    /*
     * @since v3.00
     */
    MEDIAOVERFLOW( -105L, "La solicitud de impresión ha desbordado el material de impresión", "-1" ),
    /*
     * @since v3.00
     */
    FIELDSPECFAILURE( -106L, " La sintaxis del campo de entrada es inválida", "-1" ),
    /*
     * @since v3.00
     */
    FIELDERROR( -107L, "Se produjo un error al procesar un campo, lo que provocó la terminación de la solicitud de impresión", "-1" ),
    /*
     * @since v3.00
     */
    MEDIANOTFOUND( -108L, "No se pudo encontrar la definición de medios especificada", "-1" ),
    /*
     * @since v3.00
     */
    EXTENTNOTSUPPORTED( -109L, "El dispositivo no puede reportar la extensión", "-1" ),
    /*
     * @since v3.00
     */
    MEDIAINVALID( -110L, "La definición de medios especificada es inválida", "-1" ),
    /*
     * @since v3.00
     */
    FORMINVALID( -111L, "El formato especificado es invalido", "-1" ),
    /*
     * @since v3.00
     */
    FIELDINVALID( -112L, "El campo especificado es invalido ", "-1" ),
    /*
     * @since v3.00
     */
    MEDIASKEWED( -113L, "El sesgo de los medios excedió el límite en la definición del formulario", "-1" ),
    /*
     * @since v3.00
     */
    RETRACTBINFULL( -114L, "La bandeja de retracción está llena. No se pueden retraer más medios. " +
            "Los medios actuales todavía están en el dispositivo.", "-1" ),
    /*
     * @since v3.00
     */
    STACKERFULL( -115L, "El apilador interno está lleno. No se pueden mover más medios al apilador", "-1" ),
    /*
     * @since v3.00
     */
    PAGETURNFAIL( -116L, "El dispositivo no pudo pasar la página", "-1" ),
    /*
     * @since v3.00
     */
    MEDIATURNFAIL( -117L, "El dispositivo no pudo girar los medios insertados ", "-1" ),
    /*
     * @since v3.00
     */
    SHUTTERFAIL( -118L, "Error al abrir o cerrar el obturador debido a manipulación o error de xfs.", "-2" ),
    /*
     * @since v3.00
     */
    MEDIAJAMMED( -119L, "El dispositivo está atascado", "-2" ),
    /*
     * @since v3.00
     */
    FILE_IO_ERROR( -120L, "El directorio no existe o se produjo un error en el al procesar el archivo", "-1" ),
    /*
     * @since v3.00
     */
    CHARSETDATA( -121L, "Los conjuntos de caracteres admitidos por el Proveedor de servicios son inconsistentes con los parámetros de entrada", "-1" ),
    /*
     * @since v3.00
     */
    PAPERJAMMED( -122L, "El papel se encuentra atascado", "-2" ),
    /*
     * @since v3.00
     */
    PAPEROUT( -123L, "No hay papel para imprimir", "-2" ),
    /*
     * @since v3.00
     */
    INKOUT( -124L, "No es posible imprimir, el suministro de tinta puede estar vacío", "-2" ),
    /*
     * @since v3.00
     */
    TONEROUT( -125L, "El suministro de tóner o tinta está vacío o el contraste de impresión con la cinta no es suficiente", "-2" ),
    /*
     * @since v3.00
     */
    LAMPINOP( -126L, "La lámpara de imagen no funciona", "-2" ),
    /*
     * @since v3.00
     */
    SOURCEINVALID( -127L, "La fuente de papel seleccionada no es soportada por el dispositivo", "-2" ),
    /*
     * @since v3.00
     */
    SEQUENCEINVALID( -128L, "Error de secuencia de programación", "-2" ),
    /*
     * @since v3.00
     */
    MEDIASIZE( -129L, "El medio ingresado tiene un tamaño incorrecto y el medio permanece dentro del dispositivo", "-2" ),
    /*
     * @since v3.10
     */
    INVALID_PORT( -130L, "No existe la luz guía que se intentó configurar", "-1" ),
    /*
     * @since v3.10
     */
    MEDIARETAINED( -131L, "Los medios han sido retirados en un intento de expulsarlos. El dispositivo esta limpio y puede ser utilizado", "00" ),
    /*
     * @since v3.10
     */
    BLACKMARK( -132L, "La detección de la marca negra ha fallado, no se ha impreso nada", "-2" ),
    /*
     * @since v3.10
     */
    DEFINITIONEXISTS( -133L, "La forma o definición de medios especificada ya existe y el indicador Overwrite era FALSE.", "-1" ),
    /*
     * @since v3.10
     */
    MEDIAREJECTED( -134L, "El papel ha sido rechazado y presentado al usuario o no se ha impreso nada", "-2" ),
    /*
     * @since v3.10
     */
    MEDIARETRACTED( -135L, "Los medios presentados se retrajeron automáticamente antes de que se pudieran " +
            "presentar y antes de que el comando pudiera completarse con éxito", "-1" ),
    /*
     * @since v3.10
     */
    MSFERROR( -136L, "La operación de lectura de MSF especificada por la definición de formularios " +
            "no se pudo completar con éxito debido a datos de banda magnética no válidos", "-1" ),
    /*
     * @since v3.10
     */
    NOMSF( -137L, "No se encontró una banda magnética.", "-1" ),
    /*
     * @since v3.10
     */
    FILENOTFOUND( -138L, "El archivo especificado no pudo ser encontrado", "-1" ),
    /*
     * @since v3.10
     */
    POWERSAVETOOSHORT( -139L, "El modo de ahorro de energía no se ha activado porque el " +
            "dispositivo no puede reanudar desde el modo de ahorro de energía dentro del valor especificado", "-1" ),
    /*
     * @since v3.10
     */
    POWERSAVEMEDIAPRESENT( -140L, "El modo de ahorro de energía no se ha activado porque hay medios presentes en el dispositivo.", "-1" ),
    /*
     * @since v3.20
     */
    PASSBOOKCLOSED( -141L, "Quedaban menos páginas de las especificadas para girar. Como resultado de la operación, la libreta se ha cerrado", "-1" ),
    /*
     * @since v3.20
     */
    LASTORFIRSTPAGEREACHED( -142L, "La impresora no puede cerrar la libreta " +
            "porque quedaron menos páginas de las especificadas para girar. Como resultado de la operación, la última " +
            "o la primera página se ha alcanzado y está abierta.", "-1" ),
    /*
     * @since v3.30
     */
    COMMANDUNSUPP( -143L, " El comando especificado no es soportado por el dispositivo", "-1" ),
    /*
     * @since v3.30
     */
    SYNCHRONIZEUNSUPP( -144L, "El sincronización falló porque no es soportada por el dispositivo", "-1" );

    private final long value;
    private String message;
    private String severity;

    PTRError( final long value, String message, String severity ) {
        this.value = value;
        this.message = message;
        this.severity = severity;
    }

    public String getMessage() {
        return message;
    }

    public long getValue() {
        return value;
    }

    public String getMessageByCode( long code ) {
        for ( PTRError d : values() ) {
            if ( d.getValue() == code ) {
                return d.message;
            }
        }
        return "Unkown error!";
    }

    public String getSeverityByCode( long code ) {
        for ( PTRError d : values() ) {
            if ( d.getValue() == code ) {
                return d.severity;
            }
        }
        return "Unkown error!";
    }
}

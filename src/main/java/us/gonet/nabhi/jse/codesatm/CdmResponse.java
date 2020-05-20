package us.gonet.nabhi.jse.codesatm;

public enum CdmResponse implements AtmResponse {

    SUCCESS( 300, "Termina de dispensar el monto solicitado", "00" ),
    /*
     * @since v3.00
     */
    //TOTAL
    INVALIDCURRENCY( -300, "No hay caseteros con la moneda especificada o no está disponible por el momento", "-1" ),
    /*
     * @since v3.00
     */
    //TOTAL
    INVALIDTELLERID( -301, "El ID del dispensador no es valido", "-1" ),
    /*
     * @since v3.00
     */

    //******PARCIAL
    CASHUNITERROR( -302, "Ocurrió un error con una casetera, se generará un evento WFS_EXEE_CDM_CASHUNITERROR con los detalles", "-1" ),
    /*
     * @since v3.00
     */
    INVALIDDENOMINATION( -303, "La suma de los montos por casetera es diferente al monto total especificado", "-1" ),
    /*
     * @since v3.00
     */
    INVALIDMIXNUMBER( -304, "El algoritmo no pudo ser encontrado o el número de combinación no es valido", "-1" ),
    /*
     * @since v3.00
     */
    NOCURRENCYMIX( -305, "Se seleccionaron dos o más caseteras con diferente tipo de moneda o una casetero tiene dos tipos de moneda diferentes.", "-1" ),
    /*
     * @since v3.00
     */
    //TOTAL
    NOTDISPENSABLE( -306, "No se ha podido dispensar el monto", "-1" ),
    /*
     * @since v3.00
     */
    //TOTAL
    TOOMANYITEMS( -307, "La solicitud de retiro requiere más billetes de los que pueden ser dispensados por el dispositivos", "-1" ),
    /*
     * @since v3.00
     */
    //TOTAL
    UNSUPPOSITION( -308, "La posición de salida especificada no es soportada por el dispositivo", "-1" ),
    /*
     * @since v3.00
     */
    SAFEDOOROPEN( -310, "La puerta de la caja fuerte está abierta, debe estar cerrada para realizar el comando solicitado", "-2" ),
    /*
     * @since v3.00
     */
    SHUTTERNOTOPEN( -312, "El shuter no pudo ser abierto cuando se solicitó y los artículos no fueron presentados al usuario", "-2" ),
    /*
     * @since v3.00
     */
    //TOTAL
    SHUTTEROPEN( -313, "El shutter está abierto cuando debería estar cerrado, los artículos no fueron presentados al usuario", "-2" ),
    /*
     * @since v3.00
     */
    //TOTAL
    SHUTTERCLOSED( -314, "El shutter está cerrado cuando debería de estar abierto", "-2" ),
    /*
     * @since v3.00
     */

    //TOTAL
    INVALIDCASHUNIT( -315, "El número de casetera solicitado no es válido o se intentó dispensar de la casetera de rechazos", "-1" ),
    /*
     * @since v3.00
     */
    NOITEMS( -316, "No había artículos para retraer", "-1" ),
    /*
     * @since v3.00
     */

    //TOTAL
    EXCHANGEACTIVE( -317, "El dispensador ya ejecutó un comando Exchange y se encuentra en un estado EXCHANGEACTIVE", "-1" ),
    /*
     * @since v3.00
     */
    NOEXCHANGEACTIVE( -318, "No se ejecutó un comando EXCHANGEACTIVE", "-1" ),
    /*
     * @since v3.00
     */
    SHUTTERNOTCLOSED( -319, "Falló al cerrar el shutter", "-2" ),
    /*
     * @since v3.00
     */

    //TOTAL
    PRERRORNOITEMS( -320, "Se produjo un error mientras los artículos se movían a la ranura de salida; no se presentaron los  artículos", "-1" ),
    /*
     * @since v3.00
     */

    //******PARCIAL
    PRERRORITEMS( -321, "Se produjo un error mientras los artículos se movían a la ranura de salida; se presentaron algunos artículos", "-1" ),
    /*
     * @since v3.00
     */

    //******PARCIAL
    PRERRORUNKNOWN( -322, "Se produjo un error mientras los artículos se movían a la ranura de salida; se desconoce la posición de los artículos; se requiere intervención para conocer los montos totales", "-2" ),
    /*
     * @since v3.00
     */
    ITEMSTAKEN( -323, "Los artículos presentados fueron tomados por el usuario", "0" ),
    /*
     * @since v3.00
     */
    INVALIDMIXTABLE( -327, "El contenido de al menos una de las filas definidas de la tabla de combinaciones es incorrecto", "-1" ),
    /*
     * @since v3.00
     */
    OUTPUTPOS_NOT_EMPTY( -328, "", "0" ),
    /*
     * @since v3.00
     */
    INVALIDRETRACTPOSITION( -329, "La posición de retracción no es válida o no es soportada por el dispositivo", "-1" ),
    /*
     * @since v3.00
     */
    NOTRETRACTAREA( -330, "El área de retracción especificada no es soportada por el dispositivo", "-1" ),
    /*
     * @since v3.00
     */
    NOCASHBOXPRESENT( -333, "No está presente la caja de efectivo solicitada en el comando", "-1" ),
    /*
     * @since v3.00
     */
    AMOUNTNOTINMIXTABLE( -334, "Se está usando una tabla de cambio para cambiar el tipo de moneda pero el valor de la moneda no está en la tabla", "-1" ),
    /*
     * @since v3.00
     */
    ITEMSNOTTAKEN( -335, "No se han tomado los artículos dispensados en el tiempo de timeOut establecido", "-1" ),
    /*
     * @since v3.00
     */
    ITEMSLEFT( -336, "Los artículos se han dejado en la ranura de transporte o de salida como resultado de una operación de entrega de efectivo anterior, actual o de reciclador", "-2" ),
    /*
     * @since v3.10
     */
    INVALID_PORT( -337, "Se intentó configurar una luz guía que no existe", "-1" ),
    /*
     * @since v3.10
     */
    POWERSAVETOOSHORT( -338, "El modo de ahorro de energía no se ha activado porque el dispositivo no puede reanudar desde el modo de ahorro de energía dentro del valor especificado", "-1" ),
    /*
     * @since v3.10
     */
    POWERSAVEMEDIAPRESENT( -339, "El modo de ahorro de energía no se ha activado porque hay medios presentes en el dispositivo", "-1" ),
    /*
     * @since v3.30
     */
    POSITION_NOT_EMPTY( -340, "El área de retracción especificada en la entrada no está vacía por lo que la operación de retractación no es posible", "-2" ),
    /*
     * @since v3.30
     */
    INCOMPLETERETRACT( -341, "Algunos o todos los artículos no fueron retractados por una razón no cubierta por otros códigos de error. Los detalles se informarán con el evento WFS_EXEE_CDM_INCOMPLETERETRACT", "-2" ),
    /*
     * @since v3.30
     */
    COMMANDUNSUPP( -342, "El comando solicitado no es soportado por el dispositivo", "-1" ),
    /*
     * @since v3.30
     */
    SYNCHRONIZEUNSUPP( -343, "La sincronización no es soportada por el dispositivo", "-1" ),
    UNSSUPP( 999, "UNSSUPPORTED", "##" );

    private final int value;
    private final String message;
    private final String severity;

    private CdmResponse( final int value, String message, String severity ) {
        this.value = value;
        this.message = message;
        this.severity = severity;
    }

    public static CdmResponse getResponseByCode( int code ) {
        for ( CdmResponse d : values() ) {
            if ( d.getValue() == code ) {
                return d;
            }
        }
        throw new UnsupportedOperationException();
    }

    public CdmResponse getErrorByCode( int code ) {
        for ( CdmResponse d : values() ) {
            if ( d.getValue() == code ) {
                return d;
            }
        }
        return this;
    }

    public long getValue() {
        return value;
    }

    @Override
    public String getSeverity() {
        return severity;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

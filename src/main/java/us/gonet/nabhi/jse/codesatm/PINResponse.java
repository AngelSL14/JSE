package us.gonet.nabhi.jse.codesatm;

public enum PINResponse implements AtmResponse {

    WFS_ERR_PIN_KEYNOTFOUND( -400, "La llave especificada no pudo ser encontrada", "-1" ),
    WFS_ERR_PIN_MODENOTSUPPORTED( -401, "El modo especificado no es soportado por el dispositivo", "-1" ),
    WFS_ERR_PIN_ACCESSDENIED( -402, "El módulo de encripción no está inicializado o no está listo por algún motivo del proveedor", "-1" ),
    WFS_ERR_PIN_INVALIDID( -403, "El parámetro ID no es valido", "-1" ),
    WFS_ERR_PIN_DUPLICATEKEY( -404, "Ya existe una llave con el mismo nombre y no puede ser sobrescrita", "-1" ),
    WFS_ERR_PIN_KEYNOVALUE( -406, "La llave fue encontrada pero no pudo ser cargada", "-1" ),
    WFS_ERR_PIN_USEVIOLATION( -407, "El uso especificado no es soportado por el dispositivo", "-1" ),
    WFS_ERR_PIN_NOPIN( -408, "No se ingresó ningún pin o fue borrado", "-1" ),
    WFS_ERR_PIN_INVALIDKEYLENGTH( -409, "La longitud de la llave ingresada o del pin no es compatible con el método de encriptación utilizado", "-1" ),
    /**
     * At least one of the specified function keys or FDKs is invalid.
     */
    WFS_ERR_PIN_KEYINVALID( -410, "Se ingresó una tecla invalida", "-1" ),
    /**
     * At least one of the specified function keys or FDKs is not supported by
     * the service provider.
     */
    WFS_ERR_PIN_KEYNOTSUPPORTED( -411, "Se está activando una tecla no soportada por el dispositivo o la versión", "-1" ),
    /**
     * There are no active function keys specified.
     */
    WFS_ERR_PIN_NOACTIVEKEYS( -412, "No se activó ninguna tecla", "-1" ),
    WFS_ERR_PIN_INVALIDKEY( -413, "Llave Invalida", "-1" ),//NO ESTA EN LOS MANUALES ??
    WFS_ERR_PIN_NOTERMINATEKEYS( -414, "No se activó ninguna tecla de terminación y es falso en autoterminar", "-1" ),
    WFS_ERR_PIN_MINIMUMLENGTH( -415, "El campo de longitud mínima del pin no es válido o mayor a la longitud máxima del pin", "-1" ),
    WFS_ERR_PIN_PROTOCOLNOTSUPP( -416, "El protocolo especificado no es soportado por el proveedor del dispositivo", "-1" ),
    WFS_ERR_PIN_INVALIDDATA( -417, "Ocurrió un error durante la comunicación con el chip", "-1" ),
    WFS_ERR_PIN_NOTALLOWED( -418, "El pin ingresado por el usuario no es permitido", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_NOKEYRAM( -419, "No queda espacio en la  RAM de la llave para el tipo de clave especificado", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_NOCHIPTRANSACTIVE( -420, "Se está usando una llave de tarjeta de chip como clave de cifrado y no hay una transacción de chip activa", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_ALGORITHMNOTSUPP( -421, "El algoritmo utilizado no es soportado por la llave o el dispositivo", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_FORMATNOTSUPP( -422, "El formato utilizado no es soportado por la versión o el dispositivo", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_HSMSTATEINVALID( -423, "El módulo HSM no está en un estado correcto para manejar el comando", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_MACINVALID( -424, "La MAC del mensaje no es correcta", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_PROTINVALID( -425, "El protocolo especificado es invalido", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_FORMATINVALID( -426, "El formato del mensaje o del KeyBlock es invalido", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_CONTENTINVALID( -427, "El contenido de uno de los campos relevantes de seguridad es invalido", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_SIG_NOT_SUPP( -429, "El dispositivo no admite el algoritmo de firma solicitado. La llave fue descartada", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_INVALID_MOD_LEN( -431, "La longitud del modulo especificado no es valida", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_INVALIDCERTSTATE( -432, "El módulo de certificado se encuentra en un estado en el que la solicitud no es válida", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_KEY_GENERATION_ERROR( -433, "El EPP no pudo generar el par de llaves", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_EMV_VERIFY_FAILED( -434, "La verificación de la llave importada fallo y fue descartada", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_RANDOMINVALID( -435, "El número aleatorio cifrado en los datos de entrada no coincide con el " +
            "proporcionado  previamente por el EPP. La clave no está almacenada en el PIN", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_SIGNATUREINVALID( -436, "La verificación de la firma falló. La llave no pudo ser borrada o almacenada", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_SNSCDINVALID( -437, "El numero  serial del SCD es invalido", "-1" ),
    /**
     * @since 3.00
     */
    WFS_ERR_PIN_NORSAKEYPAIR( -438, "El PinPad no tiene una llave privada", "-1" ),
    /**
     * @since 3.10
     */
    WFS_ERR_PIN_INVALID_PORT( -439, "No existe la luz guía que se intento activar", "-1" ),
    /**
     * @since 3.10
     */
    WFS_ERR_PIN_POWERSAVETOOSHORT( -440, "El modo de ahorro de energía no se ha activado", "-1" ),

    SUCCESS( 400, "Termina de ingresar Nip", "00" );

    private final int value;
    private String message;
    private String severity;

    private PINResponse( final int value, String message, String severity ) {
        this.value = value;
        this.message = message;
        this.severity = severity;
    }


    public static PINResponse getResponseByCode( int code ) {
        for ( PINResponse d : values() ) {
            if ( d.getValue() == code ) {
                return d;
            }
        }
        throw new UnsupportedOperationException();
    }


    public long getValue() {
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

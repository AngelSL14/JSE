package us.gonet.nabhi.jse.codesatm;

public enum GenericError {

    SUCCESS( 0L, "Comando exitoso", "0" ),
    ALREADY_STARTED( -1L, "El dispositivo ya fue iniciado y se trata de volver a iniciar", "0" ),
    API_VER_TOO_HIGH( -2L, "La versión de XFS que usa la aplicación es superior a la soportada por la API XFS", "-2" ),
    API_VER_TOO_LOW( -3L, "La versión de XFS que usa la aplicación es anterior a la soportada por la API XFS", "-2" ),
    CANCELED( -4L, "El comando fue cancelado", "0" ),
    CFG_INVALID_HKEY( -5L, "El parámetro especificado no corresponde a ningún registro del sistema abierto en el momento", "-1" ),
    CFG_INVALID_NAME( -6L, "El nombre especificado no existe en el registro actual", "-1" ),
    CFG_INVALID_SUBKEY( -7L, "La llave especificada no existe", "-1" ),
    CFG_INVALID_VALUE( -8L, "El valor especificado no existe en el registro abierto", "-1" ),
    CFG_KEY_NOT_EMPTY( -9L, "La llave y sub llaves no pueden ser borradas, primero deben borrarse las sub llaves", "-1" ),
    CFG_NAME_TOO_LONG( -10L, "La longitud del nombre a ser retornado excede la longitud del buffer", "-1" ),
    CFG_NO_MORE_ITEMS( -11L, "No hay más sub llaves para retornar", "-1" ),
    CFG_VALUE_TOO_LONG( -12L, "La longitud del valor a ser retornado excede la longitud del buffer", "-1" ),
    DEV_NOT_READY( -13L, "La función requería acceso al dispositivo y  el dispositivo no estaba listo.", "-2" ),
    HARDWARE_ERROR( -14L, "La función requería acceso al dispositivo y ocurrió un error en el dispositivo", "-2" ),
    INTERNAL_ERROR( -15L, "Se produjo una inconsistencia interna u otro error inesperado en el subsistema XFS", "-2" ),
    INVALID_ADDRESS( -16L, "Se esta apuntando a un puntero donde el buffer no fue previamente alojado en la memoria.", "-1" ),
    INVALID_APP_HANDLE( -17L, "El identificador de aplicación especificado no es válido, no fue creado por una llamada de creación anterior.", "-1" ),
    INVALID_BUFFER( -18L, "El parámetro de salida no es un  puntero a una estructura de buffer asignada", "-1" ),
    INVALID_CATEGORY( -19L, "La categoria solicitada no es compatible con la clase de servicio", "-1" ), // TO CHANGE
    INVALID_COMMAND( -20L, "El comando solicitado no es compatible con el dispositivo", "-1" ),
    INVALID_EVENT_CLASS( -21L, "El evento generado no es soportado por el dispositivo", "-1" ),
    INVALID_HSERVICE( -22L, "El parámetro hService no es un identificador de servicio valido", "-1" ),
    INVALID_HPROVIDER( -23L, "El parámetro hProvider no es un identificador de proveedor valido ", "-1" ),
    INVALID_HWND( -24L, "El parámetro hWnd no es un registro de Windows válido", "-1" ),
    INVALID_HWNDREG( -25L, "El parámetro hWndReg no es un registro de Windows valido", "-1" ),
    INVALID_POINTER( -26L, "El puntero no apunta a un bloque de memoria  accesible", "-1" ),
    INVALID_REQ_ID( -27L, "El parámetro RequestId no corresponde a una solicitud activa de servicio", "-" ),
    INVALID_RESULT( -28L, "El parámetro lpResult no es un puntero a una estructura WFSRESULT", "-1" ),
    INVALID_SERVPROV( -29L, "El archivo que contiene el proveedor de servicios no es válido o está dañado", "-2" ),
    INVALID_TIMER( -30L, "Los parámetros hWnd y wTimerID no corresponden a un temporizador activo actualmente", "-1" ),
    INVALID_TRACELEVEL( -31L, "El parámetro dwTraceLevel no corresponde a un nivel de seguimiento válido o conjunto de niveles", "-1" ),
    LOCKED( -32L, "El dispositivo está bloqueado bajo un hService diferente", "-2" ),
    NO_BLOCKING_CALL( -33L, "No hay una llamada de bloqueo pendiente para el hilo especificado", "-1" ),
    NO_SERVPROV( -34L, "El archivo que contiene el proveedor de servicios no existe", "-2" ),
    NO_SUCH_THREAD( -35L, "El hilo especificado no existe", "-1" ),
    NO_TIMER( -36L, " El temporizador solicitado no existe", "-1" ),
    NOT_LOCKED( -37L, "La aplicación que solicita el desbloqueo de un servicio no había realizado previamente un WFSLock o WFSAsyncLock exitoso", "-1" ),
    NOT_OK_TO_UNLOAD( -38L, "El Administrador de XFS no puede descargar la DLL del proveedor de servicios", "-2" ),
    NOT_STARTED( -39L, "La aplicación no ha realizado previamente un WFSStartUp exitoso del dispositivo", "-1" ),
    NOT_REGISTERED( -40L, "El registro de Windows hWndReg especificado no se registró para recibir mensajes para ninguna clase de evento", "-2" ),
    OP_IN_PROGRESS( -41L, "Una operación de bloqueo está en progreso en el hilo; solo WFSCancelBlockingCall y WFSIsBlocking están permitidos en este momento", "-1" ),
    OUT_OF_MEMORY( -42L, "No hay suficiente memoria disponible para satisfacer la solicitud", "-2" ),
    SERVICE_NOT_FOUND( -43L, "El nombre lógico no es un nombre de proveedor de servicios válido", "-2" ),
    SPI_VER_TOO_HIGH( -44L, "El rango de versiones de compatibilidad con SPI de XFS solicitada por el Administrador de XFS es mayor que cualquier otra compatibilidad con el Proveedor de servicios para el servicio lógico que se abre", "-2" ),
    SPI_VER_TOO_LOW( -45L, "El rango de versiones de compatibilidad con SPI de XFS solicitada por el Administrador de XFS es menor que cualquier otra compatibilidad con el Proveedor de servicios para el servicio lógico que se abre", "-2" ),
    SRVC_VER_TOO_HIGH( -46L, "", "-2" ),
    SRVC_VER_TOO_LOW( -47L, "", "-2" ),
    TIMEOUT( -48L, "El tiempo de espera ha terminado", "-1" ),
    UNSUPP_CATEGORY( -49L, "El dwCategory emitido, aunque válido para esta clase de servicio, no es compatible con este proveedor de servicios", "-1" ),
    UNSUPP_COMMAND( -50L, "El dwCommand emitido, aunque válido para esta clase de servicio, no es compatible con este proveedor de servicios", "-1" ),
    VERSION_ERROR_IN_SRVC( -51L, "", "-2" ),
    INVALID_DATA( -52L, "La estructura de datos pasada como parámetro de entrada contiene datos no válidos", "-1" ),
    SOFTWARE_ERROR( -53L, "La función requería acceso a la información de configuración y se produjo un error en el software", "-2" ),
    CONNECTION_LOST( -54L, "", "-2" ),
    /**
     * A user is preventing proper operation of the device.
     *
     * @since 3.00
     */
    USER_ERROR( -55L, "El usuario está impidiendo el correcto funcionamiento del dispositivo", "-2" ),
    /**
     * The data structure passed as an input parameter although valid for this
     * service class, is not supported by this service provider or device.
     *
     * @since 3.00
     */
    UNSUPP_DATA( -56L, "La estructura de datos que se pasa como parámetro de entrada, aunque es válida para esta clase de servicio, no es compatible con este proveedor de servicios o dispositivo", "-1" );

    private final long value;
    private final String message;
    private final String severity;

    private GenericError( final long value, String message, String severity ) {
        this.value = value;
        this.message = message;
        this.severity = severity;
    }

    public static String getMessageByCode( long code ) {
        for ( GenericError d : values() ) {
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

    public String getSeverity() {
        return severity;
    }
}

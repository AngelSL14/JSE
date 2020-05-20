package us.gonet.nabhi.jse.codesatm;

public enum CdmEvent implements AtmEvent {
    //0 : Guardar en Journal
    //1 : Cambio en la BD caseteros
    //2 : Solo en device status
    //3 : Guardar en device notif
    CASH_UNIT_CHANGE( 304, "Cambio el estado de las caseteras", "0101" ),
    CASH_UNIT_ERROR( 308, "Error en alguna casetera ", "0001" ),
    ;


    CdmEvent( int code, String journalMessage, String actions ) {
        this.code = code;
        this.journalMessage = journalMessage;
        this.actions = actions;
    }

    private final int code;
    private final String journalMessage;

    @Override
    public String getActions() {
        return actions;
    }

    private final String actions;

    public int getCode() {
        return code;
    }

    @Override
    public String getJournalMessage() {
        return journalMessage;
    }


    public static CdmEvent getEventFromCode( int code ) {
        for ( CdmEvent e : values() ) {
            if ( e.getCode() == code ) {
                return e;
            }
        }
        throw new UnsupportedOperationException();
    }
}

package us.gonet.nabhi.jse.codesatm;


public enum IdcEvent implements AtmEvent {
    //0 : Guardar en Journal
    //1 : Guardar en device status
    //2 : Guardar en device notif
    CARD_REMOVED( 204, "Tarjeta retirada", "001" ),
    INSERT_CARD( 212, "Listo para recibir tarjeta", "001" ),
    CARD_INSERTED( 203, "Tarjeta introducida", "001" ),
    ;


    IdcEvent( int code, String journalMessage, String actions ) {
        this.code = code;
        this.journalMessage = journalMessage;
        this.actions = actions;
    }

    private final int code;
    private final String journalMessage;
    private final String actions;

    @Override
    public String getActions() {
        return actions;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getJournalMessage() {
        return journalMessage;
    }


    public static IdcEvent getEventFromCode( int code ) {
        for ( IdcEvent e : values() ) {
            if ( e.getCode() == code ) {
                return e;
            }
        }
        throw new UnsupportedOperationException();
    }
}

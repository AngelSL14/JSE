package us.gonet.nabhi.jse.codesatm;

public enum PinEvent implements AtmEvent {
    //0 : Guardar en Journal
    //1 : Guardar en device status
    //2 : Guardar en device notif
    UNSSUPPORTED( 999, "UNSSUPPORTED", "000" );
    private final int code;
    private final String journalMessage;
    private final String actions;

    PinEvent( int code, String journalMessage, String actions ) {
        this.code = code;
        this.journalMessage = journalMessage;
        this.actions = actions;

    }

    @Override
    public String getActions() {
        return actions;
    }

    public static PinEvent getEventFromCode( int code ) {
        for ( PinEvent e : values() ) {
            if ( e.getCode() == code ) {
                return e;
            }
        }
        throw new UnsupportedOperationException();
    }

    public int getCode() {
        return code;
    }

    @Override
    public String getJournalMessage() {
        return null;
    }
}

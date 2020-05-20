package us.gonet.nabhi.jse.utils;

public enum ToAccounting {
    ISO00( "00", "Not Account Specified" ),
    ISO10( "10", "Savings" ),
    ISO20( "20", "Checking" ),
    ISO30( "30", "Credit" ),
    ;
    private String code;
    private String action;

    ToAccounting( String code, String action ) {
        this.code = code;
        this.action = action;
    }

    public String getCodeAsString() {
        return code;
    }

    public int getCodeAsNumber() {
        return Integer.parseInt( code );
    }

    public String getAction() {
        return action;
    }


    public static String getMessageFromCode( String code ) {
        for ( ToAccounting coder : ToAccounting.values() ) {
            if ( coder.getCodeAsString().equals( code ) ) {
                return coder.toString();
            }
        }
        return null;
    }
}

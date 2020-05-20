package us.gonet.nabhi.jse.utils;

public enum FromAccounting {
    ISO20( "20", "Checking account type" ),
    ISO10( "10", "Savings account type" ),
    ISO30( "30", "Credit account type" ),
    ISO9M( "9M", "Other account type" ),
    ;
    private String code;
    private String action;

    FromAccounting( String code, String action ) {
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
        for ( FromAccounting coder : FromAccounting.values() ) {
            if ( coder.getCodeAsString().equals( code ) ) {
                return coder.toString();
            }
        }
        return null;
    }
}

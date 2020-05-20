package us.gonet.nabhi.jse.utils;

public enum OriginAccount {
    CHECKING_ACCOUNT( "20", "Cuenta de Debito" ),
    CREDIT_ACCOUNT( "30", "Cuenta de Credito" ),
    NO_ACCOUNT( "00", "Sin cuenta" ),
    OTHER_ACCOUNT( "9M", "Otro" ),
    SAVING_ACCOUNT( "10", "Cuenta de Ahorro" );


    OriginAccount( String value, String tipo ) {
        this.value = value;
        this.tipo = tipo;
    }

    private final String tipo;

    private String value;

    public String getValue() {
        return value;
    }

    public String getTipo() {
        return tipo;
    }

    public static String getCodeFromType( String type ) {
        String val = "";
        for ( OriginAccount a : values() ) {
            if ( a.getTipo().equals( type ) ) {
                val = a.getValue();
            }
        }
        return val;
    }

    public static String getTypeFromCode( String code ) {
        String val = "";
        for ( OriginAccount a : values() ) {
            if ( a.getValue().equals( code ) ) {
                val = a.getTipo();
            }
        }
        return val;
    }
}

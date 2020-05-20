package us.gonet.nabhi.jse.utils;

public enum DestinationAccount {
    NOT_ACCOUNT_SPECIFIED( "00", "Cuenta no especificada" ),
    SAVINGS( "10", "Cuenta de Ahorro" ),
    CHECKING( "20", "Cuenta de Debito" ),
    CREDIT( "30", "Cuenta de Credito" );

    private String value;
    private final String tipo;

    DestinationAccount( String value, String tipo ) {
        this.value = value;
        this.tipo = tipo;
    }

    public String getValue() {
        return value;
    }

    public String getTipo() {
        return tipo;
    }

    public static String getTypeFromCode( String code ) {
        String val = "";
        for ( DestinationAccount a : values() ) {
            if ( a.getValue().equals( code ) ) {
                val = a.getTipo();
            }
        }
        return val;
    }
}
